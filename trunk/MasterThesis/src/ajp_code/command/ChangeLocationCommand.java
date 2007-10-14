package ajp_code.command;
/**
 * @author  Marcel Birkner
 */
public class ChangeLocationCommand implements UndoableCommand{
    private Appointment appointment;
    private Location oldLocation;
    private Location newLocation;
    private LocationEditor editor;
    
    /**
     * @return  the appointment
     * @uml.property  name="appointment"
     */
    public Appointment getAppointment(){ return appointment; }
    
    /**
     * @param appointment  the appointment to set
     * @uml.property  name="appointment"
     */
    public void setAppointment(Appointment appointment){ this.appointment = appointment; }
    public void setLocationEditor(LocationEditor locationEditor){ editor = locationEditor; }
    
    public void execute(){
        oldLocation = appointment.getLocation();
        newLocation = editor.getNewLocation();
        appointment.setLocation(newLocation);
    }
    public void undo(){
        appointment.setLocation(oldLocation);
    }
    public void redo(){
        appointment.setLocation(newLocation);
    }
}