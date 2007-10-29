package jdomain.jdraw.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jdomain.util.Util;
import jdomain.util.gui.WidgetFactory;

/*
 * GrayScaleDialog.java - created on 14.12.2003
 * 
 * @author Michaela Behling
 */

public class GrayScaleDialog extends DrawDialog implements DocumentListener {

	/** */
   private static final long serialVersionUID = 1L;
   private static int percentage = 50;
	private static boolean brighten = true;
	private final JTextField percentageField =
		new JTextField(4);

	private final JCheckBox brightenBox = new JCheckBox("Brighten pixels");

	public GrayScaleDialog() {
		super("Grayscale");

		WidgetFactory.addFocusAdapter(percentageField);

		setDefaultBorder();
		setModal(true);
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		percentageField.getDocument().addDocumentListener(this);
		percentageField.setText(String.valueOf(percentage));
		brightenBox.setSelected(brighten);

		gc.gridx = 0;
		gc.gridy = 0;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.insets = new Insets(0, 2, 2, 2);
		gc.gridwidth = 2;
		JLabel text =
			new JLabel("<html><b>Please specify the grayscale filter:</b></html>");
		text.setBorder(new EmptyBorder(0, 0, 10, 0));
		p.add(text, gc);

		gc.gridwidth = 1;
		gc.gridy++;
		JLabel l = new JLabel("Gray in %: ");
		p.add(l, gc);

		gc.gridx++;
		p.add(percentageField, gc);

		gc.gridx = 0;
		gc.gridy++;
		gc.gridwidth = 2;
		p.add(brightenBox, gc);

		main.add(p, BorderLayout.CENTER);

		addRightButton(getApproveButton());
		getRootPane().setDefaultButton(getApproveButton());

		addRightButton(getCancelButton());
		addButtonPanel();

		setFirstFocusComponent(percentageField);
	}

	public int getPercentage() {
		percentage = Util.asInt(percentageField.getText().trim());
		return percentage;
	}

	public boolean brightenPixels() {
		brighten = brightenBox.isSelected();
		return brighten;
	}

	private void checkInput() {
		String p = percentageField.getText().trim();
		if (!Util.isNumber(p)) {
			getApproveButton().setEnabled(false);
		}
		else {
			getApproveButton().setEnabled(Util.isIn(Util.asInt(p), 0, 100));
		}
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
