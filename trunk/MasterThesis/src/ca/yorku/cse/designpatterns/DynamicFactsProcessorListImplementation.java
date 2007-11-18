package ca.yorku.cse.designpatterns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * The dynamicFactsProcessor takes the file name of the dynamic facts XML file, parses it and stores all Nodes in a Document.  The next step involves some postprocessing: <br> - add order number for each method call (incrementing) - write transformed dynamic facts to a text file 
 * @author  Marcel Birkner
 * @version  0.8
 * @since  27 June, 2007
 */
public class DynamicFactsProcessorListImplementation implements DynamicFactsProcessorInterface {

	/**
	 * Variable used for debugging
	 */
	private boolean debug;
	private static String filename = "" ;
	private static DynamicFactsProcessorListImplementation dynFacts = null;

	/**
	 * Document that stores all dynamic facts. All transformations are
	 * made on this Document Object.
	 */
	private Document dynamicFactsDocument;

	private DynamicFactsProcessorListImplementation(String file, boolean debug) {
		this.debug = debug;
		this.filename = file;	
	}
		
	/**
	 * Singleton
	 */
	public static Document getDynamicFacts(String file, boolean debug) {
		String f1 = file.replace(".dynamicfacts", "");
		String f2 = filename.replace(".xml", "");
		if ( f1.equalsIgnoreCase(f2) ){
			//System.out.println("getDynamicFacts: already exists, file="+f1+" filename="+f2);
			return dynFacts.getDynamicFactsDocument();
		} else {
			//System.out.println("getDynamicFacts: is created new, file="+f1+" filename="+f2);
			dynFacts = new DynamicFactsProcessorListImplementation(file, debug);
			dynFacts.processDynamicFacts();
		}		
		return dynFacts.getDynamicFactsDocument();
	}
	


	public boolean processDynamicFacts(){
		boolean results = false;

		/*
		 * Transform TXT file to XML format
		 */
		this.filename = transformToXML( filename );

		/*
		 * Read file and convert XML file into a Document.
		 * Manipulate the data to fit our format.
		 */
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			this.dynamicFactsDocument = db.parse( new File(filename) );

			parseDocument(this.dynamicFactsDocument);	    
			addOrderOfElements(this.dynamicFactsDocument);
			results = true;
		} catch (Exception e) {
			System.out.println("DynamicFactsProcessor: Cannot parse document! " + filename + "\n" +
					"Please make sure the the filename is correct. Another reason for this " +
					"exception could be an invalid format \nof the dynamic facts data. \n\n" +
					"Exit Program.");
			e.getMessage();
			e.getStackTrace();
			System.exit(1);
		}	


