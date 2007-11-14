package ca.yorku.cse.designpatterns;

import java.util.LinkedList;

public class TreeLeaf extends TreeNode {
	
	LinkedList list = null;
	
	public TreeLeaf(String splitAttribute, String attributeValue) {
		super(splitAttribute, attributeValue);
	}

	public LinkedList getList() {
		return list;
	}

	public void setList(LinkedList list) {
		this.list = list;
	}
	
	public String toString(){
		return "splitAttribute="+splitAttribute+"|attributeValue="+attributeValue+"|referenceToHashMap="+referenceToHashMap;
	}
}
