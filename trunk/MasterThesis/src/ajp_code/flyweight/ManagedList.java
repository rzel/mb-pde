package ajp_code.flyweight;
import java.util.ArrayList;
/**
 * @author  Marcel Birkner
 */
public class ManagedList{
    private ArrayList elements = new ArrayList();
    private Class classType;
    
    public ManagedList(){ }
    public ManagedList(Class newClassType){
        classType = newClassType;
    }
    
    /**
     * @param classType  the classType to set
     * @uml.property  name="classType"
     */
    public void setClassType(Class newClassType){
        classType = newClassType;
    }
    
    public void addItem(Object item){
        if ((item != null) && (classType.isInstance(item))){
            elements.add(item);
        } else {
            elements.add(item);
        }
    }
    
    public void removeItem(Object item){
        elements.remove(item);
    }
    
    public ArrayList getItems(){
        return elements;
    }
}