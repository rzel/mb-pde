package jdomain.jdraw.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.FolderPanel;
import jdomain.util.Log;

/*
 * ToggleGridAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class ToggleGridAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected ToggleGridAction() {
      super( "Hide Grid" );
      setToolTipText( "Shows or hides the grid" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'G' ), KeyEvent.CTRL_MASK ) } );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      FolderPanel p = FolderPanel.INSTANCE;
      p.setShowGrid( !p.showGrid() );
      if ( p.showGrid() ) {
         putValue( NAME, "Hide Grid" );
         Log.info( "Grid showing." );
      }
      else {
         putValue( NAME, "Show Grid" );
         Log.info( "Grid hidden." );
      }
   }

}
