package ajp_code.prototype;
/**
 * @author  Marcel Birkner
 */
public class Address implements Copyable{
    private String type;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    public static final String EOL_STRING =
        System.getProperty("line.separator");
    public static final String COMMA = ",";
    public static final String HOME = "home";
    public static final String WORK = "work";
    
    public Address(String initType, String initStreet,
        String initCity, String initState, String initZip){
            type = initType;
            street = initStreet;
            city = initCity;
            state = initState;
            zipCode = initZip;
    }
    
    public Address(String initStreet, String initCity,
        String initState, String initZip){
            this(WORK, initStreet, initCity, initState, initZip);
    }
    public Address(String initType){
        type = initType;
    }
    public Address(){ }
    
    /**
     * @return  the type
     * @uml.property  name="type"
     */
    public String getType(){ return type; }
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
    
    public Object copy(){
        return new Address(street, city, state, zipCode);
    }
    
    public String toString(){
        return "\t" + street + COMMA + " " + EOL_STRING +
            "\t" + city + COMMA + " " + state + " " + zipCode;
    }
}