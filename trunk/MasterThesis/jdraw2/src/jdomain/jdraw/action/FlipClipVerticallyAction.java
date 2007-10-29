package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.ClipPanel;
import jdomain.jdraw.gui.Tool;

/*
 * FlipClipVerticallyAction.java - created on 14.12.2003
 * 
 * @author Michaela Behling
 */

public final class FlipClipVerticallyAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected FlipClipVerticallyAction() {
      super( "Flip Clip Vertically" );
      setToolTipText( "Flips an active clip vertically" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'V' ), 0 ) } );
      setEnabled( false );
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      ClipPanel clipPanel = Tool.getCurrentFramePanel().getLayeredPane().getClipPanel();
      clipPanel.flipVertically();
   }

}
