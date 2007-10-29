package jdomain.jdraw.action;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import jdomain.jdraw.gui.FolderPanel;
import jdomain.jdraw.gui.RotateDialog;
import jdomain.jdraw.gui.ScaleDialog;
import jdomain.jdraw.gui.Tool;
import jdomain.jdraw.gui.undo.DrawPixel;
import jdomain.jdraw.gui.undo.UndoManager;
import jdomain.jdraw.gui.undo.Undoable;
import jdomain.util.Log;

/*
 * RotateAction.java - created on 13.12.2003
 * 
 * @author Michaela Behling
 */

public final class RotateAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private Double angle;

   protected RotateAction() {
      super( "Rotate..." );
      setToolTipText( "Rotates this frame" );
   }

   public boolean prepareAction() {
      RotateDialog dialog = new RotateDialog();
      dialog.open();
      if ( dialog.getResult() == ScaleDialog.APPROVE_ACTION ) {
         angle = dialog.getRotationAngle();
         return angle != null;
      }
      return false;
   }

   public void startAction() {
      Image image = FolderPanel.INSTANCE.createOffScreenImage();
      Graphics2D g = (Graphics2D)image.getGraphics();
      AffineTransform t = g.getTransform();
      t.setToIdentity();
      final int width = Tool.getPictureWidth();
      final int height = Tool.getPictureHeight();
      t.rotate( angle.doubleValue(), width / 2, height / 2 );

      g.drawImage( image, t, new JPanel() );
      g.getTransform().setToIdentity();
      DrawPixel dp = Undoable.calculateDifference( Tool.getCurrentFrame(), image, 0, 0, width, height );
      if ( dp.isValid() ) {
         UndoManager.INSTANCE.addUndoable( dp );
         dp.redo();
      }
   }

   public void finishAction() {
      Log.info( "Frame rotated." );
   }
}
