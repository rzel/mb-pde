package ca.yorku.cse.designpatterns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Instance of the PatternDetection Engine
 * detects software <b>Design Patterns</b>, given static 
 * and dynamic facts.
 * 
 * @author Marcel Birkner
 * @version 0.9
 * @since 29 July, 2007
 */
public class PatternDetectionEngine 
{
    // Static variables
    private static boolean print_datastructure = false;
    private static boolean print_stats         = false;
    private static boolean create_report       = false;
    private static boolean debug               = false;
    private static boolean print_time	       = false;
    
    private static String candidateInstancesFileName = null;
    private static String dynamicFactsFileName       = null;
    private static String dynamicDefinitionFileName  = null;
    
    private static double threshold            = 0.8;		// default threshold is 80%
    private static LinkedList<FactFiles> input = null;		// stores the file names of the input file
    private static String report_filename      = "report.txt";	// default filename for report
    
   
    private static boolean print_results = false;
    private static LinkedList<CandidateInstance> candInstancesList = null;
    private static NodeList dynamicDefinitionList = null;
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
		 *
		 * Dynamic definition document for this design pattern.
		 */
		Document doc = null;
		String filename = "software.xml";
		try {
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    doc = db.parse(new File(filename));
	        } catch (Exception e) {
	            System.out.println("PatternDetectionEngine: -> " +
	            		"Constructor(): Cannot read from file: " + filename +
	            		"Please make sure that the file exists.");
	            e.printStackTrace();
	            System.exit(1);
	        }
	        
	        String shell = "";
	        String javex = "";
	        String grok = "";
	        String ql = "";

	        NodeList properties = doc.getElementsByTagName("properties");
	        for (int l = 0; l < properties.getLength(); l++) {  
	            shell = properties.item(l).getAttributes().getNamedItem("shellScript").getNodeValue();
	            javex = properties.item(l).getAttributes().getNamedItem("javex").getNodeValue();
	            grok  = properties.item(l).getAttributes().getNamedItem("grok").getNodeValue();
	            ql    = properties.item(l).getAttributes().getNamedItem("ql").getNodeValue();
	            
	            print("1: " + shell);
	            print("2: " + javex);
	            print("3: " + grok);
	            print("4: " + ql);
	        }
	        
	        NodeList software = doc.getElementsByTagName("software");
	        for (int j = 0; j < software.getLength(); j++) { 
	            String name      = software.item(j).getAttributes().getNamedItem("name").getNodeValue();
	            String directory = software.item(j).getAttributes().getNamedItem("directory").getNodeValue();
	            String mainClass = software.item(j).getAttributes().getNamedItem("mainClass").getNodeValue();
	            print("\n 1: " + name );
	            print(  " 2: " + directory);
	            print(  " 3: " + mainClass );
	        
	            String command = shell +" "+ directory +" "+ javex +" "+ name +" "+ "ql/adapter.ql" ; 
	            print(command);

	            //String cmd     = " ./static.sh ajp_code/adapter ./javex ajp_code.adapter ql/adapter.ql";
	            StaticAnalysis st = new StaticAnalysis(command, "static_output.txt");
	            st.runStaticAnalysis();	        
	        
	        }
	        


	        System.exit(1);
		
		

		
		
		
		
//	 	String script = args[++i];
//	 	String classf = args[++i];
//	 	String javexf = args[++i];
//	 	String outnam = args[++i];
//	 	String qlscri = args[++i];
	 	
	 	/*if ( ! (script.startsWith("./") || script.startsWith("/") ) ) {
	 	    script = "./" + script;
	 	}*/
	 	
	 	//String arg3 = "compile_ajp ";
	 	//String cmd = script + " " + classf + " " + javexf + " " + outnam + " " + qlscri;
	 	// print("arguments: " + cmd);
	 	
