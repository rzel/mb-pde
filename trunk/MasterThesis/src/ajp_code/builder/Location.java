package ajp_code.builder;
import java.io.Serializable;
/**
 * @author  Marcel Birkner
 */
public interface Location extends Serializable{
    /**
     * @return
     * @uml.property  name="location"
     */
    public String getLocation();
    /**
     * @param newLocation
     * @uml.property  name="location"
     */
    public void setLocation(String newLocation);
}