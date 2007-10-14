package ajp_code.facade;
import java.text.NumberFormat;
/**
 * @author  Marcel Birkner
 */
public class Currency{
    private char currencySymbol;
    private NumberFormat numberFormat;
    
    /**
     * @param currencySymbol  the currencySymbol to set
     * @uml.property  name="currencySymbol"
     */
    public void setCurrencySymbol(char newCurrencySymbol){ currencySymbol = newCurrencySymbol; }
    /**
     * @param numberFormat  the numberFormat to set
     * @uml.property  name="numberFormat"
     */
    public void setNumberFormat(NumberFormat newNumberFormat){ numberFormat = newNumberFormat; }
    
    /**
     * @return  the currencySymbol
     * @uml.property  name="currencySymbol"
     */
    public char getCurrencySymbol(){ return currencySymbol; }
    /**
     * @return  the numberFormat
     * @uml.property  name="numberFormat"
     */
    public NumberFormat getNumberFormat(){ return numberFormat; }
}