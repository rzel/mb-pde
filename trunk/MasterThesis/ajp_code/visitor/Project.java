package ajp_code.visitor;
import java.util.ArrayList;
/**
 * @author  Marcel Birkner
 */
public class Project implements ProjectItem{
    private String name;
    private String description;
    private ArrayList projectItems = new ArrayList();
    
    public Project(){ }
    public Project(String newName, String newDescription){
        name = newName;
        description = newDescription;
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
     * @return  the projectItems
     * @uml.property  name="projectItems"
     */
    public ArrayList getProjectItems(){ return projectItems; }
    
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
    
    public void addProjectItem(ProjectItem element){
        if (!projectItems.contains(element)){
            projectItems.add(element);
        }
    }
    
    public void removeProjectItem(ProjectItem element){
        projectItems.remove(element);
    }
    
    public void accept(ProjectVisitor v){
        v.visitProject(this);
    }
}