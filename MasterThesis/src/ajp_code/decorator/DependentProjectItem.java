package ajp_code.decorator;
/**
 * @author  Marcel Birkner
 */
public class DependentProjectItem extends ProjectDecorator{
    private ProjectItem dependentItem;
    
    public DependentProjectItem(){ }
    public DependentProjectItem(ProjectItem newDependentItem){
        dependentItem = newDependentItem;
    }
    
    /**
     * @return  the dependentItem
     * @uml.property  name="dependentItem"
     */
    public ProjectItem getDependentItem(){ return dependentItem; }
    
    /**
     * @param dependentItem  the dependentItem to set
     * @uml.property  name="dependentItem"
     */
    public void setDependentItem(ProjectItem newDependentItem){ dependentItem = newDependentItem; }
    
    public String toString(){
        return getProjectItem().toString() + EOL_STRING
            + "\tProjectItem dependent on: " + dependentItem;
    }
}