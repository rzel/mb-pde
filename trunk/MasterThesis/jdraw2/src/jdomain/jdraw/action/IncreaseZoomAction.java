package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.FolderPanel;
import jdomain.jdraw.gui.GridListener;
import jdomain.util.Log;

/*
 * IncreaseZoomAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class IncreaseZoomAction extends DrawAction implements GridListener {

   /** */
   private static final long serialVersionUID = 1L;

   protected IncreaseZoomAction() {
      super( "Increase Zoom", "zoom_in.png" );
      setToolTipText( "Zooms in one level" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( '+' ), 0 ) } );
      FolderPanel.addGridListener( this );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      if ( isEnabled() ) {
         if ( FolderPanel.getGrid() < FolderPanel.MAX_GRID ) {
            FolderPanel.setGrid( FolderPanel.getGrid() + 1 );
            Log.info( "Zoom increased." );
         }
      }
   }

   public void gridChanged( int oldValue, int newValue ) {
      setEnabled( newValue < FolderPanel.MAX_GRID );
   }

}
