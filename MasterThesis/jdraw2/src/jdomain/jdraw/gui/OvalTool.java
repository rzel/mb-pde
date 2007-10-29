package jdomain.jdraw.gui;

import jdomain.jdraw.gui.undo.DrawOval;
import jdomain.jdraw.gui.undo.UndoManager;

/* 
 *
 * @author michaela
 */

public final class OvalTool extends RectangularSelectionTool {

	private final boolean fillOval;

	public OvalTool() {
		this(false);
	}

	public OvalTool(boolean doFill) {
		fillOval = doFill;
	}

	protected void processSelection(int mouseButton) {
		DrawOval dov = new DrawOval(mouseButton, fillOval, start, current);
		if (dov.isValid()) {
			UndoManager.INSTANCE.addUndoable(dov);
			dov.redo();
		}
	}

	public boolean supportsGradientFill() {
		return fillOval;
	}

	public boolean supportsAntialias() {
		return true;
	}

}
