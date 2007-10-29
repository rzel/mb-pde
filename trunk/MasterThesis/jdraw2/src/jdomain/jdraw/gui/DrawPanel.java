package jdomain.jdraw.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import jdomain.jdraw.data.Clip;
import jdomain.jdraw.data.Palette;

/*
 * Created on 28-Oct-2003
 *
 * @author michaela
 */

public class DrawPanel extends JPanel {

	/** */
   private static final long serialVersionUID = 1L;

   private static final Color TRANS_COL = new Color(0, 0, 0, 0);

	protected Clip clip;

	public DrawPanel() {
		setOpaque(false);
	}

	public Clip getClip() {
		return clip;
	}

	public int getGrid() {
		return Tool.getGrid();
	}

	protected void setClip(Clip aClip) {
		clip = aClip;
		setPreferredSize(getPreferredSize());
	}

	public boolean showGrid() {
		return FolderPanel.INSTANCE.showGrid();
	}

	public final void paintClip(
		Graphics gr,
		final boolean drawTransparentColour) {
		Graphics2D g = (Graphics2D)gr;
		Palette pal = Tool.getCurrentPalette();
		final int transCol = Tool.getPicture().getTransparent();
		final boolean showGrid = showGrid();

		final int w = clip.getWidth();
		final int h = clip.getHeight();
		final int grid = getGrid();

		int x = 0;
		int y = 0;

		for (int row = 0; row < h; row++) {
			for (int column = 0; column < w; column++) {

				// pixel
				int c = clip.getPixel(column, row);				
				if (c == transCol) {
					if (drawTransparentColour) {
						g.setColor(TRANS_COL);
						g.fillRect(x, y, grid, grid);
					}
				}
				else {
					g.setColor(pal.getColour(c).getColour());
					g.fillRect(x, y, grid, grid);
				}
				// grid
				if (showGrid) {
					g.setColor(Color.darkGray);
					g.drawRect(x, y, grid, grid);
				}
				x = x + grid;
			}
			x = 0;
			y = y + grid;
		}
	}

	public void setPreferredSize(Dimension dim) {
		super.setPreferredSize(dim);
		setBounds(0, 0, dim.width, dim.height);
	}

	public final void paint(Graphics g) {
		super.paint(g);

		if (clip == null) {
			return;
		}
		paintClip(g, false);
	}

	public Dimension getPreferredSize() {
		if (clip == null) {
			return super.getPreferredSize();
		}
		return new Dimension(
			(getGrid() * clip.getWidth()) + 1,
			(getGrid() * clip.getHeight()) + 1);
	}

}