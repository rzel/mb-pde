package ajp_code.iterator;
/**
 * @author  Marcel Birkner
 */
public interface ToDoList extends Iterating{
    public void add(String item);
    public void add(String item, int position);
    public void remove(String item);
    public int getNumberOfItems();
    /**
     * @return
     * @uml.property  name="listName"
     */
    public String getListName();
    /**
     * @param newListName
     * @uml.property  name="listName"
     */
    public void setListName(String newListName);
}