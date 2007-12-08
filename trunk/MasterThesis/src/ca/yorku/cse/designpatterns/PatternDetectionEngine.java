package ca.yorku.cse.designpatterns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Instance of the PatternDetection Engine
 * detects software <b>Design Patterns</b>, given static 
 * and dynamic facts.
 * 
 * @author Marcel Birkner
 * @version 0.9
 * @since 17 November, 2007
 * 
 * TODO:
 * -pinot results (jdraw)
 * -evaluate my results
 * -new timing data
 */
public class PatternDetectionEngine 
{
	// Static variables
	private static boolean print_datastructure = false;
	private static boolean rank_results        = false;
	private static boolean print_stats         = false;
	private static boolean create_report       = false;
	private static boolean debug               = false;
	private static boolean enable_timing       = true;
	private static boolean print_time	       = false;
	private static boolean print_results       = false;
	private static boolean print_on_cmdline    = false;
	private static boolean redirectSystemOut   = false;

	private static LinkedList<String> time = new LinkedList<String>();
	
	private static String candidateInstancesFileName = null;
	private static String dynamicFactsFileName       = null;
	private static String dynamicDefinitionFileName  = null;

	private static double threshold            = 0.8;				// default threshold is 80%
	private static LinkedList<FactFiles> input = null;				// stores the file names of the input file

	// FILENAMES: default
	private static String config_file               = "conf/run.properties";
	private static String redirect_file;			// "redirect.txt";				// default filename for redirect
	private static String report_file;				// "report.txt";				// default filename for report
	private static String software_file;			// "software.xml";
	private static String designpattern_file;		// "designpatterns.xml";
	private static String input_file;				// "pde.input";
	private static String exception_file;			// "exception.txt";
	private static String results_file;				// "results.xml";

	private static LinkedList<CandidateInstance> candInstancesList = null;
	private static NodeList dynamicDefinitionList = null;
	
	// Variables for results report
	private static String[][] res  = new String[23][23];
	private static String[][] res2 = new String[23][23];
	private static int pointer_x  = 0;
	private static int pointer_y  = 0;


