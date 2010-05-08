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
	private static String filename = "" ;
	private static DynamicFactsProcessorListImplementation dynFacts = null;

	/**
	 * Document that stores all dynamic facts. All transformations are
	 * made on this Document Object.
	 */
	private static Document dynamicFactsDocument = null;

	private DynamicFactsProcessorListImplementation(String file) {
		this.filename = file;	
	}
		
	/**
	 * Singleton
	 */
	public static Document getDynamicFacts(String file) {
		String transformedXmlFile = file.replace(".dynamicfacts", ".transformed.xml");
		
		File xmlFactsFile        = new File (transformedXmlFile);
		
		if ( dynamicFactsDocument != null ) {
			System.out.println("DynamicFactsProcessorListImplementation -> dynamic facts exists. Return Document.");
			return dynamicFactsDocument;
		}
		else if ( xmlFactsFile.exists() && xmlFactsFile.canRead() ) {
			System.out.println("DynamicFactsProcessorListImplementation -> dynamic facts exist in transformed XML file.");
			
			/*
			 * Read transformed dynamic facts from XML file and store in Document
			 */
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				dynamicFactsDocument = db.parse( xmlFactsFile );
			} catch (Exception e) {
				System.err.println("DynamicFactsProcessorListImplementation -> " +
						"Constructor(): Cannot read from file: " + filename +
						"Please make sure that the file exists.");
				e.printStackTrace();
				System.exit(1);
			}
			System.out.println("Finished reading transformed dynamic facts from XML file.");
		} else {
			System.out.println("DynamicFactsProcessorListImplementation -> Dynamic facts do not exist yet. They are created now ... please wait.");
			dynFacts = new DynamicFactsProcessorListImplementation(file);
			dynFacts.processDynamicFacts();	
			System.out.println("DynamicFactsProcessorListImplementation -> Done processing dynamic facts file = " + file);		
		}		
		return dynamicFactsDocument;
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
			
			double t1 = System.currentTimeMillis();
			System.out.println("DynamicFactsProcessorListImplementation -> Parsing dynamic facts file=" + filename);
			this.dynamicFactsDocument = db.parse( new File(filename) );
			double t2 = System.currentTimeMillis();
			System.out.println("DynamicFactsProcessorListImplementation -> Done parsing dynamic facts time=" + (t2-t1) );
			
			System.out.println("DynamicFactsProcessorListImplementation -> transformDocument()");
			transformDocument( this.dynamicFactsDocument );
			double t3 = System.currentTimeMillis();
			System.out.println("DynamicFactsProcessorListImplementation -> Done transformDocument() time=" + (t3-t2) );
			results = true;
		} catch (Exception e) {
			System.err.println("DynamicFactsProcessorListImplementation -> Cannot parse document! " + filename + "\n" +
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
			System.out.println("DynamicFactsProcessorListImplementation -> write XML to file=" + filename);
			trans = tf.newTransformer();
			Source source = new DOMSource(this.dynamicFactsDocument);
			filename = filename.replace(".xml", ".transformed.xml");
			Result result = new StreamResult(new File(filename));
			trans.transform(source, result);
			System.out.println("DynamicFactsProcessorListImplementation -> data written to XML file.");
			results = true;
		} catch (Exception e) {
			System.err.println("DynamicFactsProcessor: Cannot store document in XML file: " + filename);
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
	 * Loop through the Document and add a sequential number to each
	 * node. During the detection process we use this number to 
	 * identify the order in which the Nodes were called.
	 * 
	 * @deprecated Jan 2008
	 * @param document contains all facts from the dynamic facts XML file
	 */
	private void addOrderOfElements(Document document) {
		NodeList nodeList = null;

		// Add order number for all entry tags
		nodeList = document.getElementsByTagName("entry");
		System.out.println("DynamicFactsProcessorListImplementation -> addOrderOfElements nodeList.length()=" + nodeList.getLength());
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


	/* 
	 * Parse the Document structure and add more information to the nodes. Add order number,
	 * start parameter for root node, 
	 * 
	 * @see ca.yorku.cse.designpatterns.DynamicFactsProcessorInterface#parseDocument(org.w3c.dom.Document)
	 */
	public void transformDocument(Document document) {
		/*
		 * Entry Point.
		 * We get the entries by their tags and keep them in a list.
		 */
		NodeList allEntries = document.getElementsByTagName("entry");

		System.out.println("DynamicFactsProcessorListImplementation -> Number of entries: " + allEntries.getLength() );
		
		/*
		 * Adjust ROOT node.
		 * Set Value in Node by whom (class & method & object) it was called
		 */
		NamedNodeMap mapRoot  = allEntries.item(0).getAttributes();
		Node lastChildRoot    = allEntries.item(0).getLastChild();
		NamedNodeMap exitRoot = lastChildRoot.getAttributes();		
		mapRoot.getNamedItem("calledByClass").setNodeValue("PROGRAM_START");
		mapRoot.getNamedItem("calledByMethod").setNodeValue("PROGRAM_START");
		mapRoot.getNamedItem("calledByObject").setNodeValue("PROGRAM_START");
		exitRoot.getNamedItem("calledByClass").setNodeValue("PROGRAM_START");
		exitRoot.getNamedItem("calledByMethod").setNodeValue("PROGRAM_START");
		exitRoot.getNamedItem("calledByObject").setNodeValue("PROGRAM_START");
		
		// Add order number
		Element eRoot = (Element)allEntries.item(0);
		eRoot.setAttribute("orderNumber", "1");
		String argsRoot = eRoot.getAttribute("args");
		if ( argsRoot.equals("") ){
			eRoot.setAttribute("args","<empty>");
		}
		
		for (int i = 1; i < allEntries.getLength(); i++) {			
			if( i%100==0 ) System.out.println("DynamicFactsProcessorListImplementation -> entry number=" + i);
			
			// Get all elements
			Element e = (Element)allEntries.item(i);
			
			// Add order number to current node
			e.setAttribute("orderNumber", i+"");
			
			// Set args attribute
			String args = e.getAttribute("args");
			if ( args.equals("") ){
				e.setAttribute("args","<empty>");
			}			
			
			/*
			 *  Get class name, method name and objects name
			 *  of parrent node.
			 */
			NamedNodeMap parentMap = e.getParentNode().getAttributes();
			String parent_class  = parentMap.getNamedItem("className").getNodeValue();
			String parent_method = parentMap.getNamedItem("methodName").getNodeValue();
			String parent_object = parentMap.getNamedItem("thisObject").getNodeValue();
			
			/* 
			 * Update current node:
			 * Set value in "entry" Node what object is created
			 * solves objectId=Null problem in entry node
			 * see dynamic_facts_transformed_old.xml
			 * this keeps the order of the method calls right
			 */
			e.setAttribute("calledByClass", parent_class);
			e.setAttribute("calledByMethod", parent_method);
			e.setAttribute("calledByObject", parent_object);
			
			String obj = e.getLastChild().getAttributes().getNamedItem("thisObject").getNodeValue();
			e.setAttribute("thisObject", obj);
		}
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
