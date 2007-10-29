package jdomain.jdraw.gui.undo;

import jdomain.jdraw.action.DrawAction;
import jdomain.jdraw.action.RedoAction;
import jdomain.jdraw.action.UndoAction;
import jdomain.jdraw.gui.MainFrame;

import java.util.ArrayList;

import jdomain.util.Assert;

/*
 * UndoManager.java - created on 30.10.2003
 * 
 * @author Michaela Behling
 */

public final class UndoManager {

   private static final int MAX_UNDOS = 20;
   public static final UndoManager INSTANCE = new UndoManager();

   private final ArrayList buffer = new ArrayList();
   private int pos;
   private boolean hasChanges;

   private UndoManager() {
      reset();
   }

   public boolean hasChanges() {
      return hasChanges;
   }

   public void setHasChanges( boolean flag ) {
      this.hasChanges = flag;
      if ( MainFrame.INSTANCE != null ) {
         MainFrame.INSTANCE.updateTitle();
      }
   }

   public void addUndoable( Undoable u ) {
      Assert.isTrue( u.isValid(), "undo: invalid undoable " + u.getClass().getName() );
      buffer.add( u );
      if ( buffer.size() > MAX_UNDOS ) {
         buffer.remove( 0 );
      }
      pos = buffer.size();
      setHasChanges( true );
      updateActions();
   }

   private void updateActions() {
      DrawAction.getAction( UndoAction.class ).setEnabled( canUndo() );
      DrawAction.getAction( RedoAction.class ).setEnabled( canRedo() );
   }

   public void reset() {
      buffer.clear();
      pos = 0;
      setHasChanges( false );
      updateActions();
   }

   public void undo() {
      pos--;
      Undoable u = get( pos );
      u.undo();
      updateActions();
   }

   public void redo() {
      Undoable u = get( pos );
      u.redo();
      pos++;
      updateActions();
   }

   private Undoable get( int index ) {
      return (Undoable)buffer.get( index );
   }

   public boolean canUndo() {
      return pos != 0;
   }

   public boolean canRedo() {
      return (!buffer.isEmpty()) && (pos < buffer.size());
   }

}
