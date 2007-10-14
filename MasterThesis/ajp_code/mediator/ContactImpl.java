package ajp_code.mediator;
/**
 * @author  Marcel Birkner
 */
public class ContactImpl implements Contact{
    private String firstName;
    private String lastName;
    private String title;
    private String organization;
    
    public ContactImpl(){}
    public ContactImpl(String newFirstName, String newLastName,
        String newTitle, String newOrganization){
            firstName = newFirstName;
            lastName = newLastName;
            title = newTitle;
            organization = newOrganization;
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
    
    public String toString(){
        return firstName + SPACE + lastName;
    }
}