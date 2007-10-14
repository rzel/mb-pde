package ajp_code.strategy;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * @author  Marcel Birkner
 */
public class ContactList implements Serializable{
    private ArrayList contacts = new ArrayList();
    private SummarizingStrategy summarizer;
    
    /**
     * @return  the contacts
     * @uml.property  name="contacts"
     */
    public ArrayList getContacts(){ return contacts; }
    public Contact [] getContactsAsArray(){ return (Contact [])(contacts.toArray(new Contact [1])); }
    
    /**
     * @param summarizer  the summarizer to set
     * @uml.property  name="summarizer"
     */
    public void setSummarizer(SummarizingStrategy newSummarizer){ summarizer = newSummarizer; }
    /**
     * @param contacts  the contacts to set
     * @uml.property  name="contacts"
     */
    public void setContacts(ArrayList newContacts){ contacts = newContacts; }
    
    public void addContact(Contact element){
        if (!contacts.contains(element)){
            contacts.add(element);
        }
    }
    public void removeContact(Contact element){
        contacts.remove(element);
    }
    
    public String summarize(){
        return summarizer.summarize(getContactsAsArray());
    }
    
    public String [] makeSummarizedList(){
        return summarizer.makeSummarizedList(getContactsAsArray());
    }
}