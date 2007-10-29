package jdomain.jdraw.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.undo.UndoManager;

/*
 * RedoAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class RedoAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected RedoAction() {
      super( "Redo", "redo.png" );
      setToolTipText( "Restores the last undone action" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'Z' ), KeyEvent.CTRL_MASK
            + KeyEvent.SHIFT_MASK ) } );
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      UndoManager.INSTANCE.redo();
   }

}
