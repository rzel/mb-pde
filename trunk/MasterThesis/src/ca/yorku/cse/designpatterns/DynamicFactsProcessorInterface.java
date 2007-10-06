package ca.yorku.cse.designpatterns;

import org.w3c.dom.Document;


/**
 * The dynamicFactsProcessor takes the file name of the dynamic
 * facts XML file, parses it and stores all Nodes in a Document. 
 * The next step involves some postprocessing: <br>
 * - add order number for each method call (incrementing)
 * - write transformed dynamic facts to a text file 
 * 
 * @author Marcel Birkner
 * @version 0.8
 * @since 27 June, 2007
 * 
 */
public interface DynamicFactsProcessorInterface {

    /**
     * Public method that returns the DOM document with
     * the dynamic facts.
     * 
     * @return Document with all dynamic facts
     */
    public abstract Document getDynamicFactsDocument();

    /**
     * Parses the Document object and transform and transforms
     * the list of dynamic method calls in valid XML format. 
     * We add a root node as starting point for the method calls. All
     * other nodes are children of the root.
     * 
     * @param document with all dynamic facts
     */
    public abstract void parseDocument(Document document);

}