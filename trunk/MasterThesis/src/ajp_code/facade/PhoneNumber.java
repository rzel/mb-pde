package ajp_code.facade;
/**
 * @author  Marcel Birkner
 */
public class PhoneNumber {
    private static String selectedInterPrefix;
    private String internationalPrefix;
    private String areaNumber;
    private String netNumber;

    public PhoneNumber(String intPrefix, String areaNumber, String netNumber) {
        this.internationalPrefix = intPrefix;
        this.areaNumber = areaNumber;
        this.netNumber = netNumber;
    }
    
    /**
     * @return  the internationalPrefix
     * @uml.property  name="internationalPrefix"
     */
    public String getInternationalPrefix(){ return internationalPrefix; }
    /**
     * @return  the areaNumber
     * @uml.property  name="areaNumber"
     */
    public String getAreaNumber(){ return areaNumber; }
    /**
     * @return  the netNumber
     * @uml.property  name="netNumber"
     */
    public String getNetNumber(){ return netNumber; }
    /**
     * @return  the selectedInterPrefix
     * @uml.property  name="selectedInterPrefix"
     */
    public static String getSelectedInterPrefix(){ return selectedInterPrefix; }
    
    /**
     * @param internationalPrefix  the internationalPrefix to set
     * @uml.property  name="internationalPrefix"
     */
    public void setInternationalPrefix(String newPrefix){ internationalPrefix = newPrefix; }
    /**
     * @param areaNumber  the areaNumber to set
     * @uml.property  name="areaNumber"
     */
    public void setAreaNumber(String newAreaNumber){ areaNumber = newAreaNumber; }
    /**
     * @param netNumber  the netNumber to set
     * @uml.property  name="netNumber"
     */
    public void setNetNumber(String newNetNumber){ netNumber = newNetNumber; }
    /**
     * @param selectedInterPrefix  the selectedInterPrefix to set
     * @uml.property  name="selectedInterPrefix"
     */
    public static void setSelectedInterPrefix(String prefix) { selectedInterPrefix = prefix; }
    
    public String toString(){
        return internationalPrefix + areaNumber + netNumber;
    }
}