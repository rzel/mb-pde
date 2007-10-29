package jdomain.jdraw.gui;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import jdomain.jdraw.data.Clip;
import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gui.undo.DrawPixel;
import jdomain.jdraw.gui.undo.Undoable;
import jdomain.util.gui.TextCalculator;

/*
 * Created on 29-Oct-2003
 *
 * @author michaela
 */

public final class TextTool extends RectangularSelectionTool {

	public static final TextTool INSTANCE = new TextTool();

	private final ClipPanel textPanel = new ClipPanel(false);
	private Clip clip;
	private TextCalculator calc;
	private Image img;
	private int background;
	private Font font;
	private String text;
	private boolean drawOutline = false;

	private TextTool() {
	}

	protected void processSelection(int mouseButton) {
		final int w = calc.getTextWidth();
		final int h = calc.getTextHeight();
		// text clip kann größer sein als das bild selbst. 
		// darum wird nicht frame.createClip aufrufen!

		clip = new Clip(w, h, Tool.getPicture().getPictureBackground());
		clip.setTransparent(Tool.getPicture().getTransparent());
		clip.fill(background);
		DrawPixel pixel = Undoable.calculateDifference(clip, img, 0, 0, w, h);
		pixel.redo();
		textPanel.setClip(0, 0, clip);		
	}

	public Clip createClip(int x, int y, int w, int h) {
		img = FolderPanel.INSTANCE.createOffScreenImage(x, y, w, h);
		Graphics2D g = (Graphics2D) img.getGraphics();
		drawText(g);
		clip = new Clip(w, h, Tool.getPicture().getPictureBackground());
		clip.setTransparent(Tool.getPicture().getTransparent());
		DrawPixel pixel = Undoable.calculateDifference(clip, img, 0, 0, w, h);
		pixel.redo();
		textPanel.setClip(x, y, clip);

		return clip;
	}

	public void process(Font aFont, String aText, boolean doOutline) {
		font = aFont;
		text = aText;
		drawOutline = doOutline;
		boolean antialias = Tool.isAntialiasOn();
		calc = new TextCalculator(0, text, font, antialias);
		img =
			Tool.getDrawPanel().createImage(
				calc.getTextWidth(),
				calc.getTextHeight());
		// prepare text graphics
		Graphics2D g = (Graphics2D) img.getGraphics();
		Palette pal = Tool.getCurrentPalette();
		Picture pic = Tool.getPicture();
		background = pic.getTransparent();
		if (background == -1) {
			background = pic.getBackground();
		}
		g.setColor(pal.getColour(pic.getBackground()).getOpaqueColour());
		g.fillRect(0, 0, calc.getTextWidth(), calc.getTextHeight());
		drawText(g);
		processSelection(LEFT_BUTTON);
	}

	private void drawText(Graphics2D g) {
		Palette pal = Tool.getCurrentPalette();
		Picture pic = Tool.getPicture();
		if (Tool.isGradientFillOn()) {
			g.setColor(pal.getColour(pic.getForeground()).getOpaqueColour());
			(g).setPaint(
				GradientPanel.INSTANCE.createGradient(
					new Dimension(calc.getTextWidth(), calc.getTextHeight())));
		}
		else {
			g.setColor(pal.getColour(pic.getForeground()).getOpaqueColour());
		}

		g.setFont(font);
		if (Tool.isAntialiasOn()) {
			g.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		}

		if (drawOutline) {
			g.setStroke(new BasicStroke(1.0f));
		}
		// write text		
		String[] lines = calc.getLines();
		TextLayout tl;
		AffineTransform at;
		FontRenderContext fc = new FontRenderContext(null, false, false);
		int y = (int) calc.getMetrics().getAscent();
		for (int i = 0; i < lines.length; i++) {
			if (drawOutline) {
				tl = new TextLayout(lines[i], font, fc);
				at = new AffineTransform();
				at.translate(0, y);
				Shape shape = tl.getOutline(at);
				g.draw(shape);
			}
			else {
				g.drawString(lines[i], 0, y);
			}
			y = y + calc.getLineHeight();
		}
	}

	public boolean supportsAntialias() {
		return true;
	}

	public void activate() {
		if (!isActive) {
			super.activate();
			MouseHandler.INSTANCE.addClip(textPanel);
			FolderPanel.addGridListener(textPanel);
		}
	}

	public void deactivate() {
		if (isActive) {
			super.deactivate();
			textPanel.deactivate();
			MouseHandler.INSTANCE.deleteClip(textPanel);
			MainFrame.INSTANCE.setCursor(Cursor.getDefaultCursor());
			textPanel.deactivate();
			FolderPanel.removeGridListener(textPanel);
		}
	}

	public boolean supportsGradientFill() {
		return true;
	}

}
