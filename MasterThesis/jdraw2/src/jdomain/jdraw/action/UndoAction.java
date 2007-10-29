package jdomain.jdraw.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.undo.UndoManager;

/*
 * UndoAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class UndoAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected UndoAction() {
      super( "Undo", "undo.png" );
      setToolTipText( "Undoes the previous action" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'Z' ), KeyEvent.CTRL_MASK ) } );
   }

   protected void _actionPerformed( ActionEvent e ) {
      UndoManager.INSTANCE.undo();
   }

}
