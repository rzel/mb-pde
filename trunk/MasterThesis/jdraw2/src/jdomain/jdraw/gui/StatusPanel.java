package jdomain.jdraw.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import jdomain.util.Log;
import jdomain.util.LogListener;
import jdomain.util.Util;
import jdomain.util.gui.AntialiasPanel;
import jdomain.util.gui.GUIUtil;

/*
 * Created on 28-Oct-2003
 *
 * @author michaela
 */

public final class StatusPanel extends AntialiasPanel implements LogListener {

	/** */
   private static final long serialVersionUID = 1L;

   public static final StatusPanel INSTANCE = new StatusPanel();

	private final JLabel text = new JLabel(" ");
	private final JLabel draw = new JLabel();

	private StatusPanel() {
		super(new BorderLayout(0, 0));
		add(text, BorderLayout.CENTER);
		Log.addLogListener(this);
		setBorder(new EmptyBorder(2, 8, 2, 8));
		text.setFont(MainFrame.SMALL_FONT);

		draw.setFont(MainFrame.SMALL_FONT);
		draw.setHorizontalAlignment(SwingConstants.RIGHT);
		add(draw, BorderLayout.EAST);
	}

	public void drawInfo(String message) {
		draw.setText(message);
	}

	// LogListener

	public void debug(String message) {
		//print(Color.darkGray, message);
	}

	public void info(String message) {
		print(message);
	}

	public void warning(String message) {
		GUIUtil.warning(MainFrame.INSTANCE, message);
		print(Color.blue, message);
	}

	public void warning(String message, Object o) {
		print(Color.blue, message + " [" + o.toString() + "]");
	}

	public void error(String message) {
		GUIUtil.error(MainFrame.INSTANCE, message);
		print(Color.red, message);
	}

	public void exception(Throwable e) {
		String s = e.getMessage();
		if ((s == null) || (s.trim().length() == 0)) {
			s =
				"An error occurred! Giving up...\n"
					+ "ExceptionType: "
					+ Util.shortClassName(e.getClass());
		}
		GUIUtil.error(MainFrame.INSTANCE, s);
		print(Color.red, e.getMessage());
	}

	public void close() {
	}

	private void print(Color col, String aText) {
		if ((aText == null) || aText.equals("")) {
			aText = " ";
		}
		text.setForeground(col);
		text.setText(aText);
	}

	private void print(String message) {
		print(Color.black, message);
	}

}
