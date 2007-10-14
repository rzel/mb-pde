package ajp_code.composite;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author  Marcel Birkner
 */
public class Task implements ProjectItem{
    private String name;
    private String details;
    private ArrayList projectItems = new ArrayList();
    private Contact owner;
    private double timeRequired;
    
    public Task(){ }
    public Task(String newName, String newDetails,
        Contact newOwner, double newTimeRequired){
        name = newName;
        details = newDetails;
        owner = newOwner;
        timeRequired = newTimeRequired;
    }
    
    /**
     * @return  the name
     * @uml.property  name="name"
     */
    public String getName(){ return name; }
    /**
     * @return  the details
     * @uml.property  name="details"
     */
    public String getDetails(){ return details; }
    /**
     * @return  the projectItems
     * @uml.property  name="projectItems"
     */
    public ArrayList getProjectItems(){ return projectItems; }
    /**
     * @return  the owner
     * @uml.property  name="owner"
     */
    public Contact getOwner(){ return owner; }
    /**
     * @return  the timeRequired
     * @uml.property  name="timeRequired"
     */
    public double getTimeRequired(){
        double totalTime = timeRequired;
        Iterator items = projectItems.iterator();
        while(items.hasNext()){
            ProjectItem item = (ProjectItem)items.next();
            totalTime += item.getTimeRequired();
        }
        return totalTime;
    }
    
    /**
     * @param name  the name to set
     * @uml.property  name="name"
     */
    public void setName(String newName){ name = newName; }
    /**
     * @param details  the details to set
     * @uml.property  name="details"
     */
    public void setDetails(String newDetails){ details = newDetails; }
    /**
     * @param owner  the owner to set
     * @uml.property  name="owner"
     */
    public void setOwner(Contact newOwner){ owner = newOwner; }
    /**
     * @param timeRequired  the timeRequired to set
     * @uml.property  name="timeRequired"
     */
    public void setTimeRequired(double newTimeRequired){ timeRequired = newTimeRequired; }
    
    public void addProjectItem(ProjectItem element){
        if (!projectItems.contains(element)){
            projectItems.add(element);
        }
    }
    public void removeProjectItem(ProjectItem element){
        projectItems.remove(element);
    }
}