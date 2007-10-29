package jdomain.jdraw.action;

import jdomain.jdraw.Settings;
import jdomain.jdraw.gui.MainFrame;
import jdomain.util.Log;

/*
 * ExitAction - created on 15.12.2005
 * 
 * @author Michaela Behling
 */

public final class ExitAction extends BlockingDrawAction implements ContinuedAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected ExitAction() {
      super( "Exit" );
      setToolTipText( "Exits JDraw" );
   }

   public void continueAction() {
      Settings.INSTANCE.saveWindowDimensions();
      Settings.INSTANCE.save();
      MainFrame.INSTANCE.setVisible( false );
      MainFrame.INSTANCE.dispose();
      Log.close();
      System.exit( 0 );
   }

   protected boolean changesImage() {
      return false;
   }

   public boolean prepareAction() {
      LoseChanges.INSTANCE.checkUnsavedChanges( this );
      return false;
   }

   public void startAction() {

   }

   public void finishAction() {

   }
}