	/**
	 * Main program. Reads passed parameters and configures program to process static
	 * and dynamic fact files. Then it calls the run method to start the detection
	 * process.
	 * 
	 * @param args input parameters for the main program
	 */
	public static void main(String[] args) 
	{
	
		/*
		 * Read properties file 
		 * conf/run.properties
		 */
	    Properties prop = new Properties();
	    try {
	    	prop.load( new FileInputStream( config_file ) );     
	    	print("Load properties.");
	    	redirect_file       = prop.getProperty("output.redirect.txt.file");			// "redirect.txt"
	    	report_file         = prop.getProperty("output.report.txt.file");			// "report.txt"
	    	software_file      	= prop.getProperty("input.software.xml.file");			// "software.xml"
	    	designpattern_file 	= prop.getProperty("input.design.patterns.xml.file");	// "designpatterns.xml"
	    	input_file 	      	= prop.getProperty("input.pde.txt.file");				// "pde.input"
	    	exception_file 	    = prop.getProperty("output.exception.txt.file");		// "exception.txt"
	    	results_file        = prop.getProperty("output.results.xml.file");			// "results.xml"
	    } catch (IOException e) {
	    	print("Could not find properties file. Please check your conf/run.properties file.");
	    	System.exit(1);
	    }

		
		// Create Instance of PatternDetectionEngine
		PatternDetectionEngine pde = new PatternDetectionEngine();

		if( args.length == 0 ) {
			pde.usage(true);
			System.exit(1);
		}


		/*
		 * Check arguments for static analysis
		 */
		for(int i=0; i<args.length; i++){
			if ( args[i].equals("-static") ){
				/*
				 * Read dynamic definition XML file and store in Document
				 * Dynamic definition document for this design pattern.
				 */
				Document software_doc = null;
				try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					software_doc = db.parse(new File(software_file));
				} catch (Exception e) {
					print("PatternDetectionEngine: -> " +
							"Constructor(): Cannot read from file: " + software_file +
					"Please make sure that the file exists.");
					e.printStackTrace();
					System.exit(1);
				}

				Document designpattern_doc = null;
				try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					designpattern_doc = db.parse(new File(designpattern_file));
				} catch (Exception e) {
					System.out.println("designpattern_doc: -> " +
							"Constructor(): Cannot read from file: " + designpattern_file +
					"Please make sure that the file exists.");
					e.printStackTrace();
					System.exit(1);
				}


				String shell    = "";
				String javex    = "";
				String grok     = "";
				String ql       = "";
				String canInDir = "";

				File pdeDel = new File(input_file);
				boolean isDeleted = pdeDel.delete();
				print("pde_input_filename is deleted? " + isDeleted);

				NodeList properties = software_doc.getElementsByTagName("properties");
				for (int l = 0; l < properties.getLength(); l++) {  
					shell = properties.item(l).getAttributes().getNamedItem("shellScript").getNodeValue();
					javex = properties.item(l).getAttributes().getNamedItem("javex").getNodeValue();
					grok  = properties.item(l).getAttributes().getNamedItem("grok").getNodeValue();
					ql    = properties.item(l).getAttributes().getNamedItem("ql").getNodeValue();
					canInDir  = properties.item(l).getAttributes().getNamedItem("candidateInstancesDirectory").getNodeValue();

					if ( !canInDir.endsWith("/")  ) {
						canInDir = canInDir + "/";
					}	            

					print("1: " + shell);
					print("2: " + javex);
					print("3: " + grok);
					print("4: " + ql);
				}




				NodeList software = software_doc.getElementsByTagName("software");
				for (int j = 0; j < software.getLength(); j++) { 
					String nameSrc      = software.item(j).getAttributes().getNamedItem("name").getNodeValue();
					String directory    = software.item(j).getAttributes().getNamedItem("directory").getNodeValue();
					String mainClass    = software.item(j).getAttributes().getNamedItem("mainClass").getNodeValue();
					String dynFactsFile = software.item(j).getAttributes().getNamedItem("dynamicFactsFile").getNodeValue();

					print("\n 1: " + nameSrc );
					print(  " 2: " + directory);
					print(  " 3: " + mainClass );
					print(  " 4: " + dynFactsFile );

					String nameDP        = "";
					String ql_script     = "";
					String pattern_roles = "";	     
					String dynamicDefinitionFile = "";

					NodeList designpattern = designpattern_doc.getElementsByTagName("designpattern");
					for (int k = 0; k < designpattern.getLength(); k++) { 
						nameDP        = designpattern.item(k).getAttributes().getNamedItem("name").getNodeValue();
						ql_script     = designpattern.item(k).getAttributes().getNamedItem("ql_script").getNodeValue();
						pattern_roles = designpattern.item(k).getAttributes().getNamedItem("pattern_roles").getNodeValue();
						dynamicDefinitionFile = designpattern.item(k).getAttributes().getNamedItem("dynamicDefinitionFile").getNodeValue();
						print("\n 1: " + nameDP );
						print(  " 2: " + ql_script);
						print(  " 3: " + pattern_roles );

						String command = shell+" "+javex+" "+grok+" "+ql+" "+directory+" "+ql_script+" "+canInDir ; 
						print(command);

						String staticFactsOutputFile = canInDir + nameSrc + "." + nameDP + ".out";
						String input_filename  = "ql.out";

						StaticAnalysis st = new StaticAnalysis(command, "static_output.txt");
						st.runStaticAnalysis();

						try {
							print("BufferedWriter out");
							BufferedWriter out = new BufferedWriter(new FileWriter( staticFactsOutputFile ));	        	    
							out.write(pattern_roles + "\n");
							out.flush();

							//Create a buffer reader for the passed file
							print("BufferedWriter in");
							BufferedReader in = new BufferedReader(new FileReader( input_filename ));
							String line = "";
							while ((line = in.readLine()) != null) {
								out.write(line + "\n");
								out.flush();
							}	                
							out.close();	        	    

							print("BufferedWriter pdeIn");
							boolean append = true;
							try {
								BufferedWriter pdeIn = new BufferedWriter(new FileWriter( input_file, append ));
								pdeIn.write(staticFactsOutputFile+" "+dynFactsFile+" "+dynamicDefinitionFile+" \n");
								pdeIn.flush();
								pdeIn.close();
							} catch (IOException e1) {
								print("problem reading pde_input_filename: " + input_file);
								e1.printStackTrace();
							}
						} catch (IOException e) {
							print("EXCEPTION: Java IO");
							BufferedWriter exc;
							try {
								exc = new BufferedWriter(new FileWriter( exception_file, true ));
								exc.write("Exception: " + command + "\n");
								exc.flush();
							} catch (IOException e1) {
								print("Exception writting to exception.txt");
								e1.printStackTrace();
							}	
						}
					}
				}
			} 
			else if (args[i].equals("-usage") || args[i].equals("-help") || args[i].equals("-h") || args[i].equals("--h") || args[i].equals("--help")) {
				pde.usage(true);
				System.exit(1);
			}
		}


		/*
		 * Check arguments that are passed to the main method
		 */
		for(int i=0; i<args.length; i++){
			if ( args[i].equals("-ci") ){
				candidateInstancesFileName = args[++i];
				print("Input parameter for -ci " + candidateInstancesFileName);
			}
			else if ( args[i].equals("-df") ){
				dynamicFactsFileName = args[++i];
				print("Input parameter for -df " + dynamicFactsFileName);
			}
			else if ( args[i].equals("-dd") ){
				dynamicDefinitionFileName = args[++i];
				print("Input parameter for -dd " + dynamicDefinitionFileName);
			}
			else if ( args[i].equals("-input") ) {
				input_file = args[++i];
				print("Input parameter for -input " + input_file );
			} 
			else if ( args[i].equals("-dynamic") ) {
				// ALL DEFAULT VALUES
				print("Input parameter -dynamic. Run with default values. ");
				threshold     = Double.parseDouble("0.80");
				print_stats   = true;
				create_report = true;
				print_results = true;
				print("Default values: "
						+ " inputFileName " + input_file + "\n"
						+ " threshold     " + threshold + "\n"
						+ " print_stats   " + print_stats + "\n"
						+ " create_report " + create_report + "\n"
						+ " print_results " + print_results);
			}
			else if ( args[i].equals("-redirect") ){ 
				redirectSystemOut = true;
				redirect_file = args[++i];
				print("Input parameter for -redirect true and output file name " + redirect_file);
			}	
			else if ( args[i].equals("-threshold") ){
				try {
					threshold = Double.parseDouble(args[++i]);
					print("Input parameter for -threshold " + threshold);
					if ( threshold > 1.0 || threshold < 0.0 ){
						throw new Exception();
					}			
				} catch (Exception e) {
					print("The threshold has to be a double between 0.0 and 1.0 (default value is 0.8 = 80%) ");
					System.exit(1);
				}
			}
			else if ( args[i].equals("-print_statistics") || args[i].equals("-ps") ){
				print_stats = true;
				print("Input parameter for -print_statistics || -ps  true");
			}
			else if ( args[i].equals("-create_report") ){
				create_report = true;
				print("Input parameter for -create_report true. Writting report to file: " + report_file);
			}	    
			else if ( args[i].equals("-print_datastructure") || args[i].equals("-pd") ){
				print_datastructure = true;
				print("Input parameter for -print_datastructure || -pd true");
			}	
			else if ( args[i].equals("-print_results") || args[i].equals("-pr") ){
				print_results = true;
				print("Input parameter for -print_results || -pr true");
			}	
			else if ( args[i].equals("-example")){
				candidateInstancesFileName = "examples/example.AbstractFactory.ql.out.instances";
				dynamicFactsFileName       = "examples/example.AbstractFactory.RunPattern.txt";
				dynamicDefinitionFileName  = "examples/example.AbstractFactory.xml";
				print_results = true;
				print("Example for PDE. Current setup: " +
						"\ncandidateInstancesFileName =" + candidateInstancesFileName +
						"\ndynamicFactsFileName       =" + dynamicFactsFileName +
						"\ndynamicDefinitionFileName  =" + dynamicDefinitionFileName);

			}	
			else if ( args[i].equals("-debug") ){
				debug = true;
				print("Input parameter for -debug true");
			}	
			else if ( args[i].equals("-rankResults") ){
				rank_results = true;
				print("Input parameter for -rankResults true");
			}	
			else if ( args[i].equals("-print_time") ){
				print_time = true;
				print("Input parameter for -print_time true");
			}
			else if ( args[i].equals("-static") ){
				print("Input parameter for -static true");
			}
			else if ( args[i].equals("-summary") ){
				print("Input parameter for -summary true");
				pde.summary( results_file );
				System.exit(1);
			}
			else if ( args[i].equals("-fullSummary") ){
				print("Input parameter for fullSummary true");
				pde.fullSummary( results_file );
				System.exit(1);
			}
			else if (args[i].equals("-help") || args[i].equals("-h") || args[i].equals("--h") || args[i].equals("--help")) {
				pde.usage(true);
				System.exit(1);
			}
			else {
				System.err.println("Wrong argument passed to the program: " + args[i]);
				pde.usage(true);
				System.exit(1);
			}
		}


		// Redirect System Output Stream 
		if ( redirectSystemOut ) { 
			try {
				System.setOut(new PrintStream( new FileOutputStream( redirect_file )));
			} catch ( FileNotFoundException e ) {
				e.printStackTrace();
			}
		}


		// Write results to XML file
		String XMLheader   = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
			+ "<!DOCTYPE entry SYSTEM \"results.dtd\">" 
			+ "<results>";
		String XMLfooter   = "</results>";
		PrintWriter resultsStream = null;
		try {
			File x = new File ( results_file );
			if ( x.exists() ) {
				print("Results file deleted: " + results_file );
				x.delete();
			}
			resultsStream  = new PrintWriter(new FileWriter( results_file, true ));
			resultsStream.println( XMLheader );
		} catch (IOException e) {
			System.err.println("Exception: Can not write results to file: " + results_file );
			e.printStackTrace();			    
		}



		/*
		 * Process fact files that are passed as arguments
		 *    -ci -df -dd
		 */
		if ( candidateInstancesFileName != null && dynamicFactsFileName != null && dynamicDefinitionFileName != null ) {

			String ci = candidateInstancesFileName;
			String dy = dynamicFactsFileName;
			String dp = dynamicDefinitionFileName;

			/*
			 * Run Pattern Detection Engine with the given input files
			 */
			if ( print_results ) {
				System.out.println("\n################################################################################################");
				System.out.println("Processing the following files ...");
				System.out.println(ci + "\n" + dy + "\n" + dp + "\n");
			}


			/*
			 * Run Detection Engine and verify/detect Design Patterns from the fact files
			 */
			pde.run2(ci, dy, dp, resultsStream);
			System.exit(1);

		} else if ( candidateInstancesFileName != null || dynamicFactsFileName != null || dynamicDefinitionFileName != null) {
			print("Please provide enough input parameters to start the Pattern Detection Engine." +
					"\ncandidateInstancesFileName = " + candidateInstancesFileName +
					"\ndynamicFactsFileName " + dynamicFactsFileName +
					"\ndynamicDefinitionFileName " + dynamicDefinitionFileName);
			System.exit(1);
		}



		/*
		 * Process -input file
		 */
		if ( input_file != null ){

			input = pde.readFactFiles( input_file );

			for(int j=0; j<input.size(); j++){

				/*
				 * Processing all files that are specified in the input file
				 */    	
				String ci = input.get(j).getCandidateInstanceFile();
				String dy = input.get(j).getDynamicFactsFile();
				String dp = input.get(j).getDynamicDefinitionFile();

				/*
				 * Run Pattern Detection Engine with the given input files
				 */
				if ( debug ) {
					System.out.println("\n################################################################################################");
					System.out.println("Processing the following files ...");
					System.out.println(ci + "\n" + dy + "\n" + dp + "\n");
				}

				/*
				 * Run Detection Engine and verify/detect Design Patterns from the fact files
				 */
				long t1=0; long t2=0;
				if (enable_timing){
					t1 =  System.currentTimeMillis();
				}
				pde.run2(ci, dy, dp, resultsStream);
				if (enable_timing){
					t2 =  System.currentTimeMillis();
					time.add( (t2-t1) + "\t pde.run2( "+ci+" )");
				}
			}
			if (enable_timing){
				print("Time:");
				for (int i = 0; i < time.size(); i++) {
					print( time.get(i) );
				}
			}
			if ( print_results ) { 
				System.out.println("################################################################################################");
				System.out.println("# PDE: # of examples, input.size()=" + input.size() );
			}	    
		}
		resultsStream.println(XMLfooter);
		resultsStream.close();



		/*
		 * Print all stats for the 23 design patterns
		 */
