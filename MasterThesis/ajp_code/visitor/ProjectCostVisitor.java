package ajp_code.visitor;
/**
 * @author  Marcel Birkner
 */
public class ProjectCostVisitor implements ProjectVisitor{
    private double totalCost;
    private double hourlyRate;
    
    /**
     * @return  the hourlyRate
     * @uml.property  name="hourlyRate"
     */
    public double getHourlyRate(){ return hourlyRate; }
    /**
     * @return  the totalCost
     * @uml.property  name="totalCost"
     */
    public double getTotalCost(){ return totalCost; }
    
    /**
     * @param hourlyRate  the hourlyRate to set
     * @uml.property  name="hourlyRate"
     */
    public void setHourlyRate(double rate){ hourlyRate = rate; }
    
    public void resetTotalCost(){ totalCost = 0.0; }
    
    public void visitDependentTask(DependentTask p){
        double taskCost = p.getTimeRequired() * hourlyRate;
        taskCost *= p.getDependencyWeightingFactor();
        totalCost += taskCost;
    }
    public void visitDeliverable(Deliverable p){
        totalCost += p.getMaterialsCost() + p.getProductionCost();
    }
    public void visitTask(Task p){
        totalCost += p.getTimeRequired() * hourlyRate;
    }
    public void visitProject(Project p){ }
}