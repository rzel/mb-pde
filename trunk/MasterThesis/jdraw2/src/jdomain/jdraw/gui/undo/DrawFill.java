package jdomain.jdraw.gui.undo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import jdomain.jdraw.data.ColourEntry;
import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.data.Pixel;
import jdomain.jdraw.gui.FillTool;
import jdomain.jdraw.gui.Tool;

/*
 * DrawFill.java - created on 15.11.2003
 * 
 * @author Michaela Behling
 */

public final class DrawFill extends Undoable {

	private final ArrayList pixels = new ArrayList();
	private final int oldCol;
	private final int newCol;
	private final int width;
	private final int height;
	private final DrawPixel drawPixel;

	public DrawFill(int mouseButton, int x, int y, boolean completeFill) {
		width = frame.getWidth();
		height = frame.getHeight();
		oldCol = frame.getPixel(x, y);
		Picture pic = Tool.getPicture();
		if (mouseButton == Tool.LEFT_BUTTON) {
			newCol = pic.getForeground();
		}
		else {
			newCol = pic.getBackground();
		}
		if (newCol != oldCol) {
			if (completeFill) {
				drawPixel = saveComplete();
			}
			else {
				savePixels(x, y);
				drawPixel = null;
			}
		}
		else {
			drawPixel = null;
		}
	}

	public boolean isValid() {
		return ((drawPixel != null) && drawPixel.isValid())
			|| (pixels.size() > 0);
	}

	private boolean isValid(int x, int y) {
		return (x >= 0) && (x < width) && (y >= 0) && (y < height);
	}

	private boolean hasColour(Point p, int col) {
		return hasColour(p.x, p.y, col);
	}

	private boolean hasColour(int x, int y, int col) {
		FillTool.Tolerance t = FillTool.INSTANCE.getTolerance();
		Palette pal = Tool.getCurrentPalette();
		ColourEntry a = pal.getColour(col);
		ColourEntry b = pal.getColour(frame.getPixel(x, y));
		int rDiff = Math.abs(a.getRed() - b.getRed());
		int gDiff = Math.abs(a.getGreen() - b.getGreen());
		int bDiff = Math.abs(a.getBlue() - b.getBlue());
		int aDiff = Math.abs(a.getAlpha() - b.getAlpha());

		return (rDiff <= t.redDiff)
			&& (gDiff <= t.greenDiff)
			&& (bDiff <= t.blueDiff)
			&& (aDiff <= t.alphaDiff);
	}

	private DrawPixel saveComplete() {
		ArrayList list = new ArrayList();
		int col;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				col = frame.getPixel(x, y);
				if (hasColour(x, y, oldCol)) {
					list.add(new Pixel(x, y, col, newCol));
				}
			}
		}
		Pixel[] pixelField = new Pixel[list.size()];
		list.toArray(pixelField);
		return new DrawPixel(pixelField);
	}

	private void savePixels(int x, int y) {
		// field: -1: not looked at, 0: looked at, don't change, 1: looked at, change
		int[][] data = new int[height][width];
		for (int i = 0; i < height; i++) {
			Arrays.fill(data[i], -1);
		}
		ArrayList points = new ArrayList(width * height);
		points.add(new Point(x, y));
		Point p;
		int pixel;
		do {
			p = (Point) points.remove(points.size() - 1);
			if (data[p.y][p.x] == -1) {
				pixel = (hasColour(p, oldCol)) ? 1 : 0;
				data[p.y][p.x] = pixel;

				if (pixel == 1) {
					//	north
					if (isValid(p.x, p.y - 1)) {
						if (data[p.y - 1][p.x] == -1) {
							points.add(new Point(p.x, p.y - 1));
						}
					}
//					// northeast
//					if (isValid(p.x + 1, p.y - 1)) {
//						if (data[p.y - 1][p.x + 1] == -1) {
//							points.add(new Point(p.x + 1, p.y - 1));
//						}
//					}
					// east
					if (isValid(p.x + 1, p.y)) {
						if (data[p.y][p.x + 1] == -1) {
							points.add(new Point(p.x + 1, p.y));
						}
					}
//					// southeast
//					if (isValid(p.x + 1, p.y + 1)) {
//						if (data[p.y + 1][p.x + 1] == -1) {
//							points.add(new Point(p.x + 1, p.y + 1));
//						}
//					}
					//	south
					if (isValid(p.x, p.y + 1)) {
						if (data[p.y + 1][p.x] == -1) {
							points.add(new Point(p.x, p.y + 1));
						}
					}
//					// southwest
//					if (isValid(p.x - 1, p.y + 1)) {
//						if (data[p.y + 1][p.x - 1] == -1) {
//							points.add(new Point(p.x - 1, p.y + 1));
//						}
//					}
					// west				
					if (isValid(p.x - 1, p.y)) {
						if (data[p.y][p.x - 1] == -1) {
							points.add(new Point(p.x - 1, p.y));
						}
					}
//					// northwest
//					if (isValid(p.x - 1, p.y - 1)) {
//						if (data[p.y - 1][p.x - 1] == -1) {
//							points.add(new Point(p.x - 1, p.y - 1));
//						}
//					}
				}
			}
		}
		while (points.size() != 0);
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				if (data[r][c] == 1) {
					pixels.add(new Point(c, r));
				}
			}
		}
	}

	private void setPixel(int col) {
		Iterator it = pixels.iterator();
		Point p;
		while (it.hasNext()) {
			p = (Point) it.next();
			frame.setPixelQuiet(p.x, p.y, col);
		}
		Tool.getCurrentFramePanel().changeNeedsRepaint(null);
	}

	public void redo() {
		if (drawPixel != null) {
			drawPixel.redo();
		}
		else {
			setPixel(newCol);
		}
	}

	public void undo() {
		if (drawPixel != null) {
			drawPixel.undo();
		}
		else {
			setPixel(oldCol);
		}
	}

}
