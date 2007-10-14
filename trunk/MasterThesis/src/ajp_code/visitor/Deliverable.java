package ajp_code.visitor;
import java.util.ArrayList;
/**
 * @author  Marcel Birkner
 */
public class Deliverable implements ProjectItem{
    private String name;
    private String description;
    private Contact owner;
    private double materialsCost;
    private double productionCost;
    
    public Deliverable(){ }
    public Deliverable(String newName, String newDescription,
        Contact newOwner, double newMaterialsCost, double newProductionCost){
        name = newName;
        description = newDescription;
        owner = newOwner;
        materialsCost = newMaterialsCost;
        productionCost = newProductionCost;
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
    /**
     * @return  the materialsCost
     * @uml.property  name="materialsCost"
     */
    public double getMaterialsCost(){ return materialsCost; }
    /**
     * @return  the productionCost
     * @uml.property  name="productionCost"
     */
    public double getProductionCost(){ return productionCost; }
    
    /**
     * @param materialsCost  the materialsCost to set
     * @uml.property  name="materialsCost"
     */
    public void setMaterialsCost(double newCost){ materialsCost = newCost; }
    /**
     * @param productionCost  the productionCost to set
     * @uml.property  name="productionCost"
     */
    public void setProductionCost(double newCost){ productionCost = newCost; }
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

    public void accept(ProjectVisitor v){
        v.visitDeliverable(this);
    }
    
    public ArrayList getProjectItems(){
        return null;
    }
}