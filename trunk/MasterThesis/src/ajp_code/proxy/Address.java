package ajp_code.proxy;
import java.io.Serializable;
/**
 * @author  Marcel Birkner
 */
public interface Address extends Serializable{
    public static final String EOL_STRING = System.getProperty("line.separator");
    public static final String SPACE = " ";
    public static final String COMMA = ",";
    public String getAddress();
    /**
     * @return
     * @uml.property  name="type"
     */
    public String getType();
    /**
     * @return
     * @uml.property  name="description"
     */
    public String getDescription();
    /**
     * @return
     * @uml.property  name="street"
     */
    public String getStreet();
    /**
     * @return
     * @uml.property  name="city"
     */
    public String getCity();
    /**
     * @return
     * @uml.property  name="state"
     */
    public String getState();
    /**
     * @return
     * @uml.property  name="zipCode"
     */
    public String getZipCode();

    /**
     * @param newType
     * @uml.property  name="type"
     */
    public void setType(String newType);
    /**
     * @param newDescription
     * @uml.property  name="description"
     */
    public void setDescription(String newDescription);
    /**
     * @param newStreet
     * @uml.property  name="street"
     */
    public void setStreet(String newStreet);
    /**
     * @param newCity
     * @uml.property  name="city"
     */
    public void setCity(String newCity);
    /**
     * @param newState
     * @uml.property  name="state"
     */
    public void setState(String newState);
    /**
     * @param newZip
     * @uml.property  name="zipCode"
     */
    public void setZipCode(String newZip);
}
