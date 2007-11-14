package ca.yorku.cse.designpatterns;

import java.util.HashMap;

public class TreeNode {
	
	String splitAttribute = "";						// classname, calledByClass, args
	String attributeValue = "";						// actuall class names
	HashMap referenceToHashMap = null;
	
	public TreeNode(String splitAttribute, String attributeValue){
		this.splitAttribute = splitAttribute;
		this.attributeValue = attributeValue;		
	}

	public String getSplitAttribute() {
		return splitAttribute;
	}

	public void setSplitAttribute(String splitAttribute) {
		this.splitAttribute = splitAttribute;
	}

	public HashMap getReferenceToHashMap() {
		return referenceToHashMap;
	}

	public void setReferenceToHashMap(HashMap referenceToHashMap) {
		this.referenceToHashMap = referenceToHashMap;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	public String toString(){
		return "splitAttribute="+splitAttribute+"|attributeValue="+attributeValue+"|referenceToHashMap="+referenceToHashMap;
	}
	
}
