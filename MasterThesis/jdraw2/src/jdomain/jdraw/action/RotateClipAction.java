package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.ClipPanel;
import jdomain.jdraw.gui.Tool;

/*
 * RotateClipAction.java - created on 14.12.2003
 * 
 * @author Michaela Behling
 */

public final class RotateClipAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected RotateClipAction() {
      super( "Rotate Clip 90°" );
      setToolTipText( "Rotates an active 90° clock-wise" );
      setEnabled( false );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'R' ), 0 ) } );
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      ClipPanel clipPanel = Tool.getCurrentFramePanel().getLayeredPane().getClipPanel();
      clipPanel.rotate();
   }

}
