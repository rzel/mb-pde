package ajp_code.AbstractFactory;
/**
 * @author  Marcel Birkner
 */
public abstract class PhoneNumber{
    private String phoneNumber;
    public abstract String getCountryCode();
    
    /**
     * @return  the phoneNumber
     * @uml.property  name="phoneNumber"
     */
    public String getPhoneNumber(){ return phoneNumber; }
    
    /**
     * @param phoneNumber  the phoneNumber to set
     * @uml.property  name="phoneNumber"
     */
    public void setPhoneNumber(String newNumber){
        try{
            Long.parseLong(newNumber);
            phoneNumber = newNumber;
        }
        catch (NumberFormatException exc){
        }
    }
}