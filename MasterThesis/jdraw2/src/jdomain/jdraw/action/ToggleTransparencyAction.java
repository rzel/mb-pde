package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.FramePanel;
import jdomain.jdraw.gui.Tool;
import jdomain.util.Log;

/*
 * ToggleTransparencyAction - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class ToggleTransparencyAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected ToggleTransparencyAction() {
      super( "Transparency as Colour" );
      setToolTipText( "Displays transparent pixels either as pattern or as colour" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( ' ' ), 0 ) } );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      FramePanel p = Tool.getCurrentFramePanel();
      p.toggleTransparencyMode();
      Log.info( (String)getValue( NAME ) );
      if ( p.getTransparencyMode() == FramePanel.SHOW_PATTERN ) {
         putValue( NAME, "Transparency as Colour" );
      }
      else {
         putValue( NAME, "Transparency as Pattern" );
      }
   }

}
