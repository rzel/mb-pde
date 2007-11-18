package ca.yorku.cse.designpatterns;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Marcel Birkner
 *
 */
public class DynamicFactsProcessorImplementation {

	protected String dynamicFactsFileName = "";
	static TreeNode root = null;
	static Node testNode = null;
	static NodeList dynFactsList = null;
	protected boolean debug = false;
	
	
	/**
	 * Constructor
	 * 
	 * @param dynamicFactsFileName
	 */
	public DynamicFactsProcessorImplementation( String dynamicFactsFileName, boolean debug, boolean timing ){
		this.debug = debug;
		this.dynamicFactsFileName = dynamicFactsFileName;
		Document dynFactsDoc = DynamicFactsProcessor.getDynamicFacts(dynamicFactsFileName, debug);
		dynFactsList = dynFactsDoc.getElementsByTagName("entry");
		createDynamicFactsTree(dynFactsList, timing);
	}
	
	
	@SuppressWarnings("unchecked")
	public LinkedList<Node> firstLevel(Node searchNode){
		LinkedList<Node> list = new LinkedList<Node>();		
		
		String className = searchNode.getAttributes().getNamedItem("className").getNodeValue();
		if ( className == "" ) className = "<empty>";
		TreeNode firstLevelNode = null;
		HashMap<Integer, TreeNode> levelOneHashMap = root.getReferenceToHashMap();
		if ( className == "<empty>") {
			// Take all lists from all TreeLeaf objects in the hash map
			Collection<TreeNode> tn = levelOneHashMap.values();
			for (Iterator iter = tn.iterator(); iter.hasNext();) {
				firstLevelNode = (TreeNode) iter.next();
				list.addAll( secondLevel(firstLevelNode, searchNode) );
			}
		} else if( levelOneHashMap.containsKey( className.hashCode() ) ){
			firstLevelNode = (TreeNode) levelOneHashMap.get( className.hashCode() );	
			list.addAll( secondLevel(firstLevelNode, searchNode) );
		} else {
			if (debug) print("1st level - !containsKey: " + className.hashCode() );
		}	
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private LinkedList<Node> secondLevel(TreeNode firstLevelNode, Node searchNode){
		LinkedList<Node> list = new LinkedList<Node>();	
		
		String calledByClass = searchNode.getAttributes().getNamedItem("calledByClass").getNodeValue();
		if ( calledByClass == "" ) calledByClass = "<empty>";
		TreeNode secondLevelNode = null;
		HashMap<Integer, TreeNode> levelTwoHashMap = firstLevelNode.getReferenceToHashMap();
		if ( calledByClass == "<empty>") {
			// Take all lists from all TreeLeaf objects in the hash map
			Collection<TreeNode> tn = levelTwoHashMap.values();
			for (Iterator iter = tn.iterator(); iter.hasNext();) {
				secondLevelNode = (TreeNode) iter.next();
				list.addAll( thirdLevel(secondLevelNode, searchNode) );
			}
		} else if( levelTwoHashMap.containsKey( calledByClass.hashCode() ) ){
			secondLevelNode = (TreeNode) levelTwoHashMap.get( calledByClass.hashCode() );	
			list.addAll( thirdLevel(secondLevelNode, searchNode) );
		} else {
			if (debug) print("2nd level - !containsKey: " + calledByClass.hashCode() );
		}	
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private LinkedList<Node> thirdLevel(TreeNode secondLevelNode, Node searchNode){
		LinkedList<Node> list = new LinkedList<Node>();	
		
		String args = searchNode.getAttributes().getNamedItem("args").getNodeValue();
		if ( args == "" ) args = "<empty>";
		TreeLeaf thirdLevelNode = null;
		HashMap<Integer, TreeLeaf> levelThreeHashMap = secondLevelNode.getReferenceToHashMap();
		if ( args == "<empty>") {
			// Take all lists from all TreeLeaf objects in the hash map
			Collection<TreeLeaf> tl = levelThreeHashMap.values();
			for (Iterator iter = tl.iterator(); iter.hasNext();) {
				TreeLeaf leaf = (TreeLeaf) iter.next();
				list.addAll( leaf.getList() );	
				if (debug) print("  list.addAll(nodeList) = " + leaf.getList().size() );
			}
		} else if( levelThreeHashMap.containsKey( args.hashCode() ) ){
			thirdLevelNode = (TreeLeaf) levelThreeHashMap.get( args.hashCode() );	
			LinkedList<Node> nodeList = thirdLevelNode.getList();
			list.addAll(nodeList);
			if (debug)  print("  list.addAll(nodeList) = " + nodeList.size());
		} else {
			if (debug) print("3rd level - !containsKey: " + args.hashCode() );
		}	
		return list;
	}
	
	
	
//	
//	@SuppressWarnings({ "unchecked" })
//	private LinkedList<Node> getListForNode(Node searchNode) {
//		LinkedList<Node> list = new LinkedList<Node>();
//		
//		String classname = searchNode.getAttributes().getNamedItem("className").getNodeValue();
//		TreeNode firstLevelNode = null;
//		HashMap<Integer, TreeNode> levelOneHashMap = root.getReferenceToHashMap();
//		if( levelOneHashMap.containsKey( classname.hashCode() ) ){
//			print("1st level - containsKey: " + classname.hashCode() );
//			firstLevelNode = (TreeNode) levelOneHashMap.get( classname.hashCode() );
//			print( firstLevelNode.toString() );
//			
//			String calledByClass = searchNode.getAttributes().getNamedItem("calledByClass").getNodeValue();
//			TreeNode secondLevelNode = null;
//			HashMap<Integer, TreeNode> levelTwoHashMap = firstLevelNode.getReferenceToHashMap();
//			if( levelTwoHashMap.containsKey( calledByClass.hashCode() ) ){
//				print("2nd level - containsKey: " + calledByClass.hashCode() );
//				secondLevelNode = (TreeNode) levelTwoHashMap.get( calledByClass.hashCode() );
//				print( secondLevelNode.toString() );
//				
//				String args = searchNode.getAttributes().getNamedItem("args").getNodeValue();
//				TreeLeaf thirdLevelNode = null;
//				HashMap<Integer, TreeLeaf> levelThreeHashMap = secondLevelNode.getReferenceToHashMap();
//				if ( args == "<empty>") {
//					// Take all lists from all TreeLeaf objects in the hash map
//					Collection<TreeLeaf> tl = levelThreeHashMap.values();
//					for (Iterator iter = tl.iterator(); iter.hasNext();) {
//						TreeLeaf element = (TreeLeaf) iter.next();
//						list.addAll( element.getList() );				
//					}
//				} else if( levelThreeHashMap.containsKey( args.hashCode() ) ){
//					print("3rd level - containsKey: " + args.hashCode() );
//					thirdLevelNode = (TreeLeaf) levelThreeHashMap.get( args.hashCode() );					
//					print( thirdLevelNode.toString() );
//					
//					/**
//					 * Add all nodes from the thirdLevelNodeList to a list
//					 */
//					LinkedList<Node> nodeList = thirdLevelNode.getList();
//					list.addAll(nodeList);
//				} else {
//					print("3rd level - !containsKey: " + args.hashCode() );
//				}	
//			} else {
//				print("2nd level - !containsKey: " + calledByClass.hashCode() );
//			}			
//		} else {
//			print("1st level - !containsKey: " + classname.hashCode() );
//		}
//
//		
//		return list;
//	}
//	

	@SuppressWarnings({ "unchecked", "unchecked" })
	public static boolean createDynamicFactsTree(NodeList dynFactsList, boolean time){
		boolean result = false;
		long startTime = 0;
		
		// Create starting point for TreeImplementation
		root = new TreeNode("root", "root");
		HashMap rootLevelHashMap = new HashMap(800);
		root.setReferenceToHashMap( rootLevelHashMap );
		
		int lev1HM=0,lev2HM=0,lev3HM=0;
		int lev1HMcontains=0,lev2HMcontains=0,lev3HMcontains=0;
		
		
		if (time) {
			startTime = System.currentTimeMillis();
		}
		
		for (int i = 0; i < dynFactsList.getLength(); i++) {
			Node node = dynFactsList.item(i);
			
			if ( i==12 ) {
				testNode = node; 
			}
			
			String classname = node.getAttributes().getNamedItem("className").getNodeValue();
			//print(i + " 1st level - classname=(" + classname + ") classnameHash=(" + classname.hashCode()+")");
			TreeNode firstLevelNode = null;
			HashMap<Integer, TreeNode> levelOneHashMap = root.getReferenceToHashMap();
			if( levelOneHashMap.containsKey( classname.hashCode() ) ){
				//print("1st level - containsKey: " + classname.hashCode() );
				firstLevelNode = (TreeNode) levelOneHashMap.get( classname.hashCode() );
				//print( firstLevelNode.toString() );
				lev1HMcontains++;
			} else {
				//print("1st level - !containsKey insert key: " + classname.hashCode() );
				// Create new TreeNode with classname and insert into HashMap
				firstLevelNode = new TreeNode("className", classname);
				HashMap firstLevelHashMap = new HashMap();
				firstLevelNode.setReferenceToHashMap(firstLevelHashMap);
				levelOneHashMap.put( classname.hashCode(), firstLevelNode);
				//print( firstLevelNode.toString() );
				lev1HM++;
			}
			//print("");
			
			TreeNode secondLevelNode = null;
			HashMap<Integer, TreeNode> levelTwoHashMap = firstLevelNode.getReferenceToHashMap();
			String calledByClass = node.getAttributes().getNamedItem("calledByClass").getNodeValue();
			//print("2nd level - calledByClass=(" + calledByClass + ") calledByClassHash=(" + calledByClass.hashCode()+")");
			if( levelTwoHashMap.containsKey( calledByClass.hashCode() )) { 
				//print("2nd level - containsKey: " + calledByClass.hashCode());
				secondLevelNode = (TreeNode) levelTwoHashMap.get( calledByClass.hashCode() );
				//print( secondLevelNode.toString() );
				lev2HMcontains++;
			} else {
				//print("2nd level - !containsKey insert key: " + calledByClass.hashCode());
				// Create new TreeNode with calledByClass and insert into HashMap
				secondLevelNode = new TreeNode("calledByClass", calledByClass);
				HashMap secondLevelHashMap = new HashMap();
				secondLevelNode.setReferenceToHashMap(secondLevelHashMap);
				levelTwoHashMap.put( calledByClass.hashCode(), secondLevelNode);
				//print( secondLevelNode.toString() );
				lev2HM++;
			}			
			//print("");
			
			
			TreeLeaf thirdLevelNode = null;
			HashMap<Integer, TreeLeaf> levelThreeHashMap = secondLevelNode.getReferenceToHashMap();
			String args = node.getAttributes().getNamedItem("args").getNodeValue();
			//print("3rd level - args=(" + args + ") argsHash=(" + args.hashCode()+")");
			if( levelThreeHashMap.containsKey( args.hashCode() ) ){
				//print("3rd - containsKey: " + args.hashCode());
				thirdLevelNode = (TreeLeaf) levelThreeHashMap.get( args.hashCode() );
				//print( thirdLevelNode.toString() );
				lev3HMcontains++;
			} else {
				//print("3rd - !containsKey insert key: " + args.hashCode());
				// Create new TreeNode with args and insert into HashMap
				thirdLevelNode = new TreeLeaf("args", args);	
				HashMap thirdLevelHashMap = new HashMap();
				LinkedList<Node> list = new LinkedList<Node>();
				thirdLevelNode.setList(list);
				thirdLevelNode.setReferenceToHashMap(thirdLevelHashMap);
				levelThreeHashMap.put(args.hashCode(), thirdLevelNode);
				//print( thirdLevelNode.toString() );
				lev3HM++;
			}			
			//print("");
			
			//print(i + " Add node: " + node.getAttributes().getNamedItem("className").getNodeValue() + " to leaf node list. \n");
			LinkedList<Node> thirdLevelList = thirdLevelNode.getList();
			thirdLevelList.add( node );
		}	

		
//		if ( time ) { 
//			long endTime = System.currentTimeMillis();
//			print("Dynamic Facts - number of entries = " + dynFactsList.getLength());
//			print("StartTime:                          " + startTime);
//			print("EndTime:                            " + endTime);
//			print("CreateTree:                         " + (endTime-startTime)/1000  + " seconds");
//		}
		
		result = true;		
		return result;
	}
	

	private static void print(String message) {
		System.out.println(message);
	}
	
}
