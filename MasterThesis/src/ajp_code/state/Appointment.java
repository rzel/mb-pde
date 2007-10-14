package ajp_code.state;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
/**
 * @author  Marcel Birkner
 */
public class Appointment implements Serializable{
    private String reason;
    private ArrayList contacts;
    private Location location;
    private Date startDate;
    private Date endDate;

    public Appointment(String reason, ArrayList contacts, Location location, Date startDate, Date endDate){
        this.reason = reason;
        this.contacts = contacts;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * @return  the reason
     * @uml.property  name="reason"
     */
    public String getReason(){ return reason; }
    /**
     * @return  the contacts
     * @uml.property  name="contacts"
     */
    public ArrayList getContacts(){ return contacts; }
    /**
     * @return  the location
     * @uml.property  name="location"
     */
    public Location getLocation(){ return location; }
    /**
     * @return  the startDate
     * @uml.property  name="startDate"
     */
    public Date getStartDate(){ return startDate; }
    /**
     * @return  the endDate
     * @uml.property  name="endDate"
     */
    public Date getEndDate(){ return endDate; }
    
    /**
     * @param reason  the reason to set
     * @uml.property  name="reason"
     */
    public void setReason(String reason){ this.reason = reason; }
    /**
     * @param contacts  the contacts to set
     * @uml.property  name="contacts"
     */
    public void setContacts(ArrayList contacts){ this.contacts = contacts; }
    /**
     * @param location  the location to set
     * @uml.property  name="location"
     */
    public void setLocation(Location location){ this.location = location; }
    /**
     * @param startDate  the startDate to set
     * @uml.property  name="startDate"
     */
    public void setStartDate(Date startDate){ this.startDate = startDate; }
    /**
     * @param endDate  the endDate to set
     * @uml.property  name="endDate"
     */
    public void setEndDate(Date endDate){ this.endDate = endDate; }
    
    public String toString(){
        return "Appointment:" + "\n    Reason: " + reason +
		"\n    Location: " + location + "\n    Start: " +
            startDate + "\n    End: " + endDate + "\n";
    }
}