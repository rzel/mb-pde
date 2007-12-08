package ca.yorku.cse.designpatterns;

import java.util.*;
import java.io.*;

/**
 * This class processes the text file that contains the tuples with the candidate
 * instances. It reads the file line by line, and creates a
 * <code>CandidateInstance</code> instances and stores all candidate instances
 * in a linked list. The list with all candidate instances can be retrieved
 * through the getter method getter method.
 * 
 * The input file has to match the format of the following example of the
 * Adapter Design Pattern: <br>
 * <br>
 *    // target adapter adaptee<br>
 *    DP ajp_code.adapter.Contact ajp_code.adapter.ContactAdapter ajp_code.adapter.Chovnatlh<br>
 *    DP ajp_code.adapter.Contact ajp_code.adapter.ContactAdapter ajp_code.adapter.ChovnatlhImpl<br>
 * <br>
 * 
 * This example contains three roles in line one: target, adapter and adaptee.
 * The other two lines contain possible candidate instances. The number of
 * classes in each line has to match the number of roles.
 * 
 * @author Marcel Birkner
 * @version 0.5
 */
public class CandidateInstanceList implements CandidateInstanceListInterface {

    /**
     * Contains a list of all possible candidate instances that
     * were found during the static analysis.
     */
    private LinkedList<CandidateInstance> candidateInstance; 
    
    /**
     * Enable debugging
     */
    private boolean debug;
    
  
    /**
     * Creates a new instance of the CandidateInstanceProcessor.
     * 
     * The input file has to match the format of the following example of
     * the Adapter Design Pattern: <br><br>
     * 
     *    // target adapter adaptee<br>
     *    DP ajp_code.adapter.Contact ajp_code.adapter.ContactAdapter ajp_code.adapter.Chovnatlh<br>
     *    DP ajp_code.adapter.Contact ajp_code.adapter.ContactAdapter ajp_code.adapter.ChovnatlhImpl<br><br>
     * 
     * This example contains three roles in line one: target, adapter and adaptee.
     * The other two lines contain possible candidate instances. The number of 
     * classes in each line has to match the number of roles.
     * 
     * @param candidateInstanceFile Textfile that contains a set of candidate instances for each line.
     * @param debug Variable for debugging.
     */
    public CandidateInstanceList(String candidateInstanceFile, boolean debug){
	this.debug = debug;
        this.parseFile(candidateInstanceFile);
    }
   
    /**
     * Parses the passed file with static facts, creates an instance of
     * CandidateInstance for each line in the file and stores the created
     * object in the staticFactObjects list.
     * 
     * @param filename Name of textfile that will be parsed to retrieve candidate instances.
     */
    private void parseFile(String filename) {
      
        try {
            //Create a buffer reader for the passed file
            BufferedReader br = new BufferedReader(new FileReader(filename));
   
            //Create the list that will be populated below
            this.candidateInstance = new LinkedList<CandidateInstance>();
          
            /*
             * Read the first line that contains the pattern roles.
             * The first line has the following format:
             * "// role1 role2 role3 ..."
             */
            String line = br.readLine();
            String[] roleNamesOfDesignPattern = line.split("\\s");
            String[] nameOfRoles = new String[ roleNamesOfDesignPattern.length ];
           
            int i;
            for(i = 0; i < roleNamesOfDesignPattern.length; i++){
        	nameOfRoles[i] = roleNamesOfDesignPattern[i];
            }
            for(int j = i; j < nameOfRoles.length; j++){
        	System.err.println("THIS SHOULD NEVER BE CALLED #####");
        	nameOfRoles[j] = "";                         
            }
            
            /* 
             * Read the rest of the textfile line by line
             * and extract the class names for the 
             * candidate instancess. 
             */
            while ((line = br.readLine()) != null) {
        	    
                // Parse each line, create and populate each CandidateInstance 
                String[] rolesParams = line.split("\\s");
                
        	// Contains all roles that are defined for the design pattern.
        	String[] candidateInstanceRoles = new String[ rolesParams.length ];
   
                int k;
                for(k = 0; k < rolesParams.length; k++){
                    candidateInstanceRoles[k] = rolesParams[k];
                }
                for(int l = k; l < candidateInstanceRoles.length; l++){
                    System.err.println("THIS SHOULD NEVER BE CALLED #####");
                    candidateInstanceRoles[l] = "";
                }
                
                // Create a new instance of the CandidateInstance class for each line parsed.
                CandidateInstance ci =
                    new CandidateInstance(nameOfRoles, candidateInstanceRoles, rolesParams.length-1);
                
                // Add all objects to the candidate instance list
                this.candidateInstance.add(ci);      
            }      

        } catch (Exception e) {
            System.out.println("CandidateInstanceProcessor: -> parseFile() " +
            		"An error occured with candidate instance textfile. " +
            		"Cannot read from file: " + filename +
            		"Please check file and make sure it matches " +
            		"the format of the following example (Adapter Pattern)." +
            		"\n" +
            		"\t// target adapter adaptee " +
            		"\tDP ajp_code.adapter.Contact ajp_code.adapter.ContactAdapter ajp_code.adapter.Chovnatlh" +
            		"\tDP ajp_code.adapter.Contact ajp_code.adapter.ContactAdapter ajp_code.adapter.ChovnatlhImpl" +
            		"\n" +
            		"This example contains three roles in line one: target, adapter and adaptee.\n" +
            		"The other two lines contain possible candidate instances. The number of" +
            		"classes in each line has to match the number of roles.");
            e.printStackTrace();
            System.exit(1);
        } 
    }
    
    /* (non-Javadoc)
     * @see ca.yorku.cse.designpatterns.CandidateInstanceProcessorInterface#getCandidateInstancesList()
     */
    public LinkedList<CandidateInstance> getCandidateInstancesList(){
	return this.candidateInstance;
    }
}