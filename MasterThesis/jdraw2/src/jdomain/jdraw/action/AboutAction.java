package jdomain.jdraw.action;

import jdomain.jdraw.Main;
import jdomain.jdraw.gui.AboutDialog;

import java.awt.event.ActionEvent;

/*
 * AboutAction - created on 16.11.2003
 * 
 * @author Michaela Behling
 */

public final class AboutAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   public AboutAction() {
      super( "About...", "about.png" );
      setToolTipText( "Displays information about " + Main.APP_NAME );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      AboutDialog dialog = new AboutDialog();
      dialog.open();
   }

}
