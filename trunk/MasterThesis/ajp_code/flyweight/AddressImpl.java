package ajp_code.flyweight;
/**
 * @author  Marcel Birkner
 */
public class AddressImpl implements Address{
    private String type;
    private String description;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    public static final String HOME = "home";
    public static final String WORK = "work";
    
    public AddressImpl(){ }
    public AddressImpl(String newDescription, String newStreet,
        String newCity, String newState, String newZipCode){
        description = newDescription;
        street = newStreet;
        city = newCity;
        state = newState;
        zipCode = newZipCode;
    }
    
    /**
     * @return  the type
     * @uml.property  name="type"
     */
    public String getType(){ return type; }
    /**
     * @return  the description
     * @uml.property  name="description"
     */
    public String getDescription(){ return description; }
    /**
     * @return  the street
     * @uml.property  name="street"
     */
    public String getStreet(){ return street; }
    /**
     * @return  the city
     * @uml.property  name="city"
     */
    public String getCity(){ return city; }
    /**
     * @return  the state
     * @uml.property  name="state"
     */
    public String getState(){ return state; }
    /**
     * @return  the zipCode
     * @uml.property  name="zipCode"
     */
    public String getZipCode(){ return zipCode; }
    
    /**
     * @param type  the type to set
     * @uml.property  name="type"
     */
    public void setType(String newType){ type = newType; }
    /**
     * @param description  the description to set
     * @uml.property  name="description"
     */
    public void setDescription(String newDescription){ description = newDescription; }
    /**
     * @param street  the street to set
     * @uml.property  name="street"
     */
    public void setStreet(String newStreet){ street = newStreet; }
    /**
     * @param city  the city to set
     * @uml.property  name="city"
     */
    public void setCity(String newCity){ city = newCity; }
    /**
     * @param state  the state to set
     * @uml.property  name="state"
     */
    public void setState(String newState){ state = newState; }
    /**
     * @param zipCode  the zipCode to set
     * @uml.property  name="zipCode"
     */
    public void setZipCode(String newZip){ zipCode = newZip; }
    
    public String toString(){
        return street + EOL_STRING + city + COMMA + SPACE +
            state + SPACE + zipCode + EOL_STRING;
    }
}