package jdomain.jdraw.action;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.PixelTool;
import jdomain.jdraw.gui.Tool;
import jdomain.jdraw.gui.ToolPanel;
import jdomain.util.Log;

/*
 * CompressAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class CompressAction extends BlockingDrawAction {

	/** */
   private static final long serialVersionUID = 1L;

   private int freedColours;

//	private String fileName;
//	private boolean success;

	protected CompressAction() {
		super("Compress");
		setToolTipText("Optimizes the used palettes");
		setAccelerators(
			new KeyStroke[] {
				 KeyStroke.getKeyStroke(new Character('K'), KeyEvent.CTRL_MASK)});
	}

	public boolean prepareAction() {
		return true;
	}

	public void startAction() {
		ToolPanel.INSTANCE.setCurrentTool(
			PixelTool.INSTANCE,
			DrawAction.getAction(SetPixelToolAction.class));
		freedColours = Tool.getPicture().compress();
	}

	public static void showResult(int freedCols) {
		switch (freedCols) {
			case 0 :
				Log.info("No colours were freed.");
				break;
			case 1 :
				Log.info("One colour was freed.");
				break;
			default :
				Log.info(String.valueOf(freedCols) + " colours were freed.");
		}
	}

	public void finishAction() {
		showResult(freedColours);
	}
}