//		if ( create_report ) {
//
//			// Redirect System Output Stream for report
//			try {
//				System.setOut(new PrintStream( new FileOutputStream( report_file )));
//			} catch ( FileNotFoundException e ) {
//				print("FileNotFoundException: Redirect System Out failed.");
//				e.printStackTrace();
//			}
//
//			for (int i=0; i<res.length; i++) {
//				for (int j=0; j<res[i].length; j++){
//					System.out.print(res[i][j] + " ");
//				}
//				System.out.println("");
//			}
//
//
//			for (int i=0; i<res2.length; i++) {
//				for (int j=0; j<res2[i].length; j++){
//					System.out.print(res2[i][j] + " ");
//				}
//				System.out.println("");
//			}	
//		}
	}


	
	
	/**
	 * This method formats the results of the static and 
	 * dynamic analysis by parsing the XML results file
	 * and grouping the results by software. This method
	 * is more detailed than summary()
	 * 
	 * @param XMLfile with all result facts
	 */
	private void fullSummary(String results_file) {
		
		File xx = new File ( results_file );
		if ( !xx.exists() ) {
			print("The results file does not exist. " +
					"Please run the static and dynamic analysis first. " +
					"The following file could not found: " + results_file);
		}
		
		DocumentBuilderFactory dbfResults = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbResults;
		Document docResults = null;

		DocumentBuilderFactory dbfSoftware = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbSoftware;
		Document docSoftware = null;
		try {
			dbResults  = dbfResults.newDocumentBuilder();
			docResults = dbResults.parse( new File( results_file ) );

			dbSoftware  = dbfSoftware.newDocumentBuilder();
			docSoftware = dbSoftware.parse( new File( software_file ) );

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		NodeList allResults = docResults.getElementsByTagName("result");
		for (int j=0; j < allResults.getLength(); j++ ) {
			Node node1 = allResults.item(j);
			String name          = node1.getAttributes().getNamedItem("name").getNodeValue();
			String sourceCode    = node1.getAttributes().getNamedItem("sourceCode").getNodeValue();
			String designPattern = node1.getAttributes().getNamedItem("designPattern").getNodeValue();
			String numberOfHits  = node1.getAttributes().getNamedItem("numberOfHits").getNodeValue();
			
			print("####################################################################\n");
			print("-> name:          " + name);
			print("   sourceCode:    " + sourceCode);
			print("   designPattern: " + designPattern);
			print("   numberOfHits:  " + numberOfHits + "\n");
			
			NodeList children = allResults.item(j).getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node node2 = children.item(i);
				if ( node2.getNodeName().equals("candidateInstance") ) {
					String percentage = node2.getAttributes().getNamedItem("percentage").getNodeValue();
					String threshold  = node2.getAttributes().getNamedItem("threshold").getNodeValue();
					String roles      = node2.getAttributes().getNamedItem("roles").getNodeValue();
					print("   -> candidateInstance");
					print("      percentage:  " + percentage);
					print("      threshold:   " + threshold);
					print("      roles:       " + roles);
					print("");
				}
			}
		}	
	}

	
	/**
	 * This method formats the results of the static and 
	 * dynamic analysis by parsing the XML results file
	 * and grouping the results by software
	 * 
	 * @param XMLfile
	 */
	@SuppressWarnings("unchecked")
	private void summary(String resultsXMLfile) {
		
		File xx = new File ( resultsXMLfile );
		if ( !xx.exists() ) {
			print("The results file does not exist. " +
					"Please run the static and dynamic analysis first. " +
					"The following file could not found: " + resultsXMLfile);
		}
		
		
		DocumentBuilderFactory dbfResults = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbResults;
		Document docResults = null;

		DocumentBuilderFactory dbfSoftware = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbSoftware;
		Document docSoftware = null;
		try {
			dbResults  = dbfResults.newDocumentBuilder();
			docResults = dbResults.parse( new File( resultsXMLfile ) );

			dbSoftware  = dbfSoftware.newDocumentBuilder();
			docSoftware = dbSoftware.parse( new File( software_file ) );

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		NodeList allResults = docResults.getElementsByTagName("result");
		NodeList allSoftware = docSoftware.getElementsByTagName("software");
		LinkedList[] groupedResults = new LinkedList[ allSoftware.getLength() ];

		// loop over all results and group them in a linked list by the attribute software
		for (int i=0; i < allSoftware.getLength(); i++ ) {
			Node softwareNode = allSoftware.item(i).getAttributes().getNamedItem("name");
			String softwareName = softwareNode.getNodeValue();
			groupedResults[i] = new LinkedList<Node>();
			LinkedList<Node> list = groupedResults[i];
			for (int j=0; j < allResults.getLength(); j++ ) {
				Node sourceCodeNode = allResults.item(j).getAttributes().getNamedItem("sourceCode");
				Node resNode = allResults.item(j);
				String sourceCodeName = sourceCodeNode.getNodeValue();
				if ( softwareName.equalsIgnoreCase(sourceCodeName) ) {
					list.add( resNode );
				}
			}
		}

		print("\nPrint Summary: ");
		print("Verified/Total \t Source Code \t\t Design Pattern \n");
		for (int i = 0; i < groupedResults.length; i++) {
			LinkedList<Node> listX = groupedResults[i];
			for (int j = 0; j < listX.size(); j++) {		
				String designP    = listX.get(j).getAttributes().getNamedItem("designPattern").getNodeValue();
				String numHitsX   = listX.get(j).getAttributes().getNamedItem("numberOfHits").getNodeValue();
				String sourceCode = listX.get(j).getAttributes().getNamedItem("sourceCode").getNodeValue();
				print(numHitsX + " \t\t " + sourceCode + " \t " + designP );
			}
			print("");
		}
	}

	
	public void run2(String candidateInstancesFileName, String dynamicFactsFileName, String dynamicDefinitionFileName, PrintWriter resultsStream ) {
		long t1=0;
		long t2=0;
		long t3=0;
		long t4=0, t41=0;
		long t5=0, t51=0;
		long t6=0, t61=0;
		long t7=0, t71=0;
		long t8=0;
		
		String codeExample = candidateInstancesFileName;
		String patternName = null;   	    
		if ( candidateInstancesFileName.contains("/") && 
				candidateInstancesFileName.contains(".out") )
		{
			int index0 = candidateInstancesFileName.indexOf("/");
			int index1 = candidateInstancesFileName.indexOf(".");
			int index2 = candidateInstancesFileName.indexOf(".out");
			codeExample = candidateInstancesFileName.substring(index0+1, index1);
			patternName = candidateInstancesFileName.substring(index1+1, index2);
		} else {    	    
			patternName = candidateInstancesFileName; 
		}
		print(   "Analyzing software code:     " + codeExample + 
			   "\nPattern we want to detect:   " + patternName); 
		
		
		/**
		 * get LinkedList with Candidate Instances
		 */
		if (enable_timing){
			t1 = System.currentTimeMillis();
		}
		CandidateInstanceListInterface candInstances = new CandidateInstanceList(candidateInstancesFileName, debug);
		candInstancesList = candInstances.getCandidateInstancesList();
		if (enable_timing){
			t2 =  System.currentTimeMillis();
			time.add( (t2-t1) + "\t CandidateInstanceList");
		}		

		if (enable_timing){
			t1 = System.currentTimeMillis();
		}
		print("\nCreate dynamic facts tree:   " + dynamicFactsFileName);
		DynamicFactsProcessorTreeImplementation dynFacts = new DynamicFactsProcessorTreeImplementation(dynamicFactsFileName, false, enable_timing);
		if (enable_timing){
			t2 =  System.currentTimeMillis();
			time.add( (t2-t1) + "\t DynamicFactsProcessorImplementation");
		}
		
		NodeList dpDefList = null;
		print("candInstancesList.size() " + candInstancesList.size());
		for (int i=0; i < candInstancesList.size(); i++)
		{
			if ( (i % 100 == 0 ) ) print("candInstancesList loop i=" + i);
			
			/** 
			 * Loop over all Candidate instances.
			 * Convert dynamic definition and candidate instance.
			 * Store all matching nodes in storeMatchedFacts data structure.
			 */
			if (enable_timing){
				t4 =  System.currentTimeMillis();
			}
			DynamicDefinitionConverter dpDef = new DynamicDefinitionConverter(dynamicDefinitionFileName, candInstancesList.get(i), false);
			Document dpDefDoc = dpDef.getDesignPatternDocument();
			dpDefList = dpDefDoc.getElementsByTagName("entry");
			if (enable_timing){
				t5 =  System.currentTimeMillis();
				t41 = t41 + (t5 - t4);
			}
			LinkedList[] storeMatchedRoles = new LinkedList[ dpDefList.getLength() ];
	
			
			for (int j = 0; j < dpDefList.getLength(); j++) {
				LinkedList<Node> list = dynFacts.firstLevel( dpDefList.item(j) );
				storeMatchedRoles[j]  = list;
				list = null;
			}
			if (enable_timing){
				t6 =  System.currentTimeMillis();
				t51 = t51 + (t6 - t5);
				
			}
			candInstancesList.get(i).setMatchedFactsDatastructure( storeMatchedRoles );

			
			/**
			 * Validate temporal restriction
			 */
			Validator validator = new Validator();
			candInstancesList = validator.validateTemporalRestriction(candInstancesList, dpDefList, dynamicFactsFileName);
			if (enable_timing){
				t7 =  System.currentTimeMillis();
				t61 = t61 + (t7 - t6);
			}
			/**
			 * Validate objects restriction
			 */
			candInstancesList = validator.validateObjects(candInstancesList, dpDefList);
			if (enable_timing){
				t8 =  System.currentTimeMillis();
				t71 = t71 + (t8 - t7);
			}		
		}
		
		
		
		
		if (enable_timing){
			t3 =  System.currentTimeMillis();
			time.add( (t3-t2) + "\t candInstancesList.size() loop");
			time.add( t41 + "\t DynamicDefinitionConverter loop");
			time.add( t51 + "\t dpDefList.getLength() loop");
			time.add( t61 + "\t validateTemporalRestriction loop");
			time.add( t71 + "\t validateObjects loop");
		}

		

		
		int count_isPattern    = 0;
		int count_isNotPattern = 0;
		double global_quantifier_sum   = 0;
		double global_quantifier_match = 0; 

		for (int i=0; i<candInstancesList.size(); i++) {

			/*
			 * Changed Version of program output:
			 * Output is in percent, relative to the quantifier
			 */
			LinkedList[] facts_list = candInstancesList.get(i).getMatchedFactsDatastructure();
			double number_of_definitions = facts_list.length;
			double number_of_definition_matches = 0;

			for (int k=0; k<facts_list.length; k++) {
				double quantifier;
				try {
					String q = dpDefList.item(k).getAttributes().getNamedItem("quantifier").getNodeValue();
					quantifier = Double.parseDouble(q);
				} catch (Exception e) {
					quantifier = (double)1;
					print("Please provide a quantifier in XML definition = " + dynamicDefinitionFileName);
				} 
				if ( !facts_list[k].isEmpty() && !facts_list[k].equals(null)){

					global_quantifier_sum = global_quantifier_sum + quantifier;
					if ( facts_list[k].size() > 0 ) {
						number_of_definition_matches++;
						global_quantifier_match = global_quantifier_match + quantifier;
					}
				}
			}

			if ( global_quantifier_sum == 0 ) global_quantifier_sum=1;

			double quantify = global_quantifier_match / global_quantifier_sum;
			double number = (number_of_definition_matches/number_of_definitions) * quantify;
			NumberFormat nf = NumberFormat.getPercentInstance();
			if ( debug ) 
				print("number > threshold "  + number + "  "+  threshold);
			if ( number >= threshold ) {	
				candInstancesList.get(i).setIsPattern(true);
				candInstancesList.get(i).setPercentage(number);
			} else {
				candInstancesList.get(i).setIsPattern(false);
				candInstancesList.get(i).setPercentage(number);
			}

			if ( print_results ) { 
				if(candInstancesList.get(i).isPattern()){
					print("Candidate instance is a pattern: \t\t" + nf.format(number) + "   \t\t threshold=" + nf.format(threshold));
				}
			}
			//print("");

			/**
			 * isPattern is set when ALL definitions are matched within the threshold are matched
			 */ 
			if ( candInstancesList.get(i).isPattern() ) {
				count_isPattern++;
			} else {
				count_isNotPattern++;
			}    	    
		}   	    


		/*
		 * Since we have the percentage for all possible candidate
		 * instances, we can rank the results in the candInstancesList
		 */
		if ( rank_results ) rankResults( candInstancesList );
		NumberFormat nf = NumberFormat.getPercentInstance();
		if ( print_on_cmdline )
			print("Number of positive candidate instances after the dynamic analysis: " + count_isPattern + " out of " + candInstancesList.size() + " ( threshold = " + nf.format(threshold) + " )" );
		if ( count_isPattern > 0){
			if ( print_on_cmdline )
				print("Here is a ranked list of all candidate instances with the corresponding class names {and pattern roles}: " + 
						candInstancesList.getFirst().getNames() + "\n");		
			for (int i=0;i<candInstancesList.size();i++){
				if (candInstancesList.get(i).isPattern() )
					if ( print_on_cmdline )
						print("  " + i + "\t " + nf.format( candInstancesList.get(i).getPercentage() ) +
								"\t " + candInstancesList.get(i).getRoles());
			}
		} 


		/*
		 * Store results in XML file
		 */ 
		resultsStream.println("<result name=\"" + candidateInstancesFileName 
				+ "\" designPattern=\"" + patternName 
				+ "\" sourceCode=\"" + codeExample
				+ "\" numberOfHits=\"" + count_isPattern 
				+ "/" + candInstancesList.size() + "\" >");

		for (int i=0; i<candInstancesList.size(); i++ ){		    
			if( candInstancesList.get(i).isPattern() ){
				resultsStream.println("<candidateInstance percentage=\"" 
						+ nf.format( candInstancesList.get(i).getPercentage() ) 
						+ "\" threshold=\"" + threshold   
						+ "\" roles=\"" + candInstancesList.get(i).getRoles() + "\" />" );
			}
		}
		resultsStream.println("</result>");	    

//		if ( print_on_cmdline )
//			print("\n######################################################################################################## \n");
//
//
//		// Print out summary of isPattern/isNotPattern
//		if( pointer_y < res.length ){
//			if ( pointer_y == 0) {
//				res[pointer_x][pointer_y]  = codeExample;
//				res2[pointer_x][pointer_y] = codeExample;
//				pointer_y++;
//			}
//			res[pointer_x][pointer_y]  = count_isPattern + "";
//			res2[pointer_x][pointer_y] = count_isNotPattern + "";
//			pointer_y++;
//		} else {
//			pointer_x++;
//			pointer_y=0;
//			if ( pointer_y == 0) {
//				res[pointer_x][pointer_y]  = codeExample;
//				res2[pointer_x][pointer_y] = codeExample;
//				pointer_y++;
//			}
//			res[pointer_x][pointer_y]  = count_isPattern + "";
//			res2[pointer_x][pointer_y] = count_isNotPattern + "";
//			pointer_y++;
//		}
		
		
	}
	

	/**
	 * Processes the input files and starts the detection of the 
	 * design pattern given the input parameters.
	 * 
	 * @param ci File that contains the candidate instances
	 * @param dy File that contains the dynamic facts extracted with Probekit
	 * @param dd File that contains the dynamic definition of the design pattern
	 * @param resultsStream for results output
	 */
	public void run(String ci, String dy, String dd, PrintWriter resultsStream) {
		if ( print_time ) 
			print("run ->                            " + System.currentTimeMillis());

		// Set filenames
		candidateInstancesFileName   = ci;
		dynamicFactsFileName         = dy;
		dynamicDefinitionFileName    = dd;


		/* 
		 * CandidateInstanceProcessor
		 * -> need access to LinkedList with all objects
		 */
		if ( print_time ) 
			print("run -> CandidateInstanceComposite " + System.currentTimeMillis());	
		CandidateInstanceListInterface candInstances = new CandidateInstanceList(candidateInstancesFileName, debug);
		candInstancesList = candInstances.getCandidateInstancesList();	
		if ( debug && !candInstancesList.isEmpty() ) 
			print("candInstancesList size " + candInstancesList.size());


		/*
		 * DynamicFactsProcessor
		 * -> need access to complete document that
		 *    represents the dynamic facts of the software
		 */  
		if ( print_time ) 
			print("run -> DynamicFactsProcessor      " + System.currentTimeMillis());	
		
		Document dynFactsDoc = DynamicFactsProcessorListImplementation.getDynamicFacts(dynamicFactsFileName, false);
		NodeList dynFactsList = dynFactsDoc.getElementsByTagName("entry");
		if( debug ) 
			print("dynFactsList Length: " + dynFactsList.getLength());


		/*
		 * Validates the matches found
		 */
		ValidatorInterface validator = new Validator();
		if ( print_time ) 
			print("run -> validator.validate         " + System.currentTimeMillis());
		validator.validate(dynamicDefinitionFileName, dynFactsList, candInstancesList, debug, print_datastructure);
		dynamicDefinitionList = validator.getDynamicDefinitionList();
		candInstancesList = validator.getCandidateInstancesList();


		/*
		 * Validate the matchedFacts with the design pattern definition
		 * - check "thisObject"
		 * - check "calledByObject"
		 */
		if ( print_time ) 
			print("run -> validator.validateObjects  " + System.currentTimeMillis());
		validator.validateObjects(candInstancesList, dynamicDefinitionList);
		candInstancesList = validator.getCandidateInstancesList();


		if ( print_time ) 
			print("run -> printResults               " + System.currentTimeMillis());
		/*
		 * Print results
		 */
		if ( debug ) {
			for (int i=0; i<candInstancesList.size(); i++){
				print(candidateInstancesFileName + " is Pattern? " + candInstancesList.get(i).isPattern() );
			}  
			printResults(candInstancesList, dynamicDefinitionList, dynamicDefinitionFileName);
		}


		/*
		 * Print Candidate Instances datastructure
		 */
		if ( print_datastructure ) {
			print_datastructure(candInstancesList);
		}


		/*
		 * Print statistics and results
		 */    	
		if ( print_stats || print_results || create_report ) { 
			String codeExample = candidateInstancesFileName;
			String patternName = null;   	    
			if ( candidateInstancesFileName.contains("/") && 
					candidateInstancesFileName.contains(".out") )
			{
				int index0 = candidateInstancesFileName.indexOf("/");
				int index1 = candidateInstancesFileName.indexOf(".");
				int index2 = candidateInstancesFileName.indexOf(".out");
				codeExample = candidateInstancesFileName.substring(index0+1, index1);
				patternName = candidateInstancesFileName.substring(index1+1, index2);
			} else {    	    
				patternName = candidateInstancesFileName; 
			}


			print(  "Analyzed software code:      " + codeExample + 
					"\nPattern we want to detect:   " + patternName); 
			int count_isPattern    = 0;
			int count_isNotPattern = 0;
			double global_quantifier_sum   = 0;
			double global_quantifier_match = 0; 

			for (int i=0; i<candInstancesList.size(); i++) {

				/*
				 * Changed Version of program output:
				 * Output is in percent, relative to the quantifier
				 */
				LinkedList[] facts_list = candInstancesList.get(i).getMatchedFactsDatastructure();
				double number_of_definitions = facts_list.length;
				double number_of_definition_matches = 0;

				for (int k=0; k<facts_list.length; k++) {
					double quantifier;
					try {
						String q = dynamicDefinitionList.item(k).getAttributes().getNamedItem("quantifier").getNodeValue();
						quantifier = Double.parseDouble(q);
					} catch (Exception e) {
						quantifier = (double)1;
						print("Please provide a quantifier in XML definition = " + dynamicDefinitionFileName);
					} 
					if ( !facts_list[k].isEmpty() && !facts_list[k].equals(null)){

						global_quantifier_sum = global_quantifier_sum + quantifier;
						if ( facts_list[k].size() > 0 ) {
							number_of_definition_matches++;
							global_quantifier_match = global_quantifier_match + quantifier;
						}
					}
				}

				if ( global_quantifier_sum == 0 ) global_quantifier_sum=1;

				double quantify = global_quantifier_match / global_quantifier_sum;
				double number = (number_of_definition_matches/number_of_definitions) * quantify;
				NumberFormat nf = NumberFormat.getPercentInstance();
				if ( debug ) 
					print("number > threshold "  + number + "  "+  threshold);
				if ( number >= threshold ) {	
					candInstancesList.get(i).setIsPattern(true);
					candInstancesList.get(i).setPercentage(number);
				} else {
					candInstancesList.get(i).setIsPattern(false);
					candInstancesList.get(i).setPercentage(number);
				}

				if ( print_results ) { 
					if(candInstancesList.get(i).isPattern()){
						print("Candidate instance is a pattern: \t\t" + nf.format(number) + "   \t\t threshold=" + nf.format(threshold));
					}
				}
				print("");

				/**
				 * isPattern is set when ALL definitions are matched within the threshold are matched
				 */ 
				if ( candInstancesList.get(i).isPattern() ) {
					count_isPattern++;
				} else {
					count_isNotPattern++;
				}    	    
			}   	    


			/*
			 * Since we have the percentage for all possible candidate
			 * instances, we can rank the results in the candInstancesList
			 */
			if ( rank_results ) rankResults( candInstancesList );

			NumberFormat nf = NumberFormat.getPercentInstance();
			if ( print_on_cmdline )
				print("Number of positive candidate instances after the dynamic analysis: " + count_isPattern + " out of " + candInstancesList.size() + " ( threshold = " + nf.format(threshold) + " )" );
			if ( count_isPattern > 0){
				if ( print_on_cmdline )
					print("Here is a ranked list of all candidate instances with the corresponding class names {and pattern roles}: " + 
							candInstancesList.getFirst().getNames() + "\n");		
				for (int i=0;i<candInstancesList.size();i++){
					if (candInstancesList.get(i).isPattern() )
						if ( print_on_cmdline )
							print("  " + i + "\t " + nf.format( candInstancesList.get(i).getPercentage() ) +
									"\t " + candInstancesList.get(i).getRoles());
				}
			} 


			/*
			 * Store results in XML file
			 */ 
			resultsStream.println("<result name=\"" + candidateInstancesFileName 
					+ "\" designPattern=\"" + patternName 
					+ "\" sourceCode=\"" + codeExample
					+ "\" numberOfHits=\"" + count_isPattern 
					+ "/" + candInstancesList.size() + "\" >");

			for (int i=0; i<candInstancesList.size(); i++ ){		    
				if( candInstancesList.get(i).isPattern() ){
					resultsStream.println("<candidateInstance percentage=\"" 
							+ nf.format( candInstancesList.get(i).getPercentage() ) 
							+ "\" threshold=\"" + threshold   
							+ "\" roles=\"" + candInstancesList.get(i).getRoles() + "\" />" );
				}
			}
			resultsStream.println("</result>");	    

			if ( print_on_cmdline )
				print("\n######################################################################################################## \n");



			// Print out summary of isPattern/isNotPattern
			if( pointer_y < res.length ){
				if ( pointer_y == 0) {
					res[pointer_x][pointer_y]  = codeExample;
					res2[pointer_x][pointer_y] = codeExample;
					pointer_y++;
				}
				res[pointer_x][pointer_y]  = count_isPattern + "";
				res2[pointer_x][pointer_y] = count_isNotPattern + "";
				pointer_y++;
			} else {
				pointer_x++;
				pointer_y=0;
				if ( pointer_y == 0) {
					res[pointer_x][pointer_y]  = codeExample;
					res2[pointer_x][pointer_y] = codeExample;
					pointer_y++;
				}
				res[pointer_x][pointer_y]  = count_isPattern + "";
				res2[pointer_x][pointer_y] = count_isNotPattern + "";
				pointer_y++;
			}

		} else if ( print_stats && candInstancesList.isEmpty() ) {
			if ( print_on_cmdline )
				print("Stats empty():  " + candidateInstancesFileName);
		}
	} // End of run method   	



	/**
	 * Rank candidate instances in list according to their percentage
	 * 
	 * @param candInstancesList list with all possible candidate instances
	 */    
	private void rankResults(LinkedList<CandidateInstance> list) {
		if ( list.size() > 0 )
			quicksortForLinkedList(list, 0, list.size()-1 );
		else 
			;
	}


	/**
	 * Implementation of Quicksort for our LinkedList
	 */
	private void quicksortForLinkedList(LinkedList<CandidateInstance> list, int left, int right) {
		print("quicksortForLinkedList -> " + list.size() + " " + left + " " + right);
		if (right > left){
			int pivotIndex = left;
			int pivotNewIndex = partition(list, left, right, pivotIndex);
			
			print("1  right > left -> left + (pivotNewIndex-1)  " + left + " " + (pivotNewIndex-1) );
			quicksortForLinkedList(list, left, (pivotNewIndex-1) );
			
			print("2  right > left -> (pivotNewIndex+1) + right " + (pivotNewIndex+1) + " " + right);
			quicksortForLinkedList(list, (pivotNewIndex+1), right);
		}
	}

	/**
	 * Implementation of Quicksort with in-place partition
	 * 
	 * @return index 
	 */
	private int partition(LinkedList<CandidateInstance> list, int left, int right, int pivotIndex){
		double pivotValue = list.get( pivotIndex ).getPercentage();
		swap( list, pivotIndex, right); 	// Move pivot to end
		int storeIndex = left;
		for( int i=left; i<right; i++ ){
			if (list.get(i).getPercentage() > pivotValue) {
				swap( list, storeIndex, i);
				storeIndex = storeIndex + 1;
			}
		}
		swap( list, right, storeIndex); 	// Move pivot to its final place
		print("  partition(left="+left+", right="+right+", pivotIndex="+pivotIndex+"): return=" + storeIndex);
		return storeIndex;
	}

	/**
	 * Implementation of Quicksort, swap method
	 * 
	 * @param list list that needs to be swapped
	 * @param right
	 * @param storeIndex
	 */
	private void swap(LinkedList<CandidateInstance> list, int right, int storeIndex) {
		CandidateInstance obj = list.get(right);
		list.set(right, list.get(storeIndex));
		list.set(storeIndex, obj);	
	}


	/**
	 * Print datastructure that contains all matched facts for the given candidate instance
	 * 
	 * @param candInstancesList2 list with all candidate instances
	 */
	private void print_datastructure(LinkedList<CandidateInstance> candInstancesList2) {
		for(int i=0; i < candInstancesList2.size(); i++) {
			if ( debug ) print("#### Candidate Instance("+i+")     ####");
			CandidateInstance caIn = candInstancesList2.get(i);
			LinkedList[] caInList = caIn.getMatchedFactsDatastructure();
			if (print_results) print(i + "| getRoles: " + caIn.getRoles() );

			if ( caIn.isPattern() ) {
				if (print_results) print(i + "| Candidate Instances is a pattern :) \n");
				for( int r=0; r < caInList.length; r++) {
					if ( caInList[r] != null && !caInList[r].isEmpty() ){
						for (int s=0; s < caInList[r].size(); s++) {
							NamedNodeMap nodemap = ((Node)caInList[r].get(s)).getAttributes();

							/** 
							 * Print datastructure
							 */
							if( print_datastructure ) {
								print("");
								print(r + " " + s + "| getRoles:       " + caIn.getRoles());
								print(r + " " + s + "| className:      " + nodemap.getNamedItem("className"));
								print(r + " " + s + "| calledByClass:  " + nodemap.getNamedItem("calledByClass"));
								print(r + " " + s + "| thisObject:     " + nodemap.getNamedItem("thisObject"));
								print(r + " " + s + "| calledByObject: " + nodemap.getNamedItem("calledByObject"));
								print(r + " " + s + "| orderNumber:    " + nodemap.getNamedItem("orderNumber"));
								print(r + " " + s + "| methodName:     " + nodemap.getNamedItem("methodName"));
								print(r + " " + s + "| calledByMethod: " + nodemap.getNamedItem("calledByMethod"));
								print(r + " " + s + "| args:           " + nodemap.getNamedItem("args"));
								print(r + " " + s + "| callDepth:      " + nodemap.getNamedItem("callDepth"));
							}
						}
					}
				}
			} else {
				if (print_results) print(i + "| Candidate Instances is NOT a pattern!!! \n");
			}
		}	
	}



	/**
	 * Print results of the candInstancesList
	 * 
	 * @param candInstancesList Candidate Instances list
	 * @param dpDefList Design Pattern definition list
	 * @param dp dynamic design pattern definition file name
	 */
	private void printResults(LinkedList<CandidateInstance> candInstancesList, NodeList dpDefList, String dp) {



		if ( print_results ){
			print("###############################################################################################################################");
			print("Design Pattern: " + dp + "\n");
			if ( candInstancesList.size() > 0 ){
				print("Get Design Pattern Class Names:\n" + candInstancesList.get(0).getNames());
			}	    

			if ( debug && dpDefList != null ) print("_|#| dpDefList: length=" + dpDefList.getLength() );
		}

		if ( dpDefList != null && dpDefList.getLength() > 0 ) {
			for (int m=0; m < dpDefList.getLength(); m++) {
				Node dpDefListNode = dpDefList.item(m);

				String childCallInSubtree = dpDefListNode.getAttributes().getNamedItem("nextCallInSubtree").getNodeValue();
				boolean isOrderSubtree = childCallInSubtree.toLowerCase().equals("yes") || childCallInSubtree.toLowerCase().equals("true");

				String inOrder  = dpDefListNode.getAttributes().getNamedItem("nextCallInOrder").getNodeValue();
				boolean isOrderRequired = inOrder.toLowerCase().equals("yes") || inOrder.toLowerCase().equals("true");

				if ( debug ){
					if ( isOrderSubtree ) {
						print("_| dpDefListItem("+m+") nextCallInSubtree ");
					} else if ( isOrderRequired ) {
						print("_| dpDefListItem("+m+") nextCallInOrder == yes ");
					} else {
						print("_| dpDefListItem("+m+") nextCallInOrder == no ");
					}
				}
			}		
		}

		print("");

		if ( print_datastructure && debug ) {
			print_datastructure(candInstancesList);
		}

	}




	/**
	 * This method reads an input file that contains a list of  
	 * file names that contain the static and dynamic facts. The method returns a LinkedList
	 * that contains FactFiles objects for each line of the input file.
	 * These objects contain three file names: <br>
	 *  - candidate instances file name <br>
	 *  - dynamic definition file name<br>
	 *  - dynamic facts file name
	 */
	private LinkedList<FactFiles> readFactFiles( String inputFileName ){
		int lineCounter = 0;
		File factFiles = new File( inputFileName );

		LinkedList<FactFiles> factFilesList = new LinkedList<FactFiles>();

		if(factFiles.exists() && factFiles.canRead() && factFiles.isFile()){
			try {
				//Create a buffer reader for the passed file
				BufferedReader br = new BufferedReader(new FileReader(factFiles));	  	        
				String line;

				// Read the file, line by line
				while ((line = br.readLine()) != null) {
					lineCounter++;

					/*
					 * Lines with "%" are comments and are neglected.
					 * Lines with "" or "\n" are also neglected.
					 */
					if( !(line.startsWith("%")) && !(line.equals("") && !(line.equals("\n")) )) {
						// Parse each line
						String[] filenames = line.split("\\s");

						// check if there are 3 file names provided
						if ( filenames.length != 3) {
							System.err.println("Error in line " + lineCounter + " of input file = " +  inputFileName + 
									"\nNumber of files per line in this input file incorrect = " + filenames.length + "\n\n" +
									"Please make sure to provide 3 file names on each \n" +
									"line for the input file. The first file name has \n" +
									"to be the candidate instances file, the second name \n" +
									"has to be the dynamic facts file and the third file \n" +
							"name is for the dynamic definition file." );
							System.exit(1);
						}

						/*
						 * Sanity check:
						 * Check if the provided file names are correct and that all three files exist
						 */
						File f1 = new File( filenames[0] );
						File f2 = new File( filenames[1] );
						File f3 = new File( filenames[2] );

						if ( f1.exists() && f1.canRead() && f1.isFile() &&
								f2.exists() && f2.canRead() && f2.isFile() &&
								f3.exists() && f3.canRead() && f3.isFile() ) {

							// All three files exists, can be read and are files
							FactFiles ff = new FactFiles(filenames[0], filenames[1], filenames[2]);
							factFilesList.add(ff);
						} else {
							System.err.println("Error in line " + lineCounter + " of input file = " +  inputFileName +
									"\nFiles can not be detected! \n\n" +
									"Some of the filenames that you provide " +
									"can not be detected and opened. \nPlease check the file names " +
									"that you provided.\n\n " + 
									"Please make sure to provide 3 file names on each \n" +
									"line for the input file. The first file name has \n" +
									"to be the candidate instances file, the second name \n" +
									"has to be the dynamic facts file and the third file \n" +
							"name is for the dynamic definition file." );
							System.exit(1);
						}
					}	 
				}     
			} catch (Exception e) {
				System.err.println("Exception in line " + lineCounter + " of input file = " +  inputFileName + "\n\n" +
						"The input file cannot be read. Please check if the file " +
						"exists \nand that the file has the correct format. \n" +
						"Please make sure to provide 3 file names on each \n" +
						"line for the input file. The first file name has \n" +
						"to be the candidate instances file, the second name \n" +
						"has to be the dynamic facts file and the third file \n" +
				"name is for the dynamic definition file." );
				e.printStackTrace();
			} 	        
		}
		return factFilesList;
	}




	/**
	 * Helper method that allows shorter code for printing text to the command line
	 * 
	 * @param string to be printed
	 */
	public static void print(String string){
		System.out.println(string);
	}


	/**
	 * Usage information of this software
	 */
	private void usage(boolean printUsage) 
	{
		String message = "Usage: java PatternDetectionEngine -input <FactsFile> " 
			+ "-ci <CandidateInstancesFileName> -df <DynamicFactsFileName> -dd <DynamicDefinitionFileName> "
			+ "-redirect <output_filename> [-threshold] [-create_report] [-example] [-testsuite] [-print_statistics]/[-ps] [-print_datastructure]/[-pd] -debug [-h] [-help] \n\n"
			+ "<FactsFile>               	: facts file with several facts \n"	
			+ "<CandidateInstancesFileName> : extracted facts from javex, grok and ql \n"
			+ "<DynamicFactsFileName>       : dynamic facts generated using probekit \n"
			+ "<DynamicDefinitionFileName>  : dynamic design pattern definition in XML format \n\n"
			+ "This program can be used in two ways. \n\n"
			+ "First:  -ci <CandidateInstancesFileName> -df <DynamicFactsFileName> -dd <DynamicDefinitionFileName> \n"
			+ "        The user can pass three fact files that are needed for the \n"
			+ "        design pattern detection/verification process \n\n"
			+ "Second: -input <FactsFile> \n"
			+ "        This parameter allows the user to pass a file that contains \n"
			+ "        a list of files that need to be processed. Each line contains \n"
			+ "        three file names. The first file name is for the candidate instances \n"
			+ "        the second for the dynamic facts file and the third for the dynamic \n"
			+ "        definition. \n\n"
			+ "There are a couple of optional parameters that allow to specify different output from the program:\n"
			+ "-example:                         If this argument is passed to the program then example files are loaded and processed.\n" 
			+ "                                  This can be used to see how PDE works. \n"
			+ "-redirect <output_filename>       Redirect command line output to textfile.\n"
			+ "-threshold                        Value between 0.0 and 1.0 that is used in ranking the found candidate instances.\n"
			+ "-create_report                    Creates report for test of GoF Patterns.\n"
			+ "-print_statistics or -ps          Prints statistics with the results of the candidate instances.\n"
			+ "-print_datastruture or -pd        Prints datastructure that contains all matching node that match\n"
			+ "                                  the dynamic definition.\n"
			+ "-print_time                       Print time in millisecond for each method call. This can be used \n"
			+ "                                  for benchmarking PDE.";
		if( printUsage ) 
			print(message);
	}

}




