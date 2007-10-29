package jdomain.jdraw.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jdomain.util.Util;
import jdomain.util.gui.WidgetFactory;

/*
 * RotateDialog.java - created on 13.12.2003 by J-Domain
 * 
 * @author Michaela Behling
 */

public final class RotateDialog
	extends DrawDialog
	implements DocumentListener {

	/** */
   private static final long serialVersionUID = 1L;
   private final JTextField angleField = new JTextField(6);	

	public RotateDialog() {
		super("Rotation Dialog");

		setModal(true);
		setUndecorated(true);

		angleField.addFocusListener(WidgetFactory.TEXTFIELD_FOCUS_ADAPTER);
		angleField.setText("180");
		angleField.getDocument().addDocumentListener(this);

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
			new JLabel("<html><b>Please enter the rotation angle:</b></html>");
		label.setBorder(new EmptyBorder(0, 0, 10, 0));
		p.add(label, gc);

		gc.gridwidth = 1;
		gc.gridy++;
		label = new JLabel("Rotation angle:");
		p.add(label, gc);

		gc.gridx++;
		p.add(angleField, gc);

		main.add(p, BorderLayout.CENTER);
		setDefaultBorder();
		addRightButton(getApproveButton());
		getRootPane().setDefaultButton(getApproveButton());
		addRightButton(getCancelButton());
		addButtonPanel();
	}


	public Double getRotationAngle() {
		int angle = Util.asInt(angleField.getText().trim());
		if (angle % 360 == 0) {
			return null;
		}
		return new Double(Math.toRadians(angle));
	}

	private void checkInput() {
		String angle = angleField.getText().trim();
		getApproveButton().setEnabled(Util.isNumber(angle, -360, 360));
	}

	

	public void changedUpdate(DocumentEvent e) {
		checkInput();
	}

	public void insertUpdate(DocumentEvent e) {
		checkInput();
	}

	public void removeUpdate(DocumentEvent e) {
		checkInput();
	}
}
