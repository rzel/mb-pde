package ajp_code.templatemethod;
import java.io.Serializable;
/**
 * @author  Marcel Birkner
 */
public abstract class ProjectItem implements Serializable{
    private String name;
    private String description;
    private double rate;
    
    public ProjectItem(){}
    public ProjectItem(String newName, String newDescription, double newRate){
        name = newName;
        description = newDescription;
        rate = newRate;
    }
    
    /**
     * @param name  the name to set
     * @uml.property  name="name"
     */
    public void setName(String newName){ name = newName; }
    /**
     * @param description  the description to set
     * @uml.property  name="description"
     */
    public void setDescription(String newDescription){ description = newDescription; }
    /**
     * @param rate  the rate to set
     * @uml.property  name="rate"
     */
    public void setRate(double newRate){ rate = newRate; }
    
    /**
     * @return  the name
     * @uml.property  name="name"
     */
    public String getName(){ return name; }
    /**
     * @return  the description
     * @uml.property  name="description"
     */
    public String getDescription(){ return description; }
    public final double getCostEstimate(){
        return getTimeRequired() * getRate() + getMaterialsCost();
    }
    /**
     * @return  the rate
     * @uml.property  name="rate"
     */
    public double getRate(){ return rate; }
    
    public String toString(){ return getName(); }
    
    public abstract double getTimeRequired();
    public abstract double getMaterialsCost();
}