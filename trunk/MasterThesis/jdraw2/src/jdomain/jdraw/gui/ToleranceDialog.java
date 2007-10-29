package jdomain.jdraw.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import jdomain.jdraw.data.Palette;
import jdomain.jdraw.gui.FillTool.Tolerance;
import jdomain.util.Util;
import jdomain.util.gui.WidgetFactory;

/*
 * ToleranceDialog.java - created on 19.12.2003 by J-Domain
 * 
 * @author Michaela Behling
 */

public final class ToleranceDialog
	extends DrawDialog
	implements DocumentListener {

	/** */
   private static final long serialVersionUID = 1L;
   private final JTextField redField = new JTextField(6);
	private final JTextField greenField = new JTextField(6);
	private final JTextField blueField = new JTextField(6);
	private final JTextField alphaField = new JTextField(6);
	
	public ToleranceDialog() {
		super("Tolerance Dialog");

		setModal(true);
		setUndecorated(true);

		Tolerance t = FillTool.INSTANCE.getTolerance();

		redField.addFocusListener(WidgetFactory.TEXTFIELD_FOCUS_ADAPTER);
		redField.setText(String.valueOf(t.redDiff));
		redField.getDocument().addDocumentListener(this);

		greenField.addFocusListener(WidgetFactory.TEXTFIELD_FOCUS_ADAPTER);
		greenField.setText(String.valueOf(t.greenDiff));
		greenField.getDocument().addDocumentListener(this);

		blueField.addFocusListener(WidgetFactory.TEXTFIELD_FOCUS_ADAPTER);
		blueField.setText(String.valueOf(t.blueDiff));
		blueField.getDocument().addDocumentListener(this);

		alphaField.addFocusListener(WidgetFactory.TEXTFIELD_FOCUS_ADAPTER);
		alphaField.setText(String.valueOf(t.alphaDiff));
		alphaField.getDocument().addDocumentListener(this);

		JPanel p = new JPanel(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.WEST;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 2;
		gc.gridheight = 1;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.insets = new Insets(0, 2, 2, 2);

		JLabel label =
			new JLabel("<html><b>Please enter fill tolerance values (0-255): </b></html>");
		label.setBorder(new EmptyBorder(0, 0, 10, 0));
		p.add(label, gc);

		gc.gridwidth = 1;
		gc.gridy++;
		label = new JLabel("Red Tolerance:");
		p.add(label, gc);

		gc.gridx++;
		p.add(redField, gc);

		gc.gridx = 0;
		gc.gridy++;
		label = new JLabel("Green Tolerance:");
		p.add(label, gc);
		
		gc.gridx++;
		p.add(greenField, gc);

		gc.gridx = 0;
		gc.gridy++;
		label = new JLabel("Blue Tolerance:");
		p.add(label, gc);

		gc.gridx++;
		p.add(blueField, gc);

		gc.gridx = 0;
		gc.gridy++;
		label = new JLabel("Alpha Tolerance:");
		p.add(label, gc);

		gc.gridx++;
		p.add(alphaField, gc);

		main.add(p, BorderLayout.CENTER);
		setDefaultBorder();
		addRightButton(getApproveButton());
		getRootPane().setDefaultButton(getApproveButton());
		addRightButton(getCancelButton());
		addButtonPanel();
		
		JButton reset = new JButton("Reset");
		reset.setMnemonic('r');
		reset.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e ) {
				redField.setText("0");
				greenField.setText("0");
				blueField.setText("0");
				alphaField.setText("0");
			}
		} );
		addLeftButton(reset);
	}

	public void approve() {
		int r = Util.asInt(redField.getText().trim());
		int g = Util.asInt(greenField.getText().trim());
		int b = Util.asInt(blueField.getText().trim());
		int a = Util.asInt(alphaField.getText().trim());
		FillTool.INSTANCE.setTolerance( new Tolerance(r,g,b,a));
		super.approve();
	}

	private void checkInput() {
		String[] f = new String[4];
		f[0] = redField.getText().trim();
		f[1] = greenField.getText().trim();
		f[2] = blueField.getText().trim();
		f[3] = alphaField.getText().trim();

		boolean ok = true;
		for (int i = 0; i < 4; i++) {
			if ((!Util.isNumber(f[i]))
				|| (!Util.isIn(Util.asInt(f[i]), 0, Palette.MAX_RGB_VALUE))) {
				ok = false;
			}
		}
		getApproveButton().setEnabled(ok);
	}

	private void update(Document doc) {
		checkInput();
	}

	public void changedUpdate(DocumentEvent e) {
		update(e.getDocument());
	}

	public void insertUpdate(DocumentEvent e) {
		update(e.getDocument());
	}

	public void removeUpdate(DocumentEvent e) {
		update(e.getDocument());
	}
}
