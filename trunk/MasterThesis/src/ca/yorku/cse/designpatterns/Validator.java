
package ca.yorku.cse.designpatterns;

import java.util.LinkedList;

import org.apache.log4j.Logger;
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
	
	// Log4J
	private static org.apache.log4j.Logger log = Logger.getLogger( Validator.class );

	
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
			DynamicDefinitionConverterInterface dpDef = new DynamicDefinitionConverter(designPatternDef, candInstancesList.get(j) );
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
							log.debug("Validator -> # doMatch(), nextCallInSubtree is specified #");

							boolean inSubtree = true;
							String currentNodeDepth = dynFactsList.item(n).getAttributes().getNamedItem("callDepth").getNodeValue();
							int currentNodeCallDepth = Integer.parseInt(currentNodeDepth);

							for (int q=n+1; q<dynFactsList.getLength() && inSubtree; q++ ) {        			
								String nextNode   = dynFactsList.item(q).getAttributes().getNamedItem("callDepth").getNodeValue();
								int nextNodeCallDepth = Integer.parseInt(nextNode);

								if (nextNodeCallDepth > currentNodeCallDepth ){
									log.debug("Validator -> " + q + "| if, currentNodeDepth < nextNodeDepth = " + currentNodeDepth + "<" + nextNodeCallDepth);

									Node dynFactsListNodeNext  = dynFactsList.item(q);
									Node dpDefListNodeNext     = dpDefList.item(m+1);
									if ( doMatch(dpDefListNodeNext, dynFactsListNodeNext) ) {
										log.debug("Validator -> " + q + "| if, Found match " + dynFactsListNodeNext.getAttributes().getNamedItem("orderNumber").getNodeValue());
										log.debug("Validator -> " + q + "| if, Found match in Subtree at position = " + nextNodeCallDepth);

										// Add matching node to datastructure
										list.add(dynFactsListNode);
									} else {
										log.debug("Validator -> " + q + "| if, Found no match ");
									}

								} else {
									log.debug("Validator -> " + q + "| else, currentNodeDepth < nextNodeDepth = " + currentNodeDepth + "<" 
												+ nextNodeCallDepth);
									log.debug("Validator -> " + q + "| else, We are leaving the Subtree");
									
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
								log.error("Validator -> The XML Design Pattern definition is not correct. File=" + designPatternDef);
								e.printStackTrace();
							}

							for (int q=n+1; q<dynFactsList.getLength(); q++ ) {
								Node dynFactsListNodeNext  = dynFactsList.item(q);
								if ( doMatch(dpDefListNodeNext,dynFactsListNodeNext) ) {
									if ( debug ) log.info("Validator -> # doMatch(dpDefListNode,dynFactsListNode) for loop " + q + " orderNumber=" 
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

							log.debug("Validator -> # doMatch(), but no order specified #" 
										+ dynFactsListNode.getAttributes().getNamedItem("orderNumber").getNodeValue());
							list.add(dynFactsListNode);
						}
					}
				}
			}

			boolean isPattern = checkIfCandidateInstanceIsAPattern(storeMatchedFacts);
			candInstancesList.get(j).setIsPattern(isPattern);

			if ( printDatastructure ) {
				log.info("storeMatchedFacts.length=" + storeMatchedFacts.length);
				for (int x=0; x<storeMatchedFacts.length; x++) {
					if ( !storeMatchedFacts[x].isEmpty() ) {
						for (int y=0; y<storeMatchedFacts[x].size(); y++){
							NamedNodeMap matchMap = ((Node)storeMatchedFacts[x].get(y)).getAttributes();
							if ( this.printDatastructure ) {
								log.info("Validator -> ");
								log.info("Validator -> " + x + " " + y + "| className:      " + matchMap.getNamedItem("className"));
								log.info("Validator -> " + x + " " + y + "| calledByClass:  " + matchMap.getNamedItem("calledByClass"));
								log.info("Validator -> " + x + " " + y + "| thisObject:     " + matchMap.getNamedItem("thisObject"));
								log.info("Validator -> " + x + " " + y + "| calledByObject: " + matchMap.getNamedItem("calledByObject"));
								log.info("Validator -> " + x + " " + y + "| orderNumber:    " + matchMap.getNamedItem("orderNumber"));
								log.info("Validator -> " + x + " " + y + "| methodName:     " + matchMap.getNamedItem("methodName"));
								log.info("Validator -> " + x + " " + y + "| calledByMethod: " + matchMap.getNamedItem("calledByMethod"));
								log.info("Validator -> " + x + " " + y + "| args:           " + matchMap.getNamedItem("args"));
								log.info("Validator -> " + x + " " + y + "| callDepth:      " + matchMap.getNamedItem("callDepth"));
							}
						}
					} else {
						log.info("Validator -> " + x + "  | storeMatchedFacts[x].isEmpty() ");
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
			//if ( dynFacts_className.length()== dpDef_className.length())
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
			//if ( dynFacts_calledByClass.length()== dpDef_calledByClass.length())
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
			//if ( dynFacts_calledByObject.length()== dpDef_calledByObject.length())
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
				thisObjectMatch && calledByObjectMatch && argsMatch ) 
		{
			log.debug("Validator -> 1 Match: def " + dpDef_className     + " | " + dpDef_calledByClass);
			log.debug("Validator -> 1 Match: dyn " + dynFacts_className  + " | " + dynFacts_calledByClass );
			log.debug("Validator -> --------------------------------------------------------------------------------------------------");
			log.debug("Validator -> 2 Match: def " + dpDef_methodName    + " | " + dpDef_calledByMethod);
			log.debug("Validator -> 2 Match: dyn " + dynFacts_methodName + " | " + dynFacts_calledByMethod );
			log.debug("Validator -> --------------------------------------------------------------------------------------------------");
			log.debug("Validator -> 3 Match: def " + dpDef_args          + " | " + dpDef_thisObject);
			log.debug("Validator -> 3 Match: dyn " + dynFacts_args       + " | " + dynFacts_thisObject);
			log.debug("Validator -> --------------------------------------------------------------------------------------------------");
			log.debug("Validator -> 4 Match: def " + dpDef_thisObject);
			log.debug("Validator -> 4 Match: dyn " + dynFacts_thisObject);
			log.debug("Validator -> - Match: dyn orderNumber = " + orderNumber);
			log.debug("\n\n");
			return true;
		}
		return false;
	}




	public LinkedList<CandidateInstance> validateTemporalRestriction(LinkedList<CandidateInstance> candidateInstancesList, NodeList designPatternDefinitionList, String dynamicFactsFileName) {
		log.info("Validator -> validateTemporalRestrictions");
		this.candInstancesList = candidateInstancesList;
		this.dpDefList         = designPatternDefinitionList;

		// Get dynamic facts Node list
		// Singleton
		Document dynFactsDoc = DynamicFactsProcessorListImplementation.getDynamicFacts(dynamicFactsFileName);
		NodeList dynFactsList = dynFactsDoc.getElementsByTagName("entry");


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
					
					// check nodes if they have to appear in the subtree
					String inSubtree = dpDefList.item(j).getAttributes().getNamedItem("nextCallInSubtree").getNodeValue();
					boolean isSubtreeRequired = inSubtree.toLowerCase().equals("yes") || inSubtree.toLowerCase().equals("true");
					
					// check nodes if order is required
					String inOrder  = dpDefList.item(j).getAttributes().getNamedItem("nextCallInOrder").getNodeValue();
					boolean isOrderRequired = inOrder.toLowerCase().equals("yes") || inOrder.toLowerCase().equals("true");
					if ( isOrderRequired || isSubtreeRequired ) {
						int minOrder = Integer.MAX_VALUE ;
						for (int k = 0; k < matchedFacts[j].size(); k++) {
							int node1OrderNumber = Integer.parseInt( ((Node)matchedFacts[j].get(k)).getAttributes().getNamedItem("orderNumber").getNodeValue() );
							if ( node1OrderNumber < minOrder ) {
								minOrder = node1OrderNumber;
							}
						}
						
						if ( matchedFacts.length < j+1 ) {
							// leave FOR loop
							break;
						}
						
						for (int k = 0; k < matchedFacts[j+1].size(); k++) {
							int node2OrderNumber = Integer.parseInt( ((Node)matchedFacts[j].get(k)).getAttributes().getNamedItem("orderNumber").getNodeValue() );
							if ( node2OrderNumber < minOrder ) {
								// Remove node 2 since it is not after node 1 in the dynamic facts
								matchedFacts[j].remove(k);
								k--;
							}								
						}
					}	
					
					// check nodes if they have to appear in the subtree
					if( isSubtreeRequired ) {
						int parentMinLevel = Integer.MAX_VALUE ;
						for (int k = 0; k < matchedFacts[j].size(); k++) {
							int parentLevel = Integer.parseInt( ((Node)matchedFacts[j].get(k)).getAttributes().getNamedItem("callDepth").getNodeValue() );
							if ( parentLevel < parentMinLevel ) {
								parentMinLevel = parentLevel;
							}
						}
						
						if ( matchedFacts.length < j+1 ) {
							// leave FOR loop
							break;
						}
						
						for (int k = 0; k < matchedFacts[j+1].size(); k++) {
							Node childNode = ((Node)matchedFacts[j+1].get(k));
							int childLevel = Integer.parseInt( ((Node)matchedFacts[j].get(k)).getAttributes().getNamedItem("callDepth").getNodeValue() );
							if ( childLevel < parentMinLevel ) {
								// Remove child node since it is not after node 1 in the dynamic facts
								matchedFacts[j].remove(k);
								k--;
							} else {
								for (int index = 0; index < matchedFacts[j].size(); index++) {
									Node parentNode = ((Node)matchedFacts[j].get(index));
									if ( !childIsInSubtree( dynFactsList, parentNode, childNode ) ) {
										matchedFacts[j].remove(k);
										k--;
									}
								}
							}
						}
					}	
				}
			}
		}	
		
		return candInstancesList;
	}
	

	private boolean childIsInSubtree(NodeList dynFactsList, Node parentNode, Node childNode) {
		boolean result = true;
		int indexParent = Integer.parseInt( parentNode.getAttributes().getNamedItem("orderNumber").getNodeValue() );
		int indexChild  = Integer.parseInt( childNode.getAttributes().getNamedItem("orderNumber").getNodeValue() );
		int levelParent = Integer.parseInt( parentNode.getAttributes().getNamedItem("callDepth").getNodeValue() );
		// int levelClient = Integer.parseInt( childNode.getAttributes().getNamedItem("callDepth").getNodeValue() );
		
		for( int i = indexParent; i < indexChild; i++ ){
			Node node = dynFactsList.item(i);
			int levelNode = Integer.parseInt( node.getAttributes().getNamedItem("callDepth").getNodeValue() );
			if( levelNode <= levelParent ) {
				return false;
			}
		}		
		return result;
	}
	

	/* (non-Javadoc)
	 * @see ca.yorku.cse.designpatterns.ValidatorInterface#validateObjects(java.util.LinkedList, org.w3c.dom.NodeList)
	 */
	public LinkedList<CandidateInstance> validateObjects(LinkedList<CandidateInstance> candidateInstancesList, NodeList designPatternDefinitionList) {
		this.candInstancesList = candidateInstancesList;
		this.dpDefList         = designPatternDefinitionList;

		/**
		 * For all candidate instances that are currently recorded as a
		 * design pattern get the storedMatchedFacts data structure 
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
					 * MatchedFacts data structure.
					 */
					if ( isSetDefThisObject || isSetDefCalledByObject ) {
						if ( print_objects ) log.info("Validator -> dpDefList.getLength()=" + dpDefList.getLength() + " matchedFacts[j].size()=" + matchedFacts[j].size());
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
								 * In this example 2 different objects comply to the requirements of obj1.
								 * These are the objects with the id's: id1 and id7
								 * In all three columns where an object has to match these two match.                    
								 *                     
								 */
								for (int kk=0; kk<matchedFacts[jj].size(); kk++) {
									resultEqual    = true;
									resultNotEqual = true;
									if ( print_objects ) log.info("\n for loop kk, i,j,k,jj: " + i + "," + j + "," + k + "," + jj + "," + kk);

									String nextThisObj     = ((Node)matchedFacts[jj].get(kk)).getAttributes().getNamedItem("thisObject").getNodeValue();
									String nextCalledByObj = ((Node)matchedFacts[jj].get(kk)).getAttributes().getNamedItem("calledByObject").getNodeValue();

									/**
									 * Check objects that have to be identical 
									 */
									if ( defThisObject.equals(defNextThisObject) && !defThisObject.equals("") && !defNextThisObject.equals("") ) {
										resultEqual = resultEqual && currentThisObj.equals(nextThisObj);
										if ( print_objects ) {
											log.info("Validator ->  EQUALS: defThisObject.equals(defNextThisObject) result=" + resultEqual);
											log.info("Validator ->  def: defThisObject     =" + defThisObject );
											log.info("Validator ->  def: defNextThisObject =" + defNextThisObject );
											log.info("Validator ->  dyn: currentThisObj =" + currentThisObj);
											log.info("Validator ->  dyn: nextThisObj    =" + nextThisObj);
										}
									}
									if ( defThisObject.equals(defNextCalledByObject) && !defThisObject.equals("") && !defNextCalledByObject.equals("") ) {
										resultEqual = resultEqual && currentThisObj.equals(nextCalledByObj);
										if ( print_objects ) {
											log.info("Validator ->  EQUALS: defThisObject.equals(defNextCalledByObject) result=" + resultEqual);	
											log.info("Validator ->  def: defThisObject         =" + defThisObject);
											log.info("Validator ->  def: defNextCalledByObject =" + defNextCalledByObject ); 
											log.info("Validator ->  dyn: currentThisObj  =" + currentThisObj);
											log.info("Validator ->  dyn: nextCalledByObj =" + nextCalledByObj);
										}
									}
									if ( defCalledByObject.equals(defNextThisObject) && !defCalledByObject.equals("") && !defNextThisObject.equals("") ) {
										resultEqual = resultEqual && currentCalledByObj.equals(nextThisObj);
										if ( print_objects ) {
											log.info("Validator ->  EQUALS: defCalledByObject.equals(defNextThisObject) result=" + resultEqual);
											log.info("Validator ->  def: defCalledByObject  =" + defCalledByObject);
											log.info("Validator ->  def: defNextThisObject  =" + defNextThisObject);
											log.info("Validator ->  dyn: currentCalledByObj =" + currentCalledByObj);
											log.info("Validator ->  dyn: nextThisObj        =" + nextThisObj);
										}
									}
									if ( defCalledByObject.equals(defNextCalledByObject) && !defCalledByObject.equals("") && !defNextCalledByObject.equals("") ) {
										resultEqual = resultEqual && currentCalledByObj.equals(nextCalledByObj);
										if ( print_objects ) {
											log.info("Validator ->  EQUALS: defCalledByObject.equals(defNextCalledByObject) result=" + resultEqual);
											log.info("Validator ->  def: defCalledByObject     =" + defCalledByObject );
											log.info("Validator ->  def: defNextCalledByObject =" + defNextCalledByObject );
											log.info("Validator ->  dyn: currentCalledByObj =" + currentCalledByObj);
											log.info("Validator ->  dyn: nextCalledByObj    =" + nextCalledByObj);
										}
									}

									/**
									 * Check objects that have to be different 
									 */
									if ( !defThisObject.equals(defNextThisObject) && !defThisObject.equals("") && !defNextThisObject.equals("") ) {
										resultNotEqual = resultNotEqual && !currentThisObj.equals(nextThisObj);
										if ( print_objects ) {
											log.info("Validator ->  NOT EQUALS: !defThisObject.equals(defNextThisObject) result=" + resultNotEqual);
											log.info("Validator ->  def: defThisObject     =" + defThisObject );
											log.info("Validator ->  def: defNextThisObject =" + defNextThisObject );
											log.info("Validator ->  dyn: currentThisObj =" + currentThisObj);
											log.info("Validator ->  dyn: nextThisObj    =" + nextThisObj);
										}					
									}
									if ( !defThisObject.equals(defNextCalledByObject) && !defThisObject.equals("") && !defNextCalledByObject.equals("") ) {
										resultNotEqual = resultNotEqual && !currentThisObj.equals(nextCalledByObj);
										if ( print_objects ) {
											log.info("Validator ->  NOT EQUALS: !defThisObject.equals(defNextCalledByObject) result=" + resultNotEqual);	
											log.info("Validator ->  def: defThisObject         =" + defThisObject);
											log.info("Validator ->  def: defNextCalledByObject =" + defNextCalledByObject ); 
											log.info("Validator ->  dyn: currentThisObj  =" + currentThisObj);
											log.info("Validator ->  dyn: nextCalledByObj =" + nextCalledByObj);
										}
									}
									if ( !defCalledByObject.equals(defNextThisObject) && !defCalledByObject.equals("") && !defNextThisObject.equals("") ) {
										resultNotEqual = resultNotEqual && !currentCalledByObj.equals(nextThisObj);
										if ( print_objects ) {
											log.info("Validator ->  NOT EQUALS: !defCalledByObject.equals(defNextThisObject) result=" + resultNotEqual);
											log.info("Validator ->  def: defCalledByObject  =" + defCalledByObject);
											log.info("Validator ->  def: defNextThisObject  =" + defNextThisObject);
											log.info("Validator ->  dyn: currentCalledByObj =" + currentCalledByObj);
											log.info("Validator ->  dyn: nextThisObj        =" + nextThisObj);
										}
									}
									if ( !defCalledByObject.equals(defNextCalledByObject) && !defCalledByObject.equals("") && !defNextCalledByObject.equals("") ) {
										resultNotEqual = resultNotEqual && !currentCalledByObj.equals(nextCalledByObj);
										if ( print_objects ) {
											log.info("Validator ->  NOT EQUALS: !defCalledByObject.equals(defNextCalledByObject) result=" + resultNotEqual);
											log.info("Validator ->  def: defCalledByObject     =" + defCalledByObject );
											log.info("Validator ->  def: defNextCalledByObject =" + defNextCalledByObject );
											log.info("Validator ->  dyn: currentCalledByObj =" + currentCalledByObj);
											log.info("Validator ->  dyn: nextCalledByObj    =" + nextCalledByObj);
										}
									}

									if ( resultEqual && resultNotEqual ) {
										if ( print_objects ) log.info("Validator ->  BOTH RESULTS ARE TRUE | i,j,k,jj,kk: " + i + "," + j + "," + k + "," + jj + "," + kk);
									} else {
										/**
										 * Remove node from matchedFacts datastructure that does not match
										 * the additional conditions 
										 */
										if ( print_objects ) {
											log.info("Validator ->  AT LEAST ONE OF THE RESULTS IS FALSE | i,j,k,jj,kk: " + i + "," + j + "," + k + "," + jj + "," + kk);
											log.info("Validator ->      Removing node from matchedFacts[j].remove(k) j,k=" + j + "," + k );
											log.info("Validator ->      Remove: " + ((Node)matchedFacts[j].get(k)).getAttributes().getNamedItem("orderNumber"));
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
		return candidateInstancesList;
	}




	/**
	 * Loops through the datastructure and checks of each fiels contains a no
	 * 
	 * @param storeMatchedFacts
	 * @return true/false if Candidate Instance is a pattern
	 */
	private boolean checkIfCandidateInstanceIsAPattern(LinkedList[] storeMatchedFacts) {
		boolean ret = true;

		log.debug("# checkIfCandidateInstanceIsAPattern");
		for (int i=0; i<storeMatchedFacts.length; i++){
			if ( !storeMatchedFacts[i].isEmpty() ) {
				ret = ret && true;
				log.debug("Validator -> " + i + "| True, 1st node exists: " + ((Node)storeMatchedFacts[i].getFirst()).getAttributes().getNamedItem("orderNumber").getNodeValue());
			} else {
				ret = false;
				log.debug("Validator -> " + i + "| False");
			}
		}
		log.debug("Validator -> # checkIfCandidateInstanceIsAPattern Result=" + ret + "\n");
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

