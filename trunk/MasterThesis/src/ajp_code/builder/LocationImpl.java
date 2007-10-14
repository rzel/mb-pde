package ajp_code.builder;
/**
 * @author  Marcel Birkner
 */
public class LocationImpl implements Location{
    private String location;
    
    public LocationImpl(){ }
    public LocationImpl(String newLocation){
        location = newLocation;
    }
    
    /**
     * @return  the location
     * @uml.property  name="location"
     */
    public String getLocation(){ return location; }
    
    /**
     * @param location  the location to set
     * @uml.property  name="location"
     */
    public void setLocation(String newLocation){ location = newLocation; }
    
    public String toString(){ return location; }
}