package ca.yorku.cse.designpatterns;

/**
 * An instance of this class represents a group of three files that contain the  candidate instances, dynamic facts and dynamic design pattern definition
 * @author  Marcel Birkner
 * @version  0.8
 * @since  27 June, 2007
 */
public class FactFiles {
    private String candidateInstanceFile;
    private String dynamicFactsFile;
    private String dynamicDefinitionFile;
    
    /**
     * Takes three filenames as parameter and stores them in this Object.
     * 
     * @param candInst file with candidate instances 
     * @param dynamicFacts file with dynamic facts of program
     * @param dynDef dynamic definition of the design pattern
     */
    public FactFiles(String candInst, String dynamicFacts, String dynDef){
	this.candidateInstanceFile = candInst;
	this.dynamicFactsFile      = dynamicFacts;
	this.dynamicDefinitionFile = dynDef;
    }

    /**
     * Returns file candidate instance file
     * @return  candidate instance file
     */
    public String getCandidateInstanceFile() {
        return this.candidateInstanceFile;
    }
    
    /**
     * Returns dynamic facts file
     * @return  dynamic facts file
     */
    public String getDynamicFactsFile() {
        return this.dynamicFactsFile;
    }
    
    /**
     * Returns dynamic definition file
     * @return  dynamic definition file
     */
    public String getDynamicDefinitionFile() {
        return this.dynamicDefinitionFile;
    }
    
    /** 
     * Sets candidate instance file
     * 
     * @param candidateInstance file name of candidate instances file
     */
    public void setCandidateInstancefile(String candidateInstance){
	this.candidateInstanceFile = candidateInstance;
    }
    
    /**
     * Sets dynamic facts file name
     * @param dynamicFacts  file name of dynamic facts fule
     */
    public void setDynamicFactsFile(String dynamicFacts){
	this.candidateInstanceFile = dynamicFacts;
    }
    
    /**
     * Sets dynamic definition file name
     * @param dynDef  dynamic definition file name
     */
    public void setDynamicDefinitionFile(String dynDef){
	this.candidateInstanceFile = dynDef;
    }
}
