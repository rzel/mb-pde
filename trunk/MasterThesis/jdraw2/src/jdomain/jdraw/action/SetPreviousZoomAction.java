package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.FolderPanel;
import jdomain.jdraw.gui.GridListener;
import jdomain.util.Log;

/*
 * SetPreviousZoomAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SetPreviousZoomAction extends DrawAction implements GridListener {

   /** */
   private static final long serialVersionUID = 1L;
   private int zoom = -1;

   protected SetPreviousZoomAction() {
      super( "Set Previous Zoom" );
      setToolTipText( "Sets previous zoom level" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( '8' ), 0 ) } );
      setEnabled( false );
      FolderPanel.addGridListener( this );
   }

   private void setPreviousZoom( int level ) {
      zoom = level;
      setEnabled( true );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      if ( isEnabled() ) {
         FolderPanel.setGrid( zoom );
         setEnabled( false );
         Log.info( "Previous zoom level set." );
      }
   }

   public void gridChanged( int oldValue, int newValue ) {
      setPreviousZoom( oldValue );
   }

}
