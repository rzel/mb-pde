package jdomain.jdraw.gui.undo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gui.FolderPanel;
import jdomain.jdraw.gui.GradientPanel;
import jdomain.jdraw.gui.Tool;

/*
 * DrawRectangle.java - created on 14.11.2003
 * 
 * @author Michaela Behling
 */

public final class DrawRectangle extends Undoable {

	private final DrawPixel draw;

	public DrawRectangle(
		int mouseButton,
		boolean filled,
		Point start,
		Point end) {
		Color col;
		Palette pal = Tool.getCurrentPalette();
		Picture pic = Tool.getPicture();
		if (mouseButton == Tool.LEFT_BUTTON) {
			col = pal.getColour(pic.getForeground()).getColour();
		}
		else {
			col = pal.getColour(pic.getBackground()).getColour();
		}

		draw = savePixels(col, filled, start, end);
	}

	public boolean isValid() {
		return draw != null;
	}

	private DrawPixel savePixels(
		Color col,
		boolean filled,
		Point start,
		Point end) {
		int minX = Math.min(start.x, end.x);
		int maxX = Math.max(start.x, end.x);
		int minY = Math.min(start.y, end.y);
		int maxY = Math.max(start.y, end.y);
		int w = (maxX - minX) + 1;
		int h = (maxY - minY) + 1;

		Image image = FolderPanel.INSTANCE.createOffScreenImage(minX, minY, w, h);
		Graphics2D g = (Graphics2D) image.getGraphics();
		prepareGraphics(g);
		if (filled && Tool.isGradientFillOn()) {
			Rectangle r = new Rectangle(minX, minY, w, h);
			g.setPaint(GradientPanel.INSTANCE.createGradient(r));
		}
		else {
			g.setColor(col);
		}

		g.translate(-minX, -minY);
		if (filled) {
			g.fillRect(minX, minY, w, h);
		}
		else {
			g.drawRect(minX, minY, w - 1, h - 1);
		}
		return calculateDifference(frame, image, minX, minY, w, h);
	}

	public void redo() {
		draw.redo();
	}

	public void undo() {
		draw.undo();
	}

}
