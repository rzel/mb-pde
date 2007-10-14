package ajp_code.memento;
import java.util.ArrayList;
/**
 * @author  Marcel Birkner
 */
public class AddressBook{
    private ArrayList contacts = new ArrayList();
    
    public Object getMemento(){
        return new AddressBookMemento(contacts);
    }
    public void setMemento(Object object){
        if (object instanceof AddressBookMemento){
            AddressBookMemento memento = (AddressBookMemento)object;
            contacts = memento.state;
        }
    }
    
    private class AddressBookMemento{
        private ArrayList state;
        
        private AddressBookMemento(ArrayList contacts){
            this.state = contacts;
        }
    }
    
    public AddressBook(){ }
    public AddressBook(ArrayList newContacts){
        contacts = newContacts;
    }
    
    public void addContact(Contact contact){
        if (!contacts.contains(contact)){
            contacts.add(contact);
        }
    }
    public void removeContact(Contact contact){
        contacts.remove(contact);
    }
    public void removeAllContacts(){
        contacts = new ArrayList();
    }
    /**
     * @return  the contacts
     * @uml.property  name="contacts"
     */
    public ArrayList getContacts(){
        return contacts;
    }
    public String toString(){
        return contacts.toString();
    }
}
