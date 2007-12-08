package ca.yorku.cse.designpatterns;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * This class represents a candidate instance as an object. It is used to store the candidate instances that were detected during the static analysis. Each object of this class is a possible candidate instance of a design pattern. They are stored in this format for easier access.
 * @author  Marcel Birkner
 * @version  0.6
 */
public class CandidateInstance{

	/*
	 * Number of roles in this pattern.
	 * Names of the classes that make up the pattern.
	 * Roles in the pattern.
	 */
	private int numberOfRoles;
	private HashMap<Integer, String> nameOfRolesMap;
	private HashMap<Integer, String> candidateInstanceRolesMap;

	/**
	 * Data structure that contains a <code>LinkedList</code> of nodes
	 * that stores all nodes that match the dynamic definition of the 
	 * design pattern.
	 */
	// private LinkedList[] matchedFactsDatastructure = null;
	private String filename = "serializedObjects/candidateInstance." + this.hashCode() + ".ser";
	
	// Is set to true if the candidate instance matches the definitions 100%
	private boolean isPattern;
	private double percentage;

	/**
	 * Default constructor.
	 */
	public CandidateInstance() {
		this.numberOfRoles = 0;
		nameOfRolesMap = new HashMap<Integer, String>();
		candidateInstanceRolesMap = new HashMap<Integer, String>();
		this.isPattern = false;
	}

	/**
	 * Constructor that takes an array containing values for roles
	 * and sets them in this object. It also takes the value for number
	 * of roles and sets numOfRoles attribute. The attribute isPattern
	 * is set to false since for now it is not known if this is a
	 * pattern or not.
	 */
	public CandidateInstance(String[] nameOfRoles, String[] candidateInstanceRoles, int numberOfRoles) {
		this.numberOfRoles = numberOfRoles;
		this.nameOfRolesMap = new HashMap<Integer, String>();

		for (int i = 0; i < numberOfRoles; i++) {
			this.nameOfRolesMap.put(i, nameOfRoles[i + 1]); // or names[i]
		}

		this.candidateInstanceRolesMap = new HashMap<Integer, String>();
		for (int i = 0; i < numberOfRoles; i++) {
			this.candidateInstanceRolesMap.put(i, candidateInstanceRoles[i + 1]);
		}
		this.isPattern = false;
	}

	/**
	 * Returns the number of roles for this design pattern.
	 * 
	 * @return number of roles
	 */
	public int getNumOfRoles() {
		return this.numberOfRoles;
	}

	/**
	 * Returns a <code>HashMap</code> with the names of the roles.
	 * 
	 * @return map with the names of the roles
	 */
	public HashMap<Integer, String> getNames() {
		return this.nameOfRolesMap;
	}

	/**
	 * Returns a Map with the roles for the candidate instance.
	 * 
	 * @return map with the candidate instance roles
	 */
	public HashMap<Integer, String> getRoles() {
		return this.candidateInstanceRolesMap;
	}

	/**
	 * Returns true if this candidate instance matches the dynamic definition of the design pattern 100%. Otherwise it returns false. 
	 * @return  true - if this candidate instance matches the dynamic definition 100%, otherwise false
	 */
	public boolean isPattern() {
		return this.isPattern;
	}

	/**
	 * Sets the number of roles for this candidate instance.
	 * @param numberOfRoles  number of roles for this candidate instance
	 */
	public void setNumberOfRoles(int numberOfRoles) {
		this.numberOfRoles = numberOfRoles;
	}

	/**
	 * Set the role names of the design pattern.
	 * 
	 * @param namesOfDesignPatternRoles HashMap with the names for this candidate instance
	 */
	public void setNames(HashMap<Integer, String> namesOfDesignPatternRoles) {
		this.nameOfRolesMap = namesOfDesignPatternRoles;
	}

	/**
	 * Set the role names for the candidate instance.
	 *  
	 * @param nameOfCandidateInstanceRoles HashMap with the role names for this candidate instance
	 */
	public void setRoles(HashMap<Integer, String> nameOfCandidateInstanceRoles) {
		this.candidateInstanceRolesMap = nameOfCandidateInstanceRoles;
	}

	/**
	 * Set if this candidate instance if a design pattern.
	 *  
	 * @param pattern true if candidate instance is a design pattern, otherwise false
	 */
	public void setIsPattern(boolean pattern) {
		this.isPattern = pattern;
	}

	/**
	 * Set data structure for this candidate instance with all Nodes from the dynamic  facts that match the design pattern definition.
	 * @param linkedList  LinkedList data structure of Nodes that contains all nodes that match the dynamic linkedList design pattern definition
	 */
	public void setMatchedFactsDatastructure(LinkedList[] linkedList) {		
		//this.matchedFactsDatastructure = linkedList;
	    ObjectOutputStream objstream;
		try {
			objstream = new ObjectOutputStream(new FileOutputStream(filename) );
		    objstream.writeObject( linkedList );
		    objstream.close();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		//this.matchedFactsDatastructure = null;
	}

	/**
	 * Returns LinkedList[] that contains all matching Nodes from the dynamic facts that match the dynamic definition of the design pattern.
	 * @return  LinkedList[] with Nodes that match the dynamic definition of the   design pattern.
	 */
	public LinkedList[] getMatchedFactsDatastructure() {
		
		LinkedList[] returnVal = null;
	    try {
	    	ObjectInputStream objstream = new ObjectInputStream(new FileInputStream(filename));
	    	returnVal = (LinkedList[]) objstream.readObject();
			objstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		return returnVal;
	}
	

	/**
	 * Returns percentage value that says how good this instances matches the dynamic definition
	 * @return  percentage value that says how good this instances matches the dynamic definition
	 */
	public double getPercentage() {
		return percentage;
	}

	/**
	 * Set percentage value that says how good this instances matches the dynamic definition
	 * @param percentage  value that says how good this instances matches the dynamic definition
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
}