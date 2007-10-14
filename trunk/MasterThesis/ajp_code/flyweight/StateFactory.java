package ajp_code.flyweight;
/**
 * @author  Marcel Birkner
 */
public class StateFactory {
    public static final State CLEAN = new CleanState();
    public static final State DIRTY = new DirtyState();
    private static State currentState = CLEAN;
    
    /**
     * @return  the currentState
     * @uml.property  name="currentState"
     */
    public static State getCurrentState(){
        return currentState;
    }
    
    /**
     * @param currentState  the currentState to set
     * @uml.property  name="currentState"
     */
    public static void setCurrentState(State state){
        currentState = state;
    }
}