package ca.yorku.cse.designpatterns;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * This class substitutes the role names of the design pattern
 * in the dynamic definition with the the class names 
 * from the candidate instance. This class takes the dynamic
 * definition of a design pattern as input and one candidate
 * instance object. It then exchanges the roles of the dynamic definition
 * with the real class names of the candidate instances. The 
 * dynamic definition has to be provided in XML format. The structure
 * has to match the DTD XML definition that is provided with this software.
 * 
 * @author Marcel Birkner
 * @version 0.8
 * @since 27 June, 2007
 */
public class DynamicDefinitionConverter implements DynamicDefinitionConverterInterface {
	
	/**
	 * Dynamic definition document for this design pattern.
	 */
	private Document doc;

	/**
	 * Convert the role names from the dynamic definition with the real class names
	 * from the candidate instance.
	 * 
	 * @param filename XML file that contains the dynamic definition of the design pattern
	 * @param candInstance candidate instance that we processing
	 * @param debug enable debugging (true/false)
	 */
	public DynamicDefinitionConverter(String filename, CandidateInstance candInstance ) {

		/*
		 * Read dynamic definition XML file and store in Document
		 */
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			this.doc = db.parse(new File(filename));
		} catch (Exception e) {
			System.err.println("DesignPatternDefinitionProcessor: -> " +
					"Constructor(): Cannot read from file: " + filename +
					"Please make sure that the file exists.");
			e.printStackTrace();
			System.exit(1);
		}

		int numOfRoles = candInstance.getNumOfRoles();
		System.out.println("DesignPatternDefinitionProcessor: numOfRoles: " + numOfRoles );
		String[] names = new String[ numOfRoles ];
		String[] roles = new String[ numOfRoles ];


		/*
		 * Loop over all roles and replace all "." dots with "/" a forward slash. 
		 * We have to do this to make the format of packages uniform in
		 * our XML document. Later we will match this Document with the dynamic
		 * facts that we extract with Probekit and therefore the format of 
		 * package name can not contain dots.
		 */ 
		for (int k = 0; k < numOfRoles; k++) {
			names[k] = candInstance.getNames().get(k);
			roles[k] = candInstance.getRoles().get(k).replaceAll("\\.","/");	    
			System.out.println("  roleNames[]: " + k + " " + names[k]);
			System.out.println("  roles[]:     " + k + " " + roles[k]);
		}


		NodeList dpDefList = this.doc.getElementsByTagName("entry");
		for (int l = 0; l < dpDefList.getLength(); l++) {  
			System.out.println(l + " before:");
			System.out.println(l + " 1: " + dpDefList.item(l).getAttributes().getNamedItem("className").getNodeValue());
			System.out.println(l + " 2: " + dpDefList.item(l).getAttributes().getNamedItem("calledByClass").getNodeValue());
			System.out.println(l + " 3: " + dpDefList.item(l).getAttributes().getNamedItem("args").getNodeValue());    

			/*
			 * Check className and calledByClass attributes of each node and 
			 * exchange them with the candidate instance class names of the 
			 * current LinkedList object. 
			 */ 
			String value_className      = dpDefList.item(l).getAttributes().getNamedItem("className").getNodeValue();
			String value_calledByClass  = dpDefList.item(l).getAttributes().getNamedItem("calledByClass").getNodeValue();
			String value_methodName     = dpDefList.item(l).getAttributes().getNamedItem("methodName").getNodeValue();
			String value_calledByMethod = dpDefList.item(l).getAttributes().getNamedItem("calledByMethod").getNodeValue();
			String value_thisObject     = dpDefList.item(l).getAttributes().getNamedItem("thisObject").getNodeValue();
			String value_calledByObject = dpDefList.item(l).getAttributes().getNamedItem("calledByObject").getNodeValue();
			String value_args           = dpDefList.item(l).getAttributes().getNamedItem("args").getNodeValue();

			for (int m=0; m < numOfRoles; m++){
				// Check className
				if ( value_className.equals( names[m] ) ){
					dpDefList.item(l).getAttributes().getNamedItem("className").setNodeValue( roles[m] );
				}

				// Check calledByClass
				if ( value_calledByClass.equals( names[m] ) ) {
					dpDefList.item(l).getAttributes().getNamedItem("calledByClass").setNodeValue( roles[m] );
				}

				// Check methodName
				if ( value_methodName.equals( names[m] ) ){
					dpDefList.item(l).getAttributes().getNamedItem("methodName").setNodeValue( roles[m] );
				}

				// Check calledByMethod
				if ( value_calledByMethod.equals( names[m] ) ) {
					dpDefList.item(l).getAttributes().getNamedItem("calledByMethod").setNodeValue( roles[m] );
				}	

				// Check thisObject
				if ( value_thisObject.equals( names[m] ) ){
					dpDefList.item(l).getAttributes().getNamedItem("thisObject").setNodeValue( roles[m] );
				}

				// Check calledByObject
				if ( value_calledByObject.equals( names[m] ) ) {
					dpDefList.item(l).getAttributes().getNamedItem("calledByObject").setNodeValue( roles[m] );
				}	

				// Check args
				if ( value_args.equals( names[m] ) ) {
					dpDefList.item(l).getAttributes().getNamedItem("args").setNodeValue( roles[m] );
				}
			}

			System.out.println(l + " after:");
			System.out.println(l + " 1: " + dpDefList.item(l).getAttributes().getNamedItem("className").getNodeValue());
			System.out.println(l + " 2: " + dpDefList.item(l).getAttributes().getNamedItem("calledByClass").getNodeValue());
			System.out.println(l + " 3: " + dpDefList.item(l).getAttributes().getNamedItem("args").getNodeValue());
		}
	}


	/* (non-Javadoc)
	 * @see ca.yorku.cse.designpatterns.DynamicDefinitionConverterInterface#getDesignPatternDocument()
	 */
	public Document getDesignPatternDocument(){
		return this.doc;
	}    
}   