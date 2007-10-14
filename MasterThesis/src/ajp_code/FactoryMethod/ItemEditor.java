package ajp_code.FactoryMethod;
import javax.swing.JComponent;
public interface ItemEditor {
    public JComponent getGUI();
    public void commitChanges();
}