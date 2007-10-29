package jdomain.jdraw.action;

import javax.swing.SwingUtilities;

import jdomain.jdraw.gui.MainFrame;
import jdomain.jdraw.gui.undo.UndoManager;
import jdomain.util.Util;
import jdomain.util.gui.GUIUtil;
import jdomain.util.gui.StandardDialog;

public final class LoseChanges {

   public static final LoseChanges INSTANCE = new LoseChanges();

   private LoseChanges() {
   }

   public void checkUnsavedChanges( final ContinuedAction action ) {
      if ( UndoManager.INSTANCE.hasChanges() ) {
         int result = GUIUtil.question( MainFrame.INSTANCE, "Lose Changes?",
               "The current image has unsaved changes.\nDo you want to save them?", "Save", "Lose Changes",
               "Cancel" );
         if ( result == StandardDialog.APPROVE_ACTION ) {
            final SaveAction saveAction = (SaveAction)DrawAction.getAction( SaveAction.class );
            saveAction.actionPerformed();
            while ( !saveAction.hasFinished() ) {
               Util.delay( 100 );
            }
            SwingUtilities.invokeLater( new Runnable() {
               public void run() {
                  if ( saveAction.successfullySaved() ) { // save & continue
                     action.continueAction();
                  }
                  else {
                     System.err.println( "save failed" );
                  }
               }
            } );
         }
         else if ( result == StandardDialog.ABORT_ACTION ) {
            // do nothing
         }
         else { // lose changes
            action.continueAction();
         }
      }
      else { // no changes
         action.continueAction();
      }

   }

}