	 	// Validation: Check if run.sh script and software directory can be found
/*	 	File ff = new File( script );
	 	File df = new File( javexf );
	 	if ( ! ff.exists() ) {
	 	    print("ERROR: Script (" + script + ") can not be found.");
	 	    System.exit(1);
	 	}
	 	if ( ! df.exists() ) {
	 	    print("ERROR: Script (" + javexf + ") can not be found.");
	 	    System.exit(1);
	 	}*/

	    } 
	    else if (args[i].equals("-usage") || args[i].equals("-help") || args[i].equals("-h") || args[i].equals("--h") || args[i].equals("--help")) {
		pde.usage(true);
		System.exit(1);
	    }
	}

	
	
	String inputFileName = null;
	boolean redirectSystemOut = false;
	String output_filename = "output.txt";		// default redirect output textfile
	
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
		inputFileName = args[++i];
		print("Input parameter for -input " + inputFileName);
	    } 
	    else if ( args[i].equals("-redirect") ){ 
		redirectSystemOut = true;
		output_filename = args[++i];
		print("Input parameter for -redirect true and output file name " + output_filename);
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
		print("Input parameter for -create_report true. Writting report to file: " + report_filename);
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
	    else if ( args[i].equals("-print_time") ){
		print_time = true;
		print("Input parameter for -print_time true");
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
		System.setOut(new PrintStream( new FileOutputStream( output_filename )));
	    } catch ( FileNotFoundException e ) {
		e.printStackTrace();
	    }
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
	     * Transform probekit textfile to valid XML format
	     */
	    dy = pde.transformToXML(dy);


	    /*
	     * Run Detection Engine and verify/detect Design Patterns from the fact files
	     */
	    pde.run(ci, dy, dp);	 
	    
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
	if ( inputFileName != null ){
	    
	    input = pde.readFactFiles( inputFileName );
	    
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
		 * Transform probekit textfile to valid XML format
		 */
		dy = pde.transformToXML(dy);
		
		
		/*
		 * Run Detection Engine and verify/detect Design Patterns from the fact files
		 */
		pde.run(ci, dy, dp);
	    }
	    if ( print_results ) { 
		System.out.println("################################################################################################");
		System.out.println("# PDE: # of examples, input.size()=" + input.size() );
	    }	    
	}
	
	
	/*
	 * Print all stats for the 23 design patterns
	 */
	if ( print_stats || create_report ) {

	    // Redirect System Output Stream for report
	    try {
		System.setOut(new PrintStream( new FileOutputStream( report_filename )));
	    } catch ( FileNotFoundException e ) {
		print("FileNotFoundException: Redirect System Out failed.");
		e.printStackTrace();
	    }

	    //System.out.println("\n\nResults 1");
	    for (int i=0; i<res.length; i++) {
		for (int j=0; j<res[i].length; j++){
		    System.out.print(res[i][j] + " ");
		}
		System.out.println();
	    }


	    //System.out.println("Results 2");
	    for (int i=0; i<res2.length; i++) {
		for (int j=0; j<res2[i].length; j++){
		    System.out.print(res2[i][j] + " ");
		}
		System.out.println();
	    }	
	}
    }
	
	
    /**
     * Processes the input files and starts the detection of the 
     * design pattern given the input parameters.
     * 
     * @param ci File that contains the candidate instances
     * @param dy File that contains the dynamic facts extracted with Probekit
     * @param dd File that contains the dynamic definition of the design pattern
     */
    public void run(String ci, String dy, String dd) {
	if ( print_time ) print("run ->                            " + System.currentTimeMillis());
	
	// Set filenames
	candidateInstancesFileName   = ci;
	dynamicFactsFileName         = dy;
	dynamicDefinitionFileName    = dd;
	
	
	/* 
	 * CandidateInstanceProcessor
	 * -> need access to LinkedList with all objects
	 */
	if ( print_time ) print("run -> CandidateInstanceComposite " + System.currentTimeMillis());
	CandidateInstanceListInterface candInstances = new CandidateInstanceList(candidateInstancesFileName, debug);
	candInstancesList = candInstances.getCandidateInstancesList();
	if ( debug && !candInstancesList.isEmpty() ) 
	    System.out.println("candInstancesList size " + candInstancesList.size());

	
	/*
	 * DynamicFactsProcessor
	 * -> need access to complete document that
	 *    represents the dynamic facts of the software
	 */  
	if ( print_time ) print("run -> DynamicFactsProcessor      " + System.currentTimeMillis());
	DynamicFactsProcessorInterface dynFacts  = new DynamicFactsProcessor(dynamicFactsFileName, debug);
	NodeList dynFactsList = null;
	Document dynFactsDoc = dynFacts.getDynamicFactsDocument();
	dynFactsList = dynFactsDoc.getElementsByTagName("entry");
	if( debug ) 
	    System.out.println("dynFactsList Length: " + dynFactsList.getLength());
	
		
	/*
	 * Validates the matches found
	 */
    	ValidatorInterface validator = new Validator();
	if ( print_time ) print("run -> validator.validate         " + System.currentTimeMillis());
    	validator.validate(dynamicDefinitionFileName, dynFactsList, candInstancesList, debug, print_datastructure);
    	dynamicDefinitionList = validator.getDynamicDefinitionList();
    	candInstancesList = validator.getCandidateInstancesList();

    	
   	/*
    	 * Validate the matchedFacts with the design pattern definition
    	 * - check "thisObject"
    	 * - check "calledByObject"
    	 */
	if ( print_time ) print("run -> validator.validateObjects  " + System.currentTimeMillis());
    	validator.validateObjects(candInstancesList, dynamicDefinitionList);
    	candInstancesList = validator.getCandidateInstancesList();
    	
    	
	if ( print_time ) print("run -> printResults               " + System.currentTimeMillis());
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
    	    String codeExample = null;
    	    String patternName = null;   	    
    	    if ( candidateInstancesFileName.contains("/") && 
    		    candidateInstancesFileName.contains(".") && 
    		    candidateInstancesFileName.contains(".ql.") )
    	    {
        	    int index1 = candidateInstancesFileName.indexOf("/");
            	    int index2 = candidateInstancesFileName.indexOf(".");
            	    int index3 = candidateInstancesFileName.indexOf(".ql.");
            	    codeExample = candidateInstancesFileName.substring(index1+1, index2);
            	    patternName = candidateInstancesFileName.substring(index2+1, index3);  
    	    } else {    	    
    		codeExample = candidateInstancesFileName;
    		patternName = candidateInstancesFileName; 
    	    }
    	    
    	    print(  "# Code example from:           " + codeExample + 
    		  "\n# Pattern we want to detect:   " + patternName + "\n"); 
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
    		if ( debug ) print("number > threshold "  + number + "  "+  threshold);
    		if ( number >= threshold ) {	
    		    candInstancesList.get(i).setIsPattern(true);
    		    candInstancesList.get(i).setPercentage(number);
    		} else {
    		    candInstancesList.get(i).setIsPattern(false);
    		    candInstancesList.get(i).setPercentage(number);
    		}
    		
    		if ( print_results ) { 
    		    print("Candidate Instance is pattern?   " + candInstancesList.get(i).isPattern() + " \t\t" + nf.format(number) + "   \t\t threshold=" + nf.format(threshold));
    		}
    		
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
	    rank_results( candInstancesList );
    
	    NumberFormat nf = NumberFormat.getPercentInstance();
 	    print("Number of positive candidate instances after the dynamic analysis: " + count_isPattern + " out of " + candInstancesList.size() + " ( threshold = " + nf.format(threshold) + " )" );
	    if ( count_isPattern > 0){
		print("Here is a ranked list of all candidate instances with the corresponding class names {and pattern roles}: " + 
			candInstancesList.getFirst().getNames() + "\n");		
		for (int i=0;i<candInstancesList.size();i++){
		    if (candInstancesList.get(i).isPattern() )
			print("  " + i + "\t " + nf.format( candInstancesList.get(i).getPercentage() ) +
				"\t " + candInstancesList.get(i).getRoles());
		}
	    } else {
		print("None of the given candidate instances is a design pattern.");
	    }
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
    	    print("Stats empty():  " + candidateInstancesFileName);
    	}
    } // End of run method   	



    /**
     * Rank candidate instances in list according to their percentage
     * 
     * @param candInstancesList list with all possible candidate instances
     */    
    private void rank_results(LinkedList<CandidateInstance> list) {
	quicksortForLinkedList(list, 0, list.size()-1 );
    }
    
    
    /**
     * Implementation of Quicksort for our LinkedList
     */
    private void quicksortForLinkedList(LinkedList<CandidateInstance> list, int left, int right) {
        if (right > left){
            int pivotIndex = left;
            int pivotNewIndex = partition(list, left, right, pivotIndex);
            quicksortForLinkedList(list, left, pivotNewIndex-1);
            quicksortForLinkedList(list, pivotNewIndex+1, right);
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
	    print("Get Design Pattern Class Names:\n" + candInstancesList.get(0).getNames());
	    if ( debug ) print("_|#| dpDefList: length=" + dpDefList.getLength() );
	}
	
	for (int m=0; m < dpDefList.getLength(); m++) {
	    Node dpDefListNode = dpDefList.item(m);

	    /*String childCallNextNode = dpDefListNode.getAttributes().getNamedItem("dependentOnNextNode").getNodeValue();
	    boolean isOrderNextCall  = childCallNextNode.toLowerCase().equals("yes") || childCallNextNode.toLowerCase().equals("true");*/

	    String childCallInSubtree = dpDefListNode.getAttributes().getNamedItem("nextCallInSubtree").getNodeValue();
	    boolean isOrderSubtree = childCallInSubtree.toLowerCase().equals("yes") || childCallInSubtree.toLowerCase().equals("true");

	    String inOrder  = dpDefListNode.getAttributes().getNamedItem("nextCallInOrder").getNodeValue();
	    boolean isOrderRequired = inOrder.toLowerCase().equals("yes") || inOrder.toLowerCase().equals("true");

	    if ( debug ){
		/*if ( isOrderNextCall ) {
		    print("_| dpDefListItem("+m+") dependentOnNextNode ");
		} else */
		if ( isOrderSubtree ) {
		    print("_| dpDefListItem("+m+") nextCallInSubtree ");
		} else if ( isOrderRequired ) {
		    print("_| dpDefListItem("+m+") nextCallInOrder == yes ");
		} else {
		    print("_| dpDefListItem("+m+") nextCallInOrder == no ");
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
        
        String filenameXML = filenameTXT.replace(".txt", ".xml");
        String headerXML   = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
		      	     "<!DOCTYPE entry SYSTEM \"schema.dtd\">" +
		      	     "<entry args=\"\" calledByClass=\"\" calledByMethod=\"\" calledByObject=\"\" className=\"\" " +
		      	     "methodName=\"\" thisObject=\"\" callDepth=\"0\" >";
        String exitTag	   = "<exit calledByClass=\"\" calledByMethod=\"\" calledByObject=\"\" className=\"\" methodName=\"\" thisObject=\"\" callDepth=\"0\"></exit></entry>";

	File factFiles = new File(filenameTXT);
	if( factFiles.exists() && factFiles.canRead() && factFiles.isFile()) {
	    try {
		outputStream = new PrintWriter(new FileWriter(filenameXML));
		outputStream.println(headerXML);

		
		/*
	         * Read the file, line by line and count all entry tags and exit tags
	         */
		inputStream = new BufferedReader(new FileReader(filenameTXT));
		String line;
	        while ((line = inputStream.readLine()) != null) {
	            outputStream.println(line);
	            
	            // count number of entry and exit tags
	            if ( line.startsWith( "<entry" ) ) entryCount++;
	            if ( line.startsWith( "<exit"  ) ) exitCount++;

	        }
	        
	        if ( debug ) 
	            print("Entry and Exit tags are equal? " + entryCount + "==" + exitCount );
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
     * Helper method that allows shorter code for printing text to the command line
     * 
     * @param string to be printed
     */
    static void print(String string){
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




