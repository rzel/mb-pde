package jdomain.jdraw.action;

import jdomain.jdraw.gui.DrawDialog;
import jdomain.jdraw.gui.MainFrame;
import jdomain.jdraw.gui.SizeDialog;
import jdomain.jdraw.gui.undo.UndoManager;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

/*
 * ResizeAction.java - created on 30.10.2003
 * 
 * @author Michaela Behling
 */

public final class ResizeAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private Dimension dimension;

   public ResizeAction() {
      super( "Resize...", "resize.png" );
      setToolTipText( "Resizes the current image" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'R' ), KeyEvent.CTRL_MASK ) } );
   }

   public boolean prepareAction() {
      SizeDialog d = new SizeDialog();
      d.open();
      if ( d.getResult() == DrawDialog.APPROVE_ACTION ) {
         dimension = d.getInput();
         return true;
      }
      return false;
   }

   public void startAction() {
      MainFrame.INSTANCE.getPicture().setSize( dimension.width, dimension.height );
   }

   public void finishAction() {
      UndoManager.INSTANCE.reset();
   }

}
