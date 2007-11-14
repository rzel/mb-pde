package ca.yorku.cse.designpatterns;

import java.util.LinkedList;

/**
 * This class processes the textfile that contains the tuples with the candidate
 * instances. It reads the file line by line, and creates a
 * <code>CandidateInstance</code> instances and stores all candidate instances
 * in a linked list. The list with all candidate instances can be retrieved
 * through the getter method getter method.
 * 
 * @author Marcel Birkner
 */
public interface CandidateInstanceListInterface {

    /** 
     * Getter method that returns the list of all candidate instances read from the input file.
     * 
     * @return LinkedList of static fact objects
     */
    public abstract LinkedList<CandidateInstance> getCandidateInstancesList();

}