		/*
		 * Write Document to XML file. Store transformed Document
		 * in XML file. Write full method call facts to a file. 
		 */
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = null;
		try {
			trans = tf.newTransformer();
			Source source = new DOMSource(this.dynamicFactsDocument);
			removeExitTags(this.dynamicFactsDocument);

			// filename = filename.replace(".xml", "_transformed.xml");
			Result result = new StreamResult(new File(filename));
			trans.transform(source, result);
			results = true;
		} catch (Exception e) {
			System.out.println("DynamicFactsProcessor: Cannot store document in XML file: " + filename);
			e.getStackTrace();
			System.exit(1);
		}
		return results;
	}




	/**
	 * This method reads the dynamic facts that are produced by 
	 * from a textfile probekit. It will transform the input into
	 * a valid XML file by adding one root node and checking for 
	 * valid opening and closing tags. The XML file will be verified
	 * against an XML schema. 
	 */
	private String transformToXML(String filenameTXT) {

		BufferedReader inputStream  = null;
		PrintWriter    outputStream = null;

		int entryCount = 0;
		int exitCount  = 0;

		String filenameXML = filenameTXT.replace(".dynamicfacts", ".xml");
		String headerXML   = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
		"<!DOCTYPE entry SYSTEM \"schema.dtd\">" +
		"<entry args=\"\" calledByClass=\"\" calledByMethod=\"\" calledByObject=\"\" className=\"\" " +
		"methodName=\"\" thisObject=\"\" callDepth=\"0\" >";
		String exitTag	   = "<exit calledByClass=\"\" calledByMethod=\"\" calledByObject=\"\" className=\"\" methodName=\"\" thisObject=\"\" callDepth=\"0\"></exit></entry>";

		File factFiles = new File( filenameTXT );
		if( factFiles.exists() && factFiles.canRead() && factFiles.isFile()) {
			try {
				outputStream = new PrintWriter(new FileWriter( filenameXML ));
				outputStream.println(headerXML);


				/*
				 * Read the file, line by line and count all entry tags and exit tags
				 */
				inputStream = new BufferedReader(new FileReader( filenameTXT ));
				String line;
				while ((line = inputStream.readLine()) != null) {
					outputStream.println(line);

					// count number of entry and exit tags
					while ( line.contains( "<entry" ) ) {
						entryCount++;
						line = line.replaceFirst( "<entry" , "");
					}
					while ( line.contains( "<exit"  ) ) {
						exitCount++;
						line = line.replaceFirst( "<exit" , "");
					}
					
					
				}


				if ( debug ) 
					System.out.println("Entry and Exit tags are equal? " + entryCount + "==" + exitCount );
				if ( entryCount > exitCount ){
					int diff = entryCount - exitCount;
					for (int i=0; i<diff; i++){
						outputStream.println(exitTag);		// add missing closing tags
					}
				}

				outputStream.println(exitTag);			// close single root

				// Close streams
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}

			} catch (FileNotFoundException e) {
				System.err.println("PDE: FileNotFoundException! inputStream ");
				e.printStackTrace();
			} catch (IOException e){
				System.err.println("PDE: IOException! ");
				e.printStackTrace();
			}
		}
		else {
			System.err.println("PDE: transformToXML, the provided file does not exist = " + filenameTXT);
		}
		return filenameXML;
	}



	/**
	 * @param doc2
	 */
	private void removeExitTags(Document doc2) {
		// TODO	
	}


	/**
	 * Loop through the Document and add a sequential number to each
	 * node. During the detection process we use this number to 
	 * identify the order in which the Nodes were called.
	 * 
	 * @param document contains all facts from the dynamic facts XML file
	 */
	private void addOrderOfElements(Document document) {
		NodeList nodeList = null;

		// Add order number for all entry tags
		nodeList = document.getElementsByTagName("entry");
		for (int i=0; i<nodeList.getLength(); i++){
			Element e = (Element)nodeList.item(i);
			e.setAttribute("orderNumber", i+"");
			String args = e.getAttribute("args");
			if ( args.equals("") ){
				e.setAttribute("args","<empty>");
			}
		}

		// Add order number for all exit tags
		nodeList = document.getElementsByTagName("exit");
		for (int i=0; i<nodeList.getLength(); i++){
			Element e = (Element)nodeList.item(i);
			e.setAttribute("orderNumber", i+"");
		}

	}


	/* (non-Javadoc)
	 * @see ca.yorku.cse.designpatterns.DynamicFactsProcessorInterface#getDynamicFactsDocument()
	 */
	/**
	 * @return  the dynamicFactsDocument
	 */
	public Document getDynamicFactsDocument(){
		return this.dynamicFactsDocument;
	}



	/** 
	 * Remove duplicates from the Document and write call facts without 
	 * duplicate lines to file. This reduces the Document from 190 lines to 10 
	 * in our test cases. Removes 95% of the duplicate data. Result can be 
	 * used if order of method calls does not matter. Otherwise we need
	 * the Duplicates.
	 * 
	 * @param document with all dynamic facts
	 * @param filename to which we will write the transformed dynamic facts 
	 * that will not contain any duplicate method calls
	 * 
	 * @deprecated
	 * @since 0.5 
	 */
	public void removeDuplicates(Document document, String filename) {
		// We get the nodes with the exit tag
		NodeList allNodes = document.getElementsByTagName("entry");

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

		// Loop over allNodes and put them into a LinkedHashMap
		for (int i = 0; i < allNodes.getLength(); i++) {
			NamedNodeMap nodeMap = allNodes.item(i).getAttributes();
			String caller = nodeMap.getNamedItem("calledByClass")
			.getNodeValue();
			String callee = nodeMap.getNamedItem("className").getNodeValue();
			String output = "calls " + caller + " " + callee;
			map.put(output, output);
		}

		// Loop over map
		Iterator iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String output = map.get(iterator.next());
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(
						filename, true));
				out.write(output);
				out.newLine();
				out.close();
			} catch (IOException e) {
			}
		}
	}


	/** 
	 * This method writes "calls caller callee"
	 * fact to a text file. 
	 * 
	 * @deprecated
	 * @since 0.5
	 * 
	 * @param document with all dynamic facts
	 * @param filename to which we will write the transformed dynamic facts
	 * 
	 */
	public void writeDynamicFacts(Document document, String filename) {
		// We get the nodes with the exit tag
		NodeList allNodes = document.getElementsByTagName("entry");

		// Loop over allNodes and write dynamic facts to file
		for (int i = 0; i < allNodes.getLength(); i++) {
			NamedNodeMap nodeMap = allNodes.item(i).getAttributes();
			String caller = nodeMap.getNamedItem("calledByClass")
			.getNodeValue();
			String callee = nodeMap.getNamedItem("className").getNodeValue();
			String output = "calls " + caller + " " + callee;
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
				out.write(output);
				out.newLine();
				out.close();
			} catch (IOException e) {
			}
		}
	}


	/* (non-Javadoc)
	 * @see ca.yorku.cse.designpatterns.DynamicFactsProcessorInterface#parseDocument(org.w3c.dom.Document)
	 */
	public void parseDocument(Document document) {
		/*
		 * Entry Point.
		 * We get the entries by its tags and keep them in a list.
		 */
		NodeList allEntries = document.getElementsByTagName("entry");

		if ( debug ) {
			System.out.println("Number of entries: " + allEntries.getLength() + "\n");
		}

		// Which Method called/created this Class/Object
		for (int i = 0; i < allEntries.getLength(); i++) {

			// We get one by one products and create beans for each
			NamedNodeMap nodeMap = allEntries.item(i).getAttributes();
			Node lastChild = allEntries.item(i).getLastChild();
			NamedNodeMap exit = lastChild.getAttributes();

			String parent_class = "";
			String parent_method = "";
			String parent_object = "";

			if (i != 0) {
				parent_class = allEntries.item(i).getParentNode()
				.getAttributes().getNamedItem("className")
				.getNodeValue();
				parent_method = allEntries.item(i).getParentNode()
				.getAttributes().getNamedItem("methodName")
				.getNodeValue();
				parent_object = allEntries.item(i).getParentNode()
				.getAttributes().getNamedItem("thisObject")
				.getNodeValue();

				if ( debug ) {
					System.out.println("parent | calledByClass =" + parent_class);
					System.out.println("parent | calledByMethod=" + parent_method);
					System.out.println("parent | calledByObject=" + parent_object);
				}
			} 
			else if (i == 0) {
				parent_class  = "PROGRAM_START";
				parent_method = "PROGRAM_START";
				parent_object = "PROGRAM_START";
			}

			/*
			 * Set value in "entry" Node what object is created
			 * solves objectId=Null problem in entry node
			 * see dynamic_facts_transformed_old.xml
			 * this keeps the order of the method calls right
			 */
			String obj = exit.getNamedItem("thisObject").getNodeValue();
			nodeMap.getNamedItem("thisObject").setNodeValue(obj);

			// set Value in Node by whom (class & method & object) it was called
			nodeMap.getNamedItem("calledByClass").setNodeValue(parent_class);
			nodeMap.getNamedItem("calledByMethod").setNodeValue(parent_method);
			nodeMap.getNamedItem("calledByObject").setNodeValue(parent_object);
			exit.getNamedItem("calledByClass").setNodeValue(parent_class);
			exit.getNamedItem("calledByMethod").setNodeValue(parent_method);
			exit.getNamedItem("calledByObject").setNodeValue(parent_object);


			if ( debug ) {
				for (int j = 0; j < nodeMap.getLength(); j++) {
					System.out.println("");
					System.out.println("node   | " + nodeMap.item(j));
					System.out.println("exit   | " + exit.item(j));
				}
				System.out.println("");
			}
		}
	}

	public boolean isDebug() {
		return debug;
	}


	public void setDebug(boolean debug) {
		this.debug = debug;
	}


	public String getFilename() {
		return filename;
	}


	public void setFilename(String filename) {
		this.filename = filename;
	}


	public void setDynamicFactsDocument(Document dynamicFactsDocument) {
		this.dynamicFactsDocument = dynamicFactsDocument;
	}
}
