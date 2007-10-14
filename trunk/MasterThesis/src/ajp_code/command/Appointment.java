package ajp_code.command;
import java.util.Date;
/**
 * @author  Marcel Birkner
 */
public class Appointment{
    private String reason;
    /**
     * @uml.property  name="contacts"
     * @uml.associationEnd  multiplicity="(0 -1)"
     */
    private Contact[] contacts;
    private Location location;
    private Date startDate;
    private Date endDate;

    public Appointment(String reason, Contact[] contacts, Location location, Date startDate, Date endDate){
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
    public Contact[] getContacts(){ return contacts; }
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
     * @param location  the location to set
     * @uml.property  name="location"
     */
    public void setLocation(Location location){ this.location = location; }
    
    public String toString(){
        return "Appointment:" + "\n    Reason: " + reason +
		"\n    Location: " + location + "\n    Start: " +
            startDate + "\n    End: " + endDate + "\n";
    }
}