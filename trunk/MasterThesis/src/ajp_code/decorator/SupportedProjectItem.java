package ajp_code.decorator;
import java.io.File;
import java.util.ArrayList;
/**
 * @author  Marcel Birkner
 */
public class SupportedProjectItem extends ProjectDecorator{
    private ArrayList supportingDocuments = new ArrayList();
    
    public SupportedProjectItem(){ }
    public SupportedProjectItem(File newSupportingDocument){
        addSupportingDocument(newSupportingDocument);
    }
    
    /**
     * @return  the supportingDocuments
     * @uml.property  name="supportingDocuments"
     */
    public ArrayList getSupportingDocuments(){
        return supportingDocuments;
    }
    
    public void addSupportingDocument(File document){
        if (!supportingDocuments.contains(document)){
            supportingDocuments.add(document);
        }
    }
    
    public void removeSupportingDocument(File document){
        supportingDocuments.remove(document);
    }
    
    public String toString(){
        return getProjectItem().toString() + EOL_STRING
            + "\tSupporting Documents: " + supportingDocuments;
    }
}