package ca.yorku.cse.designpatterns;

import java.util.LinkedList;

import org.w3c.dom.NodeList;

/**
 * Validates the results given the dynamic definitions and the
 * datastructure that contains the matched facts. 
 */
public interface ValidatorInterface {

    public abstract void validate(String designPatternDef,
	    NodeList dynFactsList,
	    LinkedList<CandidateInstance> candInstancesList, boolean debug,
	    boolean printDatastructure);

    /**
     * Validate all candidate instances the method calls
     * are coming from the right objects. Check if the method
     * calls are on the right objects as well.
     * 
     * @param candidateInstancesList list with candidate instances
     * @param designPatternDefinitionList dynamic design pattern definition list
     */
    public abstract LinkedList<CandidateInstance> validateObjects(
	    LinkedList<CandidateInstance> candidateInstancesList,
	    NodeList designPatternDefinitionList);

    /**
     * Returns the candidate instance list
     * 
     * @return candInstancesList
     */
    public abstract LinkedList<CandidateInstance> getCandidateInstancesList();

    /**
     * Returns the dynamic definition list
     * 
     * @return NodeList dynamic definition list
     */
    public abstract NodeList getDynamicDefinitionList();

}