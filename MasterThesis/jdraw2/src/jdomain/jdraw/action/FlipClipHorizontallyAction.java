package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.ClipPanel;
import jdomain.jdraw.gui.Tool;

/*
 * FlipClipHorizontallyAction.java - created on 14.12.2003
 * 
 * @author Michaela Behling
 */

public final class FlipClipHorizontallyAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected FlipClipHorizontallyAction() {
      super( "Flip Clip Horizontally" );
      setToolTipText( "Flips an active clip horizontally" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'H' ), 0 ) } );
      setEnabled( false );
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      ClipPanel clipPanel = Tool.getCurrentFramePanel().getLayeredPane().getClipPanel();
      clipPanel.flipHorizontally();
   }

}
