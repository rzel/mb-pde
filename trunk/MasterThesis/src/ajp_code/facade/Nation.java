package ajp_code.facade;
import java.text.NumberFormat;
/**
 * @author  Marcel Birkner
 */
public class Nation {
    private char symbol;
    private String name;
    private String dialingPrefix;
    private String propertyFileName;
    private NumberFormat numberFormat;
    
    public Nation(String newName, char newSymbol, String newDialingPrefix,
        String newPropertyFileName, NumberFormat newNumberFormat) {
        name = newName;
        symbol = newSymbol;
        dialingPrefix = newDialingPrefix;
        propertyFileName = newPropertyFileName;
        numberFormat = newNumberFormat;
    }
    
    /**
     * @return  the name
     * @uml.property  name="name"
     */
    public String getName(){ return name; }
    /**
     * @return  the symbol
     * @uml.property  name="symbol"
     */
    public char getSymbol(){ return symbol; }
    /**
     * @return  the dialingPrefix
     * @uml.property  name="dialingPrefix"
     */
    public String getDialingPrefix(){ return dialingPrefix; }
    /**
     * @return  the propertyFileName
     * @uml.property  name="propertyFileName"
     */
    public String getPropertyFileName(){ return propertyFileName; }
    /**
     * @return  the numberFormat
     * @uml.property  name="numberFormat"
     */
    public NumberFormat getNumberFormat(){ return numberFormat; }
    
    public String toString(){ return name; }
}