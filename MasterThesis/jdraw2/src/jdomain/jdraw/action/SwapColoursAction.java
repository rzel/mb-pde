package jdomain.jdraw.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gui.Tool;
import jdomain.util.Log;

/*
 * SwapColoursAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SwapColoursAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SwapColoursAction() {
      super( "Swap Colours" );
      setToolTipText( "Swaps foreground with background colour" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'M' ), KeyEvent.CTRL_MASK ) } );
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      Picture pic = Tool.getPicture();
      Palette pal = Tool.getCurrentPalette();
      pal.swapColours( pic, pic.getForeground(), pic.getBackground() );
      Log.info( "Swapped colours #" + String.valueOf( pic.getForeground() ) + " and #"
            + String.valueOf( pic.getBackground() ) );
   }

}
