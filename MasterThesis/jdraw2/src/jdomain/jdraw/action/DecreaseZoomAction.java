package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.FolderPanel;
import jdomain.jdraw.gui.GridListener;
import jdomain.util.Log;

/*
 * DecreaseZoomAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class DecreaseZoomAction extends DrawAction implements GridListener {

   /** */
   private static final long serialVersionUID = 1L;

   protected DecreaseZoomAction() {
      super( "Decrease Zoom", "zoom_out.png" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( '-' ) } );
      setToolTipText( "Zooms out one level" );
      FolderPanel.addGridListener( this );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      if ( isEnabled() ) {
         if ( FolderPanel.getGrid() > FolderPanel.MIN_GRID ) {
            FolderPanel.setGrid( FolderPanel.getGrid() - 1 );
            Log.info( "Zoom decreased." );
         }
      }
   }

   public void gridChanged( int oldValue, int newValue ) {
      setEnabled( newValue > FolderPanel.MIN_GRID );
   }

}
