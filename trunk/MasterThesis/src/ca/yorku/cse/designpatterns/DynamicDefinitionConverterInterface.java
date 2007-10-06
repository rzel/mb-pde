package ca.yorku.cse.designpatterns;

import org.w3c.dom.Document;

/**
 * This class substitutes the role names of the design pattern
 * in the dynamic definition with the the class names 
 * from the candidate instance. This class takes the dynamic
 * definition of a design pattern as input and one candidate
 * instance object. It then exchanges the roles of the dynamic definition
 * with the real class names of the candidate instances. The 
 * dynamic definition has to be provided in XML format. The structure
 * has to match the DTD XML definition that is provided with this software.
 * 
 * @author Marcel Birkner
 * @version 0.8
 * @since 27 June, 2007
 */
public interface DynamicDefinitionConverterInterface {

    /**
     * Return the dynamic definition Document with the real class names
     * of the current candidate instance.
     * 
     * @return Document of the dynamic definitions that contains the real class names of the candidate instance
     */
    public abstract Document getDesignPatternDocument();

}