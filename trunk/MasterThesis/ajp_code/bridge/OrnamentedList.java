package ajp_code.bridge;
/**
 * @author  Marcel Birkner
 */
public class OrnamentedList extends BaseList{
    private char itemType;
    
    /**
     * @return  the itemType
     * @uml.property  name="itemType"
     */
    public char getItemType(){ return itemType; }
    /**
     * @param itemType  the itemType to set
     * @uml.property  name="itemType"
     */
    public void setItemType(char newItemType){
        if (newItemType > ' '){
            itemType = newItemType;
        }
    }
    
    public String get(int index){
        return itemType + " " + super.get(index);
    }
}