
package ca.yorku.cse.designpatterns;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Validates the results given the dynamic definitions and the
 * datastructure that contains the matched facts.
 * 
 * Validator takes the first object from the LinkedList and passes it to 
 * DynamicDefinitionConverter so we can adjust the document with the real 
 * names of the classes we are looking at. This way we are substituting
 * i.e. "Leaf", "Composite", "Component" with the real class names
 * from the software system.  <br><br>
 * 
 * XML schema attributes:<br>
 * -> methodName, className, thisObject, args, calledByClass, calledByMethod<br><br>
 * 
 * 
 *  Then we initialize storeMatchedFacts datastructure:<br>
 *  The LinkedList[] stores a LinkedList of Nodes for each
 *  Design Pattern definition that needs to be matched.
 *  If a match between the dynamic facts and the DP definition
 *  is found this matching node is added to the 
 *  corresponding LinkedList<Node>. The more matches are found 
 *  the longer the Linked Lists of type <Node> can become.
 *  When we find a lot of matches for all LinkedLists<Node> 
 *  it is a very good indicator that this DP is valid.<br><br>
 *  
 *  This LinkedList[] is stored with each CandidateInstances, even 
 *  if no matches are found. Therefore it is easy to see later on why
 *  this CandidateInstance is not a Design Pattern and what matches
 *  are missing for it to be a design pattern.<br><br>
 *
 * Each node is added chronological therefore in each column their orderNumber
 * must be increasing. Each Node stores all information of our CandidateInstance,
 * full className methodName, calledByClass, calledByMethod, orderNumber, ...
 * 
 * @author Marcel Birkner
 * @version 0.8
 * 
 */
public class Validator implements ValidatorInterface {
    private LinkedList<CandidateInstance> candInstancesList = null;
    private NodeList dpDefList = null;
    private boolean debug = false;
    private boolean print_objects = false;
    private boolean printDatastructure = false;
    
    
    /* (non-Javadoc)
     * @see ca.yorku.cse.designpatterns.ValidatorInterface#validate(java.lang.String, org.w3c.dom.NodeList, java.util.LinkedList, boolean, boolean)
     */
    @SuppressWarnings("unchecked")
    public void validate(String designPatternDef, NodeList dynFactsList, LinkedList<CandidateInstance> candInstancesList, boolean debug, boolean printDatastructure){
	this.debug = debug;
	this.printDatastructure = printDatastructure;
	this.candInstancesList  = candInstancesList;
	
	
	/*
	 *  Take the first object from the LinkedList and pass it to 
	 *  StaticFactsObject so we can adjust the document with the correct 
	 *  names of the classes we are looking at. This way we are substituting
	 *  i.e. "Leaf", "Composite", "Component" with the real class names
	 *  from the software system.  
	 *  
	 *  XML schema attributes:
	 *  -> methodName, className, thisObject, args, calledByClass, calledByMethod
	 *  
	 */
    	for (int j=0; j < candInstancesList.size(); j++)
    	{
    	    /** 
    	     * CreatePatternDocument
    	     * -> need access to document that represents
    	     *    the design pattern we are looking for
    	     * -> pass current candInstancesList so that the XML document can be
    	     *    formatted with the right class names
    	     */
    	    DynamicDefinitionConverterInterface dpDef = new DynamicDefinitionConverter(designPatternDef, candInstancesList.get(j), debug);
    	    Document dpDefDoc = dpDef.getDesignPatternDocument();
    	    this.dpDefList = dpDefDoc.getElementsByTagName("entry");
    	    
      	    
    	    /*
    	     *  Initialize storeMatchedFacts datastructure:
    	     *  The LinkedList[] stores a LinkedList of Nodes for each
    	     *  Design Pattern definition that needs to be matched.
    	     *  If a match between the dynamic facts and the DP definition
    	     *  is found this matching node is added to the 
    	     *  corresponding LinkedList<Node>. The more matches are found 
    	     *  the longer the Linked Lists of type <Node> can become.
    	     *  When we find a lot of matches for all LinkedLists<Node> 
    	     *  it is a very good indicator that this DP is valid.
    	     *  
    	     *  This LinkedList[] is stored with each CandidateInstances, even 
    	     *  if no matches are found. Therefore it is easy to see later on why
    	     *  this CandidateInstance is not a Design Pattern and what matches
    	     *  are missing for it to be a design pattern.
    	     *  	      
    	     *    
    	     *  LinkedList[]         [1]       [2]       [3]       [4]  ...  [n]
    	     *                        |         |         |         |         |
    	     *  LinkedList<Node>   [node01]  [node04]  [node55]  [node09]  [node32]
    	     *                     [node03]  [node21]            [node25]
    	     *                     [node15]                      [node53]
    	     *                     [node31]                      [node88]
    	     *
    	     * Each node is added chronological therefore in each column their orderNumber
    	     * must be increasing. Each Node stores all information of our CandidateInstance,
    	     * full className methodName, calledByClass, calledByMethod, orderNumber, ...
    	     * 
    	     */
    	    LinkedList[] storeMatchedFacts = new LinkedList[ dpDefList.getLength() ];
    	    for (int p=0; p < dpDefList.getLength(); p++ ){
    		LinkedList<Node> dynMatches = new LinkedList<Node>();
    		storeMatchedFacts[p] = dynMatches;
    	    }


    	    /*
    	     *  Loop over all objects in Design Pattern Definition LinkedList 
    	     *  - get the first node of the CreatePatternDocument
    	     *    and look for this node in the DynamicFacts document
    	     *  - go through all nodes in the CreatePatternDocument
    	     *    and if all can be found then its most likely the 
    	     *    design pattern we were looking for
    	     */
    	    boolean isOrderRequired = false;
    	    boolean isOrderSubtree  = false;
   	    
    	    for (int n=0; n<dynFactsList.getLength(); n++) {
    		Node dynFactsListNode = dynFactsList.item(n);
    		    		   		
    		for (int m=0; m < dpDefList.getLength(); m++) {
    		    Node dpDefListNode = dpDefList.item(m);
    		    
    		    
		    LinkedList<Node> list = storeMatchedFacts[m];
    		    
    		    /**
    		     * Compare dynamic facts with pattern definition if 
    		     * a match exists
    		     */
    		    if ( doMatch(dpDefListNode, dynFactsListNode) ) {	

    			/**
    			 * Check if the next matching method call has to be 
    			 * in the Subtree of the current call. 
    			 */
   			
    			String childCallInSubtree = dpDefListNode.getAttributes().getNamedItem("nextCallInSubtree").getNodeValue();
    			isOrderSubtree = childCallInSubtree.toLowerCase().equals("yes") || childCallInSubtree.toLowerCase().equals("true");
    			
    			String inOrder  = dpDefListNode.getAttributes().getNamedItem("nextCallInOrder").getNodeValue();
    			isOrderRequired = inOrder.toLowerCase().equals("yes") || inOrder.toLowerCase().equals("true");
    			
    			if( isOrderSubtree && ( dpDefList.getLength() >= m+1 ) && ( dynFactsList.getLength() >= n+1 ) ) {    
			    
    			    /*
    			     * MODEL: nextCallInSubtree
    			     * 
    			     * If two nodes match and "nextCallInSubtree" is set to true then we check the
    			     * following nodes from the dynamic facts if they match the next node from
    			     * our dpDefList as long as they are called in the subtree of the first node
    			     * 
    			     * We check if the node is in the subtree by checking the "callDepth" of the 
    			     * nodes. The callDepth has to be larger than from our first node. Otherwise
    			     * we are not in the subtree anymore.
    			     * 
    			     */
			    if ( debug ) System.out.println("# doMatch(), nextCallInSubtree is specified #");
    			    
    			    boolean inSubtree = true;
    			    String currentNodeDepth = dynFactsList.item(n).getAttributes().getNamedItem("callDepth").getNodeValue();
    			    int currentNodeCallDepth = Integer.parseInt(currentNodeDepth);

    			    for (int q=n+1; q<dynFactsList.getLength() && inSubtree; q++ ) {        			
    				String nextNode   = dynFactsList.item(q).getAttributes().getNamedItem("callDepth").getNodeValue();
    				int nextNodeCallDepth = Integer.parseInt(nextNode);

    				if (nextNodeCallDepth > currentNodeCallDepth ){
    				    if ( debug )
    					System.out.println(q + "| if, currentNodeDepth < nextNodeDepth = " + currentNodeDepth + "<" + nextNodeCallDepth);

    				    Node dynFactsListNodeNext  = dynFactsList.item(q);
    				    Node dpDefListNodeNext     = dpDefList.item(m+1);
    				    if ( doMatch(dpDefListNodeNext, dynFactsListNodeNext) ) {
    					if ( debug ) {
    					    System.out.println(q + "| if, Found match " + dynFactsListNodeNext.getAttributes().getNamedItem("orderNumber").getNodeValue());
    					    System.out.println(q + "| if, Found match in Subtree at position = " + nextNodeCallDepth);
    					}
    					
    					// Add matching node to datastructure
    					list.add(dynFactsListNode);
    				    } else {
    					if ( debug ) System.out.println(q + "| if, Found no match ");
    				    }
    				    
    				} else {
    				    if ( debug ) {
    					System.out.println(q + "| else, currentNodeDepth < nextNodeDepth = " + currentNodeDepth + "<" 
    						+ nextNodeCallDepth);
    					System.out.println(q + "| else, We are leaving the Subtree");
    				    }
    				    inSubtree = false;
    				}
    			    }
  		    	} else if ( isOrderRequired && ( dpDefList.getLength() >= m+1 ) && ( dynFactsList.getLength() >= n+1 ) ) {
  		    	    
    		    	    /*
    			     * MODEL: nextCallInOrder == true
    			     * 
    			     * If two nodes match and "nextCallInOrder" is set to true then we check if
    			     * the next node from the dpDefList is found in the following nodes of the
    			     * dynamic facts
    			     * 
    			     */ 
  		    	    Node dpDefListNodeNext = null;
    			    try {
    				dpDefListNodeNext = dpDefList.item(m+1);
    			    } catch (RuntimeException e) {
    				System.out.println("The XML Design Pattern definition is not correct. File=" + designPatternDef);
    				e.printStackTrace();
    			    }
    			    
    			   for (int q=n+1; q<dynFactsList.getLength(); q++ ) {
    				Node dynFactsListNodeNext  = dynFactsList.item(q);
    				if ( doMatch(dpDefListNodeNext,dynFactsListNodeNext) ) {
    				    if ( debug ) System.out.println("# doMatch(dpDefListNode,dynFactsListNode) for loop " + q + " orderNumber=" 
    					    + dynFactsListNodeNext.getAttributes().getNamedItem("orderNumber").getNodeValue());
    				    list.add(dynFactsListNode);
    				}			
    			    }
    		    	} else {
    			    /**
    			     * MODEL: nextCallInOrder == false
    			     * 
    			     * If two nodes match and the order of the next method call is
    			     * not required than we don't need to check anything else and
    			     * can add the found dynamic facts node to our datastructure
    			     * 
    			     */ 
    		    	    
    		    	    if ( debug ) { 
    		    		System.out.println("# doMatch(), but no order specified #" 
    		    		    + dynFactsListNode.getAttributes().getNamedItem("orderNumber").getNodeValue());
    		    	    }
    		    	    list.add(dynFactsListNode);
    		    	}
    		    }
    		}
    	    }
    	    
    	    boolean isPattern = checkIfCandidateInstanceIsAPattern(storeMatchedFacts);
    	    candInstancesList.get(j).setIsPattern(isPattern);
    	    
    	    if ( printDatastructure ) {
    		System.out.println("storeMatchedFacts.length=" + storeMatchedFacts.length);
    		for (int x=0; x<storeMatchedFacts.length; x++) {
    		    if ( !storeMatchedFacts[x].isEmpty() ) {
    			for (int y=0; y<storeMatchedFacts[x].size(); y++){
    			    NamedNodeMap matchMap = ((Node)storeMatchedFacts[x].get(y)).getAttributes();
    			    if ( this.printDatastructure ) {
        			    System.out.println("");
            			    System.out.println(x + " " + y + "| className:      " + matchMap.getNamedItem("className"));
            			    System.out.println(x + " " + y + "| calledByClass:  " + matchMap.getNamedItem("calledByClass"));
        			    System.out.println(x + " " + y + "| thisObject:     " + matchMap.getNamedItem("thisObject"));
            			    System.out.println(x + " " + y + "| calledByObject: " + matchMap.getNamedItem("calledByObject"));
            			    System.out.println(x + " " + y + "| orderNumber:    " + matchMap.getNamedItem("orderNumber"));
            			    System.out.println(x + " " + y + "| methodName:     " + matchMap.getNamedItem("methodName"));
        			    System.out.println(x + " " + y + "| calledByMethod: " + matchMap.getNamedItem("calledByMethod"));
            			    System.out.println(x + " " + y + "| args:           " + matchMap.getNamedItem("args"));
            			    System.out.println(x + " " + y + "| callDepth:      " + matchMap.getNamedItem("callDepth"));
    			    }
    			}
    		    } else {
    			System.out.println(x + "  | storeMatchedFacts[x].isEmpty() ");
    		    }
    		}
    	    }

    	    // Store LinkedList[] storeMatchedFact with each Candidate Instance
    	    candInstancesList.get(j).setMatchedFactsDatastructure(storeMatchedFacts);

    	} // End of Loop candInstancesList.size()
    }
    
    
    
    
    /**
     * Checks if two given nodes match with the following attributes:
     *  -className
     *  -calledByClass
     *  -args
     * 
     * @param dpDefNode dynamic definition Node
     * @param dynFactsNode dynamic facts Node
     * 
     * @return true if Nodes match, false otherwise
     */
    private boolean doMatch(Node dpDefNode, Node dynFactsNode) {

	if (dpDefNode == null || dynFactsNode == null ){
	    return false;
	}
	
	boolean classNameMatch      = false;
	boolean methodNameMatch     = false;
	boolean thisObjectMatch     = true;		// this is tested in a separat iteration
	boolean argsMatch           = false;
	boolean calledByClassMatch  = false;
	boolean calledByMethodMatch = false;
	boolean calledByObjectMatch = true;		// this is tested in a separat iteration
	
	String dpDef_className      = dpDefNode.getAttributes().getNamedItem("className").getNodeValue();
	String dpDef_methodName     = dpDefNode.getAttributes().getNamedItem("methodName").getNodeValue();
	String dpDef_calledByClass  = dpDefNode.getAttributes().getNamedItem("calledByClass").getNodeValue();
	String dpDef_calledByMethod = dpDefNode.getAttributes().getNamedItem("calledByMethod").getNodeValue(); 
	String dpDef_calledByObject = dpDefNode.getAttributes().getNamedItem("calledByObject").getNodeValue(); 
	String dpDef_thisObject     = dpDefNode.getAttributes().getNamedItem("thisObject").getNodeValue();
	String dpDef_args           = dpDefNode.getAttributes().getNamedItem("args").getNodeValue();

	String orderNumber             = dynFactsNode.getAttributes().getNamedItem("orderNumber").getNodeValue();
	String dynFacts_className      = dynFactsNode.getAttributes().getNamedItem("className").getNodeValue();
	String dynFacts_methodName     = dynFactsNode.getAttributes().getNamedItem("methodName").getNodeValue();	
	String dynFacts_calledByClass  = dynFactsNode.getAttributes().getNamedItem("calledByClass").getNodeValue();
	String dynFacts_calledByMethod = dynFactsNode.getAttributes().getNamedItem("calledByMethod").getNodeValue();
	String dynFacts_calledByObject = dynFactsNode.getAttributes().getNamedItem("calledByObject").getNodeValue();
	String dynFacts_thisObject     = dynFactsNode.getAttributes().getNamedItem("thisObject").getNodeValue();
	String dynFacts_args           = dynFactsNode.getAttributes().getNamedItem("args").getNodeValue();
	dynFacts_args = dynFacts_args.replaceAll("\\.", "/");
	
	if ( dpDef_className.equals("") ) {
	    classNameMatch = true;
	} else if ( dynFacts_className.contains(dpDef_className) ) {
	    if ( dynFacts_className.length()== dpDef_className.length())
		classNameMatch = true;
	}
	
	if ( dpDef_methodName.equals("") ) {
	    methodNameMatch = true;
	} else if ( dynFacts_methodName.contains(dpDef_methodName) ) {
	    if ( dynFacts_methodName.length()== dpDef_methodName.length())
		methodNameMatch = true;
	}

	if ( dpDef_calledByClass.equals("") ) {
	    calledByClassMatch = true;
	} else if ( dynFacts_calledByClass.contains(dpDef_calledByClass) ) {
	    if ( dynFacts_calledByClass.length()== dpDef_calledByClass.length())
		calledByClassMatch = true;
	}
	
	if ( dpDef_calledByMethod.equals("") ) {
	    calledByMethodMatch = true;
	} else if ( dynFacts_calledByMethod.contains(dpDef_calledByMethod) ) {
	    if ( dynFacts_calledByMethod.length()== dpDef_calledByMethod.length())
		calledByMethodMatch = true;
	}
	
	if ( dpDef_calledByObject.equals("") ) {
	    calledByObjectMatch = true;
	} else if ( dynFacts_calledByObject.contains(dpDef_calledByObject) ) {
	    if ( dynFacts_calledByObject.length()== dpDef_calledByObject.length())
		calledByObjectMatch = true;
	}

	dpDef_thisObject = dpDef_thisObject + "@";
	if ( dpDef_thisObject.equals("@") ) {
	    thisObjectMatch = true;
	} else if ( dynFacts_thisObject.contains(dpDef_thisObject) ) {
	    thisObjectMatch = true;
	}
	
	dpDef_args = "|" + dpDef_args + "@";
	if ( dpDef_args.equals("|@") ) {
	    argsMatch = true;
	} else if ( dynFacts_args.contains(dpDef_args) ) {
	    argsMatch = true;
	}
	
	if (   	classNameMatch && methodNameMatch && calledByClassMatch && calledByMethodMatch && 
		thisObjectMatch && calledByObjectMatch && argsMatch 
		) {
	    
	    if ( debug ) {
		System.out.println("1 Match: def " + dpDef_className     + " | " + dpDef_calledByClass);
		System.out.println("1 Match: dyn " + dynFacts_className  + " | " + dynFacts_calledByClass );
		System.out.println("--------------------------------------------------------------------------------------------------");
		System.out.println("2 Match: def " + dpDef_methodName    + " | " + dpDef_calledByMethod);
		System.out.println("2 Match: dyn " + dynFacts_methodName + " | " + dynFacts_calledByMethod );
		System.out.println("--------------------------------------------------------------------------------------------------");
		System.out.println("3 Match: def " + dpDef_args          + " | " + dpDef_thisObject);
		System.out.println("3 Match: dyn " + dynFacts_args       + " | " + dynFacts_thisObject);
		System.out.println("--------------------------------------------------------------------------------------------------");
		System.out.println("4 Match: def " + dpDef_thisObject);
		System.out.println("4 Match: dyn " + dynFacts_thisObject);
		System.out.println("- Match: dyn orderNumber = " + orderNumber);
		System.out.println("\n\n");
	    }
	    return true;
	}
	return false;
    }
  
    
    
