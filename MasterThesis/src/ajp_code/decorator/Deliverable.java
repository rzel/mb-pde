package ajp_code.decorator;
/**
 * @author  Marcel Birkner
 */
public class Deliverable implements ProjectItem{
    private String name;
    private String description;
    private Contact owner;
    
    public Deliverable(){ }
    public Deliverable(String newName, String newDescription,
        Contact newOwner){
        name = newName;
        description = newDescription;
        owner = newOwner;
    }
    
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
    /**
     * @return  the owner
     * @uml.property  name="owner"
     */
    public Contact getOwner(){ return owner; }
    public double getTimeRequired(){ return 0; }
    
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
     * @param owner  the owner to set
     * @uml.property  name="owner"
     */
    public void setOwner(Contact newOwner){ owner = newOwner; }
    
    public String toString(){
        return "Deliverable: " + name;
    }
}