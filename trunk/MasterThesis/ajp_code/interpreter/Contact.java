package ajp_code.interpreter;
import java.io.Serializable;
/**
 * @author  Marcel Birkner
 */
public interface Contact extends Serializable{
    public static final String SPACE = " ";
    /**
     * @return
     * @uml.property  name="firstName"
     */
    public String getFirstName();
    /**
     * @return
     * @uml.property  name="lastName"
     */
    public String getLastName();
    /**
     * @return
     * @uml.property  name="title"
     */
    public String getTitle();
    /**
     * @return
     * @uml.property  name="organization"
     */
    public String getOrganization();
    
    /**
     * @param newFirstName
     * @uml.property  name="firstName"
     */
    public void setFirstName(String newFirstName);
    /**
     * @param newLastName
     * @uml.property  name="lastName"
     */
    public void setLastName(String newLastName);
    /**
     * @param newTitle
     * @uml.property  name="title"
     */
    public void setTitle(String newTitle);
    /**
     * @param newOrganization
     * @uml.property  name="organization"
     */
    public void setOrganization(String newOrganization);
}