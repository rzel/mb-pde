package ajp_code.observer;
/**
 * @author  Marcel Birkner
 */
public class Task{
    private String name = "";
    private String notes = "";
    private double timeRequired;
    
    public Task(){ }
    public Task(String newName, String newNotes, double newTimeRequired){
        name = newName;
        notes = newNotes;
        timeRequired = newTimeRequired;
    }
    
    /**
     * @return  the name
     * @uml.property  name="name"
     */
    public String getName(){ return name; }
    /**
     * @return  the notes
     * @uml.property  name="notes"
     */
    public String getNotes(){ return notes; }
    /**
     * @return  the timeRequired
     * @uml.property  name="timeRequired"
     */
    public double getTimeRequired(){ return timeRequired; }
    /**
     * @param name  the name to set
     * @uml.property  name="name"
     */
    public void setName(String newName){ name = newName; }
    /**
     * @param timeRequired  the timeRequired to set
     * @uml.property  name="timeRequired"
     */
    public void setTimeRequired(double newTimeRequired){ timeRequired = newTimeRequired; }
    /**
     * @param notes  the notes to set
     * @uml.property  name="notes"
     */
    public void setNotes(String newNotes){ notes = newNotes; }
    public String toString(){ return name + " " + notes; }
}