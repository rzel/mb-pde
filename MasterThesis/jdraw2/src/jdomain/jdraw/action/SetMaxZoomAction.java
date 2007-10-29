package jdomain.jdraw.action;

import jdomain.jdraw.gui.FolderPanel;
import jdomain.jdraw.gui.GridListener;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.util.Log;

/*
 * SetMaxZoomAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SetMaxZoomAction extends DrawAction implements GridListener {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetMaxZoomAction() {
      super( "Maximum Zoom" );
      setToolTipText( "Sets the maximum zoom level" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( '9' ) } );
      FolderPanel.addGridListener( this );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      FolderPanel.setGrid( FolderPanel.MAX_GRID );
      Log.info( "Maximum zoom set." );
   }

   public void gridChanged( int oldValue, int newValue ) {
      setEnabled( newValue != FolderPanel.MAX_GRID );
   }

}
