package ajp_code.templatemethod;
/**
 * @author  Marcel Birkner
 */
public class Deliverable extends ProjectItem{
    private double materialsCost;
    private double productionTime;
    
    public Deliverable(){ }
    public Deliverable(String newName, String newDescription,
        double newMaterialsCost, double newProductionTime,
        double newRate){
        super(newName, newDescription, newRate);
        materialsCost = newMaterialsCost;
        productionTime = newProductionTime;
    }
    
    /**
     * @param materialsCost  the materialsCost to set
     * @uml.property  name="materialsCost"
     */
    public void setMaterialsCost(double newCost){ materialsCost = newCost; }
    /**
     * @param productionTime  the productionTime to set
     * @uml.property  name="productionTime"
     */
    public void setProductionTime(double newTime){ productionTime = newTime; }
    
    /**
     * @return  the materialsCost
     * @uml.property  name="materialsCost"
     */
    public double getMaterialsCost(){ return materialsCost; }
    public double getTimeRequired(){ return productionTime; }
}