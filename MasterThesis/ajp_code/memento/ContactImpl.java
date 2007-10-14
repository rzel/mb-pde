package ajp_code.memento;
/**
 * @author  Marcel Birkner
 */
public class ContactImpl implements Contact{
    private String firstName;
    private String lastName;
    private String title;
    private String organization;
    private Address address;
    
    public ContactImpl(){}
    public ContactImpl(String newFirstName, String newLastName,
        String newTitle, String newOrganization, Address newAddress){
            firstName = newFirstName;
            lastName = newLastName;
            title = newTitle;
            organization = newOrganization;
            address = newAddress;
    }
    
    /**
     * @return  the firstName
     * @uml.property  name="firstName"
     */
    public String getFirstName(){ return firstName; }
    /**
     * @return  the lastName
     * @uml.property  name="lastName"
     */
    public String getLastName(){ return lastName; }
    /**
     * @return  the title
     * @uml.property  name="title"
     */
    public String getTitle(){ return title; }
    /**
     * @return  the organization
     * @uml.property  name="organization"
     */
    public String getOrganization(){ return organization; }
    /**
     * @return  the address
     * @uml.property  name="address"
     */
    public Address getAddress(){ return address; }
    
    /**
     * @param firstName  the firstName to set
     * @uml.property  name="firstName"
     */
    public void setFirstName(String newFirstName){ firstName = newFirstName; }
    /**
     * @param lastName  the lastName to set
     * @uml.property  name="lastName"
     */
    public void setLastName(String newLastName){ lastName = newLastName; }
    /**
     * @param title  the title to set
     * @uml.property  name="title"
     */
    public void setTitle(String newTitle){ title = newTitle; }
    /**
     * @param organization  the organization to set
     * @uml.property  name="organization"
     */
    public void setOrganization(String newOrganization){ organization = newOrganization; }
    /**
     * @param address  the address to set
     * @uml.property  name="address"
     */
    public void setAddress(Address newAddress){ address = newAddress; }
    
    public String toString(){
        return firstName + " " + lastName;
    }
}