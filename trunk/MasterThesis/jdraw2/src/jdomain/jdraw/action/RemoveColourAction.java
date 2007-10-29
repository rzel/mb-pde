package jdomain.jdraw.action;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gui.PixelTool;
import jdomain.jdraw.gui.Tool;
import jdomain.jdraw.gui.ToolPanel;
import jdomain.util.Log;

/*
 * RemoveColourAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class RemoveColourAction extends BlockingDrawAction {

	/** */
   private static final long serialVersionUID = 1L;

   protected RemoveColourAction() {
		super("Remove Colour");
		setToolTipText("Removes the current foreground colour from this palette");
		setAccelerators(
			new KeyStroke[] {
				 KeyStroke.getKeyStroke(new Character('D'), KeyEvent.CTRL_MASK)});
	}

	public boolean prepareAction() {
		return true;
	}

	public void startAction() {
		ToolPanel.INSTANCE.setCurrentTool(
			PixelTool.INSTANCE,
			DrawAction.getAction(SetPixelToolAction.class));
		Picture pic = Tool.getPicture();
		Palette pal = Tool.getCurrentPalette();
		final int index = pic.getForeground();
		pal.removeColour(index);
		Log.info("Colour #" + String.valueOf(index) + " removed.");
	}

	public void finishAction() {
	}
}
