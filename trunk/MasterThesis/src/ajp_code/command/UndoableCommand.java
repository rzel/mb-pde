package ajp_code.command;
public interface UndoableCommand extends Command{
    public void undo();
    public void redo();
}