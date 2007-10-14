package ajp_code.decorator;
/**
 * @author  Marcel Birkner
 */
public abstract class ProjectDecorator implements ProjectItem{
    private ProjectItem projectItem;
    
    /**
     * @return  the projectItem
     * @uml.property  name="projectItem"
     */
    protected ProjectItem getProjectItem(){ return projectItem; }
    /**
     * @param projectItem  the projectItem to set
     * @uml.property  name="projectItem"
     */
    public void setProjectItem(ProjectItem newProjectItem){ projectItem = newProjectItem; }
    
    public double getTimeRequired(){
        return projectItem.getTimeRequired();
    }
}