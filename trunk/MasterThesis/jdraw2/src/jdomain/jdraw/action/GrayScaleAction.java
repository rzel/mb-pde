package jdomain.jdraw.action;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;

import javax.swing.GrayFilter;

import jdomain.jdraw.gui.FolderPanel;
import jdomain.jdraw.gui.GrayScaleDialog;
import jdomain.jdraw.gui.Tool;
import jdomain.jdraw.gui.undo.DrawPixel;
import jdomain.jdraw.gui.undo.UndoManager;
import jdomain.jdraw.gui.undo.Undoable;
import jdomain.util.Log;

/*
 * GrayScaleAction.java - created on 14.12.2003
 * 
 * @author Michaela Behling
 */

public final class GrayScaleAction extends BlockingDrawAction {

	/** */
   private static final long serialVersionUID = 1L;
   private boolean brightenPixel;
	private int percentage;

	protected GrayScaleAction() {
		super("Grayscale...");
		setToolTipText("Grayscales the current frame");
	}

	public boolean prepareAction() {	
		GrayScaleDialog d = new GrayScaleDialog();
		d.open();
		if ( d.getResult() == GrayScaleDialog.APPROVE_ACTION ) {
			percentage = d.getPercentage();
			brightenPixel = d.brightenPixels();
			return true;
		}
		return false;
	}

	public void startAction() {
		Image image = FolderPanel.INSTANCE.createOffScreenImage();		
		GrayFilter filter = new GrayFilter(brightenPixel,percentage);
		ImageProducer prod = new FilteredImageSource(image.getSource(), filter);
		image = Toolkit.getDefaultToolkit().createImage(prod);			
		
		DrawPixel dp =
			Undoable.calculateDifference(
				Tool.getCurrentFrame(),
				image,
				0,
				0,
				Tool.getPictureWidth(),
				Tool.getPictureHeight());
		if (dp.isValid()) {
			UndoManager.INSTANCE.addUndoable(dp);
			dp.redo();
		}
	}

	public void finishAction() {
		Log.info("Grayscaled.");
	}
}
