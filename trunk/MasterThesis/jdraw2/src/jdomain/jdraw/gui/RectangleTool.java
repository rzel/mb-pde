package jdomain.jdraw.gui;

import jdomain.jdraw.gui.undo.DrawRectangle;
import jdomain.jdraw.gui.undo.UndoManager;

/*
 * Created on 29-Oct-2003
 *
 * @author michaela
 */

public final class RectangleTool extends RectangularSelectionTool {
	
	
	private final boolean filled;

	public RectangleTool() {
		this(false);
	}

	public RectangleTool(boolean isFilled ) {
		filled = isFilled; 
	}
	
	
	
	public boolean supportsGradientFill() {
		return filled;
	}

	protected void processSelection(int mouseButton) {
		DrawRectangle dr =
			new DrawRectangle(mouseButton, filled, start, current);
		if (dr.isValid()) {
			UndoManager.INSTANCE.addUndoable(dr);
			dr.redo();
		}
	}

}
