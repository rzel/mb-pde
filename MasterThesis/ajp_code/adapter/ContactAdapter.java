package ajp_code.adapter;
/**
 * @author      Marcel Birkner
 * @uml.dependency   supplier="ajp_code.adapter.ChovnatlhImpl"
 * @uml.dependency   supplier="MasterThesisSoftware.null"
 */
public class ContactAdapter implements Contact{
    /**
     * @uml.property   name="contact"
     */
    private Chovnatlh contact;
    
    public ContactAdapter(){
        contact = new ChovnatlhImpl();
    }
    public ContactAdapter(Chovnatlh newContact){
        contact = newContact;
    }
    
    public String getFirstName(){
        return contact.tlhapWa$DIchPong();
    }
    public String getLastName(){
        return contact.tlhapQavPong();
    }
    public String getTitle(){
        return contact.tlhapPatlh();
    }
    public String getOrganization(){
        return contact.tlhapGhom();
    }
    
    /**
     * @param contact  the contact to set
     * @uml.property  name="contact"
     */
    public void setContact(Chovnatlh newContact){
        contact = newContact;
    }
    public void setFirstName(String newFirstName){
        contact.cherWa$DIchPong(newFirstName);
    }
    public void setLastName(String newLastName){
        contact.cherQavPong(newLastName);
    }
    public void setTitle(String newTitle){
        contact.cherPatlh(newTitle);
    }
    public void setOrganization(String newOrganization){
        contact.cherGhom(newOrganization);
    }
    
    public String toString(){
        return contact.toString();
    }
}