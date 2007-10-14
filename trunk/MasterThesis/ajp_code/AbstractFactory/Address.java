package ajp_code.AbstractFactory;

/**
 * @author  Marcel Birkner
 */
public abstract class Address{
    private String street;
    private String city;
    private String region;
    private String postalCode;
    
    public static final String EOL_STRING =
        System.getProperty("line.separator");
    public static final String SPACE = " ";
    
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
     * @return  the postalCode
     * @uml.property  name="postalCode"
     */
    public String getPostalCode(){ return postalCode; }
    /**
     * @return  the region
     * @uml.property  name="region"
     */
    public String getRegion(){ return region; }
    public abstract String getCountry();
    
    public String getFullAddress(){
        return street + EOL_STRING +
            city + SPACE + postalCode + EOL_STRING;
    }
    
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
     * @param region  the region to set
     * @uml.property  name="region"
     */
    public void setRegion(String newRegion){ region = newRegion; }
    /**
     * @param postalCode  the postalCode to set
     * @uml.property  name="postalCode"
     */
    public void setPostalCode(String newPostalCode){ postalCode = newPostalCode; }
}