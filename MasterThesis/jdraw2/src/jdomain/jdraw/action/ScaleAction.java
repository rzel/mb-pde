package jdomain.jdraw.action;

import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.ScaleDialog;
import jdomain.jdraw.gui.Tool;
import jdomain.jdraw.gui.undo.UndoManager;
import jdomain.util.Log;

/*
 * ScaleAction.java - created on 11.12.2003
 * 
 * @author Michaela Behling
 */

public final class ScaleAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private Dimension dimension;
   private int scaleStyle = 0;

   protected ScaleAction() {
      super( "Scale Image..." );
      setToolTipText( "Scales this picture" );

      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'C' ), KeyEvent.CTRL_MASK ) } );
   }

   public boolean prepareAction() {
      ScaleDialog dialog = new ScaleDialog();
      dialog.open();
      if ( dialog.getResult() == ScaleDialog.APPROVE_ACTION ) {
         dimension = dialog.getScalingDimension();
         scaleStyle = dialog.getScalingStyle();
         return (dimension.width != Tool.getPictureWidth()) || (dimension.height != Tool.getPictureHeight());
      }
      return false;
   }

   public void startAction() {
      Tool.getPicture().scale( dimension, scaleStyle );
      UndoManager.INSTANCE.reset();
   }

   public void finishAction() {
      Log.info( "Image scaled." );
   }
}
