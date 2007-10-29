package jdomain.jdraw.data;

import java.awt.Rectangle;
import java.util.Arrays;

import jdomain.jdraw.data.event.ChangeEvent;

/*
 * Clip.java - created on 30.10.2003
 * 
 * @author Michaela Behling
 */

public class Clip extends DataObject {

	protected int transColour = -1;

	private static final long serialVersionUID = 0L;
	
	protected int[][] data;
	
	public Clip(final int width, final int height, int background) {
		data = new int[height][width];
		fill(background);
	}

	public Clip(int[][] dataField) {
		data = dataField;
	}

	public final int getTransparentColour() {
		return transColour;
	}

	public final boolean usesTransparency() {
		return transColour != -1;
	}

	public void setTransparent(int index) {
		transColour = index;
	}

	protected final void setTransparentColourQuiet(int index) {
		transColour = index;
	}

	public final void fill(int colour) {
		final int height = getHeight();
		for (int i = 0; i < height; i++) {
			Arrays.fill(data[i], colour);
		}
	}

	public final void rotate() {
		final int height = getHeight();
		final int width = getWidth();		
		final int maxY = height-1;
		
		int[][] newData = new int[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				newData[x][maxY-y] = getPixel(x, y);
			}		
		}
		data = newData;
	}

	public final void flipVertically() {
		final int height = getHeight();
		final int width = getWidth();
		final int maxX = width - 1;
		int[][] newData = new int[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				newData[y][maxX - x] = getPixel(x, y);
			}
		}
		data = newData;
	}

	public final void flipHorizontally() {
		final int height = getHeight();
		final int width = getWidth();
		final int maxY = height - 1;
		int[][] newData = new int[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				newData[maxY - y][x] = getPixel(x, y);
			}
		}
		data = newData;
	}

	public void setPixels(Pixel[] pixels) {
		setPixels(pixels, false);
	}

	public void restorePixels(Pixel[] pixels) {
		setPixels(pixels, true);
	}

	private void setPixels(final Pixel[] pixels, final boolean restore) {
		final int len = pixels.length;
		Pixel p;
		if (restore) {
			for (int i = 0; i < len; i++) {
				p = pixels[i];
				setPixelQuiet(p.x, p.y, p.oldColour);
			}
		}
		else {
			for (int i = 0; i < len; i++) {
				p = pixels[i];
				setPixelQuiet(p.x, p.y, p.newColour);
			}
		}
		notifyDataListeners(new ChangeEvent(this, FRAME_NEEDS_REPAINT));
	}

	public void pasteClip(Clip clip, int startX, int startY) {
		final int minX = Math.max(0, startX);
		final int minY = Math.max(0, startY);

		final int maxX = Math.min((startX + clip.getWidth()) - 1, getWidth() - 1);
		final int maxY =
			Math.min((startY + clip.getHeight()) - 1, getHeight() - 1);

		int col;
		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				col = clip.getPixel(x - startX, y - startY);
				if (col != transColour) {
					setPixelQuiet(x, y, col);
				}
			}
		}
		notifyDataListeners(new ChangeEvent(this, CLIP_DATA_CHANGED));
	}

	public void setPixelQuiet(int x, int y, int col) {
		data[y][x] = col;
	}

	public void setPixel(int x, int y, int col) {
		if (data[y][x] != col) {
			data[y][x] = col;
			notifyDataListeners(
				new ChangeEvent(this, CLIP_PIXEL_CHANGED, x, y, col));
		}
	}

	public final Rectangle getBounds() {
		return new Rectangle(0, 0, getWidth(), getHeight());
	}

	public final int getWidth() {
		return data[0].length;
	}

	public final int getHeight() {
		return data.length;
	}

	public final int getPixel(int x, int y) {
		return data[y][x];
	}
}