    /* (non-Javadoc)
     * @see ca.yorku.cse.designpatterns.ValidatorInterface#validateObjects(java.util.LinkedList, org.w3c.dom.NodeList)
     */
    public void validateObjects(LinkedList<CandidateInstance> candidateInstancesList, NodeList designPatternDefinitionList) {
	this.candInstancesList = candidateInstancesList;
	this.dpDefList         = designPatternDefinitionList;
	
	/**
	 * For all candidate instances that are currently recorded as a
	 * design pattern get the storedMatchedFacts datastructure 
	 * and do the following.
	 * 
	 * Loop through the design pattern definition list and check all 
	 * conditions for the attributes "thisObject" and "calledByObject"
	 */
	for(int i=0; i<candInstancesList.size(); i++ ){
	    CandidateInstance caIn = candInstancesList.get(i);
	    if ( caIn.isPattern() ) {
		LinkedList[] matchedFacts = caIn.getMatchedFactsDatastructure();
		
		for (int j=0; j<dpDefList.getLength(); j++ ) {
		    boolean resultEqual    = true;
		    boolean resultNotEqual = true;
		    
		    String defThisObject     = dpDefList.item(j).getAttributes().getNamedItem("thisObject").getNodeValue();
		    String defCalledByObject = dpDefList.item(j).getAttributes().getNamedItem("calledByObject").getNodeValue();
		    
		    // since Java 1.6 
		    // boolean isSetDefThisObject     = !defThisObject.toLowerCase().isEmpty();
		    // boolean isSetDefCalledByObject = !defCalledByObject.toLowerCase().isEmpty();
		    boolean isSetDefThisObject        = !(defThisObject.toLowerCase().length() == 0);
		    boolean isSetDefCalledByObject    = !(defCalledByObject.toLowerCase().length() == 0);
		    
		    /**
		     * Check all attributes that are set in the following part of the
		     * MatchedFacts datastructure.
		     */
		    if ( isSetDefThisObject || isSetDefCalledByObject ) {
			if ( print_objects ) System.out.println("dpDefList.getLength()=" + dpDefList.getLength() + " matchedFacts[j].size()=" + matchedFacts[j].size());
			for ( int k=0; k<matchedFacts[j].size(); k++) {
			    String currentThisObj     = ((Node)matchedFacts[j].get(k)).getAttributes().getNamedItem("thisObject").getNodeValue();
			    String currentCalledByObj = ((Node)matchedFacts[j].get(k)).getAttributes().getNamedItem("calledByObject").getNodeValue();

			    /**
			     * Loop over all nodes in the next position of the dpDefList and compare it to the 
			     * values that are set at the current position.
			     * 
			     * Then move on to the next position in the dpDefList.
			     */
			    for (int jj=j+1; jj<dpDefList.getLength() && ( j+1<dpDefList.getLength() ); jj++){
				String defNextThisObject     = dpDefList.item(jj).getAttributes().getNamedItem("thisObject").getNodeValue();
				String defNextCalledByObject = dpDefList.item(jj).getAttributes().getNamedItem("calledByObject").getNodeValue();


				/**
				 * Compare all objects from the storedMatchedFacts with the next
				 * occurrence of this object in the dpDefList
				 *
				 * LinkedList[]        [obj1]      []      [obj1]    [obj1] ...  []
				 *                       |         |         |         |         |
				 * LinkedList<Node>    [id1]      [id2]     [id1]     [id1]     [id2]
				 *                     [id3]      [id5]     [id7]     [id7]
				 *                     [id5]                          [id3]
				 *                     [id7]                          [id8]
				 *             
				 * In this example 2 different objects fullfil the requirements of obj1.
				 * These are the objects with the id's: id1 and id7
				 * In all three columns where an object has to match these two match.                    
				 *                     
				 */
				for (int kk=0; kk<matchedFacts[jj].size(); kk++) {
				    resultEqual    = true;
				    resultNotEqual = true;
				    if ( print_objects ) System.out.println("\n for loop kk, i,j,k,jj: " + i + "," + j + "," + k + "," + jj + "," + kk);
	    
				    String nextThisObj     = ((Node)matchedFacts[jj].get(kk)).getAttributes().getNamedItem("thisObject").getNodeValue();
				    String nextCalledByObj = ((Node)matchedFacts[jj].get(kk)).getAttributes().getNamedItem("calledByObject").getNodeValue();
				    
				    /**
				     * Check objects that have to be identical 
				     */
				    if ( defThisObject.equals(defNextThisObject) && !defThisObject.equals("") && !defNextThisObject.equals("") ) {
					resultEqual = resultEqual && currentThisObj.equals(nextThisObj);
					if ( print_objects ) {
						System.out.println(" EQUALS: defThisObject.equals(defNextThisObject) result=" + resultEqual);
						System.out.println(" def: defThisObject     =" + defThisObject );
						System.out.println(" def: defNextThisObject =" + defNextThisObject );
						System.out.println(" dyn: currentThisObj =" + currentThisObj);
						System.out.println(" dyn: nextThisObj    =" + nextThisObj);
					}
				    }
				    if ( defThisObject.equals(defNextCalledByObject) && !defThisObject.equals("") && !defNextCalledByObject.equals("") ) {
					resultEqual = resultEqual && currentThisObj.equals(nextCalledByObj);
					if ( print_objects ) {
					    System.out.println(" EQUALS: defThisObject.equals(defNextCalledByObject) result=" + resultEqual);	
					    System.out.println(" def: defThisObject         =" + defThisObject);
					    System.out.println(" def: defNextCalledByObject =" + defNextCalledByObject ); 
					    System.out.println(" dyn: currentThisObj  =" + currentThisObj);
					    System.out.println(" dyn: nextCalledByObj =" + nextCalledByObj);
					}
				    }
				    if ( defCalledByObject.equals(defNextThisObject) && !defCalledByObject.equals("") && !defNextThisObject.equals("") ) {
					resultEqual = resultEqual && currentCalledByObj.equals(nextThisObj);
					if ( print_objects ) {
					    System.out.println(" EQUALS: defCalledByObject.equals(defNextThisObject) result=" + resultEqual);
					    System.out.println(" def: defCalledByObject  =" + defCalledByObject);
					    System.out.println(" def: defNextThisObject  =" + defNextThisObject);
					    System.out.println(" dyn: currentCalledByObj =" + currentCalledByObj);
					    System.out.println(" dyn: nextThisObj        =" + nextThisObj);
					}
				    }
				    if ( defCalledByObject.equals(defNextCalledByObject) && !defCalledByObject.equals("") && !defNextCalledByObject.equals("") ) {
					resultEqual = resultEqual && currentCalledByObj.equals(nextCalledByObj);
					if ( print_objects ) {
					    System.out.println(" EQUALS: defCalledByObject.equals(defNextCalledByObject) result=" + resultEqual);
					    System.out.println(" def: defCalledByObject     =" + defCalledByObject );
					    System.out.println(" def: defNextCalledByObject =" + defNextCalledByObject );
					    System.out.println(" dyn: currentCalledByObj =" + currentCalledByObj);
					    System.out.println(" dyn: nextCalledByObj    =" + nextCalledByObj);
					}
				    }
				    
				    /**
				     * Check objects that have to be different 
				     */
				    if ( !defThisObject.equals(defNextThisObject) && !defThisObject.equals("") && !defNextThisObject.equals("") ) {
					resultNotEqual = resultNotEqual && !currentThisObj.equals(nextThisObj);
					if ( print_objects ) {
					    System.out.println(" NOT EQUALS: !defThisObject.equals(defNextThisObject) result=" + resultNotEqual);
					    System.out.println(" def: defThisObject     =" + defThisObject );
					    System.out.println(" def: defNextThisObject =" + defNextThisObject );
					    System.out.println(" dyn: currentThisObj =" + currentThisObj);
					    System.out.println(" dyn: nextThisObj    =" + nextThisObj);
					}					
				    }
				    if ( !defThisObject.equals(defNextCalledByObject) && !defThisObject.equals("") && !defNextCalledByObject.equals("") ) {
					resultNotEqual = resultNotEqual && !currentThisObj.equals(nextCalledByObj);
					if ( print_objects ) {
					    System.out.println(" NOT EQUALS: !defThisObject.equals(defNextCalledByObject) result=" + resultNotEqual);	
					    System.out.println(" def: defThisObject         =" + defThisObject);
					    System.out.println(" def: defNextCalledByObject =" + defNextCalledByObject ); 
					    System.out.println(" dyn: currentThisObj  =" + currentThisObj);
					    System.out.println(" dyn: nextCalledByObj =" + nextCalledByObj);
					}
				    }
				    if ( !defCalledByObject.equals(defNextThisObject) && !defCalledByObject.equals("") && !defNextThisObject.equals("") ) {
					resultNotEqual = resultNotEqual && !currentCalledByObj.equals(nextThisObj);
					if ( print_objects ) {
					    System.out.println(" NOT EQUALS: !defCalledByObject.equals(defNextThisObject) result=" + resultNotEqual);
					    System.out.println(" def: defCalledByObject  =" + defCalledByObject);
					    System.out.println(" def: defNextThisObject  =" + defNextThisObject);
					    System.out.println(" dyn: currentCalledByObj =" + currentCalledByObj);
					    System.out.println(" dyn: nextThisObj        =" + nextThisObj);
					}
				    }
				    if ( !defCalledByObject.equals(defNextCalledByObject) && !defCalledByObject.equals("") && !defNextCalledByObject.equals("") ) {
					resultNotEqual = resultNotEqual && !currentCalledByObj.equals(nextCalledByObj);
					if ( print_objects ) {
					    System.out.println(" NOT EQUALS: !defCalledByObject.equals(defNextCalledByObject) result=" + resultNotEqual);
					    System.out.println(" def: defCalledByObject     =" + defCalledByObject );
					    System.out.println(" def: defNextCalledByObject =" + defNextCalledByObject );
					    System.out.println(" dyn: currentCalledByObj =" + currentCalledByObj);
					    System.out.println(" dyn: nextCalledByObj    =" + nextCalledByObj);
					}
				    }
				    				    
				    if ( resultEqual && resultNotEqual ) {
					if ( print_objects ) System.out.println(" BOTH RESULTS ARE TRUE | i,j,k,jj,kk: " + i + "," + j + "," + k + "," + jj + "," + kk);
				    } else {
					/**
					 * Remove node from matchedFacts datastructure that does not match
					 * the additional conditions 
					 */
					if ( print_objects ) {
					    System.out.println(" AT LEAST ONE OF THE RESULTS IS FALSE | i,j,k,jj,kk: " + i + "," + j + "," + k + "," + jj + "," + kk);
					    System.out.println("     Removing node from matchedFacts[j].remove(k) j,k=" + j + "," + k );
					    System.out.println("     Remove: " + ((Node)matchedFacts[j].get(k)).getAttributes().getNamedItem("orderNumber"));
					}
					
					// only remove when facts are not empty
					if ( !matchedFacts[j].isEmpty() ) 
					    matchedFacts[j].remove(k);
					
					// make sure not to violate the list boundaries
					if ( k > 0 ) {
					    k = k-1;
					} else if ( k == 0) {
					    k = 0;
					}					
				    }
				}
			    }
			}
		    }
		}
	    }
	}
    }

    

    
    /**
     * Loops through the datastructure and checks of each fiels contains a no
     * 
     * @param storeMatchedFacts
     * @return true/false if Candidate Instance is a pattern
     */
    private boolean checkIfCandidateInstanceIsAPattern(LinkedList[] storeMatchedFacts) {
	boolean ret = true;
	
	if ( debug ) System.out.println("# checkIfCandidateInstanceIsAPattern");
	for (int i=0; i<storeMatchedFacts.length; i++){
	    if ( !storeMatchedFacts[i].isEmpty() ) {
		ret = ret && true;
		if ( debug ) System.out.println(i + "| True, 1st node exists: " + ((Node)storeMatchedFacts[i].getFirst()).getAttributes().getNamedItem("orderNumber").getNodeValue());
	    } else {
		ret = false;
		if ( debug ) System.out.println(i + "| False");
	    }
	}
	if ( debug ) System.out.println("# checkIfCandidateInstanceIsAPattern Result=" + ret + "\n");
	return ret;
    }

    
    
    /* (non-Javadoc)
     * @see ca.yorku.cse.designpatterns.ValidatorInterface#getCandidateInstancesList()
     */
    public LinkedList<CandidateInstance> getCandidateInstancesList() {
	return this.candInstancesList;
    }


    /* (non-Javadoc)
     * @see ca.yorku.cse.designpatterns.ValidatorInterface#getDynamicDefinitionList()
     */
    public NodeList getDynamicDefinitionList() {
	return this.dpDefList;
    }
    
    
}

