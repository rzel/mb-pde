package ajp_code.visitor;
import java.util.ArrayList;
/**
 * @author  Marcel Birkner
 */
public class DependentTask extends Task{
    private ArrayList dependentTasks = new ArrayList();
    private double dependencyWeightingFactor;
    
    public DependentTask(){ }
    public DependentTask(String newName, Contact newOwner,
        double newTimeRequired, double newWeightingFactor){
        super(newName, newOwner, newTimeRequired);
        dependencyWeightingFactor = newWeightingFactor;
    }
    
    /**
     * @return  the dependentTasks
     * @uml.property  name="dependentTasks"
     */
    public ArrayList getDependentTasks(){ return dependentTasks; }
    /**
     * @return  the dependencyWeightingFactor
     * @uml.property  name="dependencyWeightingFactor"
     */
    public double getDependencyWeightingFactor(){ return dependencyWeightingFactor; }
    
    /**
     * @param dependencyWeightingFactor  the dependencyWeightingFactor to set
     * @uml.property  name="dependencyWeightingFactor"
     */
    public void setDependencyWeightingFactor(double newFactor){ dependencyWeightingFactor = newFactor; }
    
    public void addDependentTask(Task element){
        if (!dependentTasks.contains(element)){
            dependentTasks.add(element);
        }
    }
    
    public void removeDependentTask(Task element){
        dependentTasks.remove(element);
    }
    
    public void accept(ProjectVisitor v){
        v.visitDependentTask(this);
    }
}