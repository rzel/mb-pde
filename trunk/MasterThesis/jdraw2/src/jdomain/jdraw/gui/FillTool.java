package jdomain.jdraw.gui;

import java.awt.Point;
import java.awt.event.KeyEvent;

import jdomain.jdraw.gui.undo.DrawFill;
import jdomain.jdraw.gui.undo.UndoManager;

/* 
 *
 * @author michaela
 */

public final class FillTool extends Tool {

	private Tolerance tolerance = new Tolerance(0, 0, 0, 0);

	public static final FillTool INSTANCE = new FillTool();

	private FillTool() {
	}

	public void clicked(int button, Point p, int modifier ) {
		if (p != null) {
			boolean completeFill = ((modifier & KeyEvent.CTRL_MASK)!=0);
			DrawFill f = new DrawFill(button, p.x, p.y, completeFill);
			if (f.isValid()) {
				UndoManager.INSTANCE.addUndoable(f);
				f.redo();
			}
		}
	}

	public Tolerance getTolerance() {
		return tolerance;
	}

	public void setTolerance(Tolerance aTolerance) {
		this.tolerance = aTolerance;
	}

	public static final class Tolerance {
		public final int redDiff;
		public final int greenDiff;
		public final int blueDiff;
		public final int alphaDiff;

		public Tolerance(int r, int g, int b, int alpha) {
			redDiff = r;
			greenDiff = g;
			blueDiff = b;
			alphaDiff = alpha;
		}
	}

}
