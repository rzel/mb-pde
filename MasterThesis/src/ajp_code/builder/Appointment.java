package ajp_code.builder;
import java.util.ArrayList;
import java.util.Date;
/**
 * @author  Marcel Birkner
 */
public class Appointment{
    private Date startDate;
    private Date endDate;
    private String description;
    private ArrayList attendees = new ArrayList();
    private Location location;
    public static final String EOL_STRING =
        System.getProperty("line.separator");
    
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
     * @return  the description
     * @uml.property  name="description"
     */
    public String getDescription(){ return description; }
    /**
     * @return  the attendees
     * @uml.property  name="attendees"
     */
    public ArrayList getAttendees(){ return attendees; }
    /**
     * @return  the location
     * @uml.property  name="location"
     */
    public Location getLocation(){ return location; }

    /**
     * @param description  the description to set
     * @uml.property  name="description"
     */
    public void setDescription(String newDescription){ description = newDescription; }
    /**
     * @param location  the location to set
     * @uml.property  name="location"
     */
    public void setLocation(Location newLocation){ location = newLocation; }
    /**
     * @param startDate  the startDate to set
     * @uml.property  name="startDate"
     */
    public void setStartDate(Date newStartDate){ startDate = newStartDate; }
    /**
     * @param endDate  the endDate to set
     * @uml.property  name="endDate"
     */
    public void setEndDate(Date newEndDate){ endDate = newEndDate; }
    /**
     * @param attendees  the attendees to set
     * @uml.property  name="attendees"
     */
    public void setAttendees(ArrayList newAttendees){
        if (newAttendees != null){
            attendees = newAttendees;
        }
    }
    
    public void addAttendee(Contact attendee){
        if (!attendees.contains(attendee)){
            attendees.add(attendee);
        }
    }
    
    public void removeAttendee(Contact attendee){
        attendees.remove(attendee);
    }
    
    public String toString(){
        return "  Description: " + description + EOL_STRING +
            "  Start Date: " + startDate + EOL_STRING +
            "  End Date: " + endDate + EOL_STRING +
            "  Location: " + location + EOL_STRING +
            "  Attendees: " + attendees;
    }
}