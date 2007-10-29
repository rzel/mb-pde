package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.FolderPanel;
import jdomain.jdraw.gui.GridListener;
import jdomain.util.Log;

/*
 * SetMinZoomAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SetMinZoomAction extends DrawAction implements GridListener {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetMinZoomAction() {
      super( "Minimum Zoom" );
      setToolTipText( "Displays the image in its current dimension" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( '0' ) } );
      FolderPanel.addGridListener( this );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      FolderPanel.setGrid( FolderPanel.MIN_GRID );
      Log.info( "Minimum zoom set." );
   }

   public void gridChanged( int oldValue, int newValue ) {
      setEnabled( newValue > FolderPanel.MIN_GRID );
   }

}
