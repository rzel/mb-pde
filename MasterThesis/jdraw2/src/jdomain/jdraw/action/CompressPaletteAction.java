package jdomain.jdraw.action;

import jdomain.jdraw.gui.PixelTool;
import jdomain.jdraw.gui.Tool;
import jdomain.jdraw.gui.ToolPanel;

/*
 * CompressPaletteAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class CompressPaletteAction extends BlockingDrawAction {

	/** */
   private static final long serialVersionUID = 1L;

   private int result;

//	private String fileName;
//	private boolean success;

	protected CompressPaletteAction() {
		super("Remove unused colours");
		setToolTipText("Removes all unused colours");
	}

	public boolean prepareAction() {
		return true;
	}

	public void startAction() {
		ToolPanel.INSTANCE.setCurrentTool(
			PixelTool.INSTANCE,
			DrawAction.getAction(SetPixelToolAction.class));
		result = Tool.getCurrentPalette().compress();
	}

	public void finishAction() {
		CompressAction.showResult(result);
	}
}
