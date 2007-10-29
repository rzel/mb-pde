package jdomain.jdraw.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

/*
 * ReopenAction - created on 16.12.2005
 * 
 * @author Michaela Behling
 */

public final class ReopenAction extends AbstractAction implements ContinuedAction {

   /** */
   private static final long serialVersionUID = 1L;
   private final File file;

   public ReopenAction( File file ) {
      super( file.getName() );
      this.file = file;
      putValue( LONG_DESCRIPTION, "Reopens " + file.toString() );
   }

   public void actionPerformed( ActionEvent e ) {
      LoseChanges.INSTANCE.checkUnsavedChanges( this );
   }

   public void continueAction() {
      SwingUtilities.invokeLater( new Runnable() {
         public void run() {
            LoadAction.openImage( file.toString() );
         }
      } );
   }

}
