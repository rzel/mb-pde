package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.MainFrame;
import jdomain.util.Log;

/*
 * ToggleViewsAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class ToggleViewsAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private boolean show = true;

   protected ToggleViewsAction() {
      super( "Hide Views" );
      setToolTipText( "Shows or hides the palette and tool views" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( '\t' ), 0 ) } );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      show = !show;
      if ( show ) {
         putValue( NAME, "Hide Views" );
         Log.info( "Views showing." );
      }
      else {
         putValue( NAME, "Show Views" );
         Log.info( "Views hidden." );
      }
      MainFrame.INSTANCE.showViews( show );
   }

}
