/*******************************************************************
 *
 *  $Author: Michaela $
 *  $Date: 2005/12/14 12:54:26 $
 *  $Revision: 1.3 $
 *
 *******************************************************************/

package jdomain.jdraw.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import jdomain.util.Log;
import jdomain.util.gui.BrowserFilter;
import jdomain.util.gui.ScrollablePanel;
import jdomain.util.gui.StandardDialog;

public class SearchFrame extends StandardDialog {
	///////////////////////////////////////////////////// Constants

	/** */
   private static final long serialVersionUID = 1L;
   private static final int MAX_LENGTH = 50;
	private static int rows;
	private static int columns;

	static {
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension dim = t.getScreenSize();
		dim.width = (int) (( dim.width) * 0.8);
		dim.height = (int) (( dim.height) * 0.8);
		columns = dim.width / MAX_LENGTH;
		rows = dim.height / MAX_LENGTH;
	}

	private static final int INSET = 4;

	private static final Font PLAIN = new Font("SansSerif", Font.PLAIN, 12);

	private static final BrowserFilter[] FILTER =
		new BrowserFilter[] {
			DrawBrowser.ALL_IMAGES_READ_FILTER,
			DrawBrowser.DRAW_FILTER,
			DrawBrowser.GIF_FILTER,
			DrawBrowser.ICO_FILTER,
			DrawBrowser.JPG_FILTER,
			DrawBrowser.PNG_FILTER };

	///////////////////////////////////////////////////// Fields

	private final JPanel settings = new JPanel(new GridBagLayout());

	private final JComboBox extBox = new JComboBox(FILTER);
	private JTextField dirField;
	private JLabel info;
	private final IconListener listener = new IconListener();

	private int colCount = 0;
	private final GridBagConstraints grid = new GridBagConstraints();

	private JScrollPane pane;
	private String directory = DrawBrowser.INSTANCE.getOpenDir();
	private ScrollablePanel picPanel =
		new ScrollablePanel(new GridBagLayout(), MAX_LENGTH, rows);

	private final JButton search = new JButton("Search");
	private final JButton browse = new JButton("Browse...");

	private String imageName = null;

	private final ICOHandler icoHandler = new ICOHandler();
	private final JDHandler jdHandler = new JDHandler();

	///////////////////////////////////////////////////// Constructors

	public SearchFrame() {
		super(MainFrame.INSTANCE, "Search Images...");
		setContentPane(main);
		setModal(true);
		createSettings();

		grid.insets = new Insets(INSET, INSET, INSET, INSET);
		grid.anchor = GridBagConstraints.NORTHWEST;

		main.add(settings, BorderLayout.NORTH);

		picPanel.setBackground(Color.white);
		pane = new JScrollPane(picPanel);
		pane.getViewport().setBackground(Color.white);
		main.add(pane, BorderLayout.CENTER);

		info = new JLabel(" ");
		info.setHorizontalAlignment(SwingConstants.LEFT);
		info.setBorder(new EmptyBorder(2, 10, 2, 10));
		getLeftButtonPanel().add(info);

		setDefaultBorder();
		setUndecorated(false);
		addButtonPanel();
		addRightButton(search);
		search.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		addRightButton(getCancelButton());
		getCancelButton().setText("Close");
		getRootPane().setDefaultButton(search);

		extBox.setEditable(false);

		pane.setPreferredSize(
			new Dimension(columns * MAX_LENGTH, rows * MAX_LENGTH));
	}

	///////////////////////////////////////////////////// Class Methods

	///////////////////////////////////////////////////// Instance methods

	private final void createSettings() {

		GridBagConstraints g = new GridBagConstraints();

		settings.setBorder(
			new CompoundBorder(
				new EtchedBorder(),
				new EmptyBorder(10, 10, 10, 10)));
		JLabel dirLabel = new JLabel("Directory: ");
		dirField = new JTextField(directory, 25);

		JLabel extLabel = new JLabel("Filter: ");
		browse.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				browse(dirField);
			}
		});

		g.gridx = 0;
		g.gridy = 0;
		g.anchor = GridBagConstraints.WEST;
		g.fill = GridBagConstraints.HORIZONTAL;

		settings.add(dirLabel, g);
		g.gridx++;
		settings.add(dirField, g);
		g.gridx++;
		settings.add(Box.createHorizontalStrut(4), g);
		g.gridx++;
		settings.add(browse, g);
		g.gridx++;
		settings.add(Box.createHorizontalStrut(8), g);
		g.gridx++;
		settings.add(extLabel, g);
		g.gridx++;
		settings.add(extBox, g);
		g.gridx++;
	}

	private void info(String text) {
		if (text.length() == 0)
			text = " ";

		final String infoText = text;
		Runnable runner = new Runnable() {
			public void run() {
				info.setText(infoText);
			}
		};
		SwingUtilities.invokeLater(runner);
	}

	public String getImageName() {
		return imageName;
	}

	private ImageIcon getIcon(String fileName) {
		ImageIcon icon = null;
		try {
			if (icoHandler.canHandleImage(fileName)) {
				icon = icoHandler.createIconLabel(fileName);
			}
			else if (jdHandler.canHandleImage(fileName)) {
				icon = jdHandler.createIconLabel(fileName);
			}
			else {
				icon = new ImageIcon(fileName);
			}
		}
		catch (Exception e) {
			Log.exception(e);

		}
		return icon;
	}

	public void addImage(String fileName) {
		ImageIcon icon = getIcon(fileName);
		if (icon == null) {
			return;
		}
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();
		if ((w > MAX_LENGTH - (2 * INSET)) || (h > MAX_LENGTH - (2 * INSET))) {
			Image i = icon.getImage();
			if (w > h)
				i =
					i.getScaledInstance(
						MAX_LENGTH - (2 * INSET),
						-1,
						Image.SCALE_SMOOTH);
			else
				i =
					i.getScaledInstance(
						-1,
						MAX_LENGTH - (2 * INSET),
						Image.SCALE_SMOOTH);
			icon = new ImageIcon(i);
		}
		JLabel iconLabel = new JLabel(icon);
		String name = fileName.substring(dirField.getText().trim().length());
		iconLabel.setToolTipText(name + " (" + w + "x" + h + ")");
		iconLabel.addMouseListener(listener);

		final JLabel pic = iconLabel;

		Runnable runner = new Runnable() {

			public void run() {
				Dimension dim = pane.getSize();
				colCount++;
				if (!(colCount * MAX_LENGTH <= dim.width - 20)) {
					grid.gridx = 0;
					grid.gridy = grid.gridy + 1;
					colCount = 1;
				}
				picPanel.add(pic, grid);
				grid.gridx++;
			}
		};

		SwingUtilities.invokeLater(runner);
	}

	private void browse(JTextField field) {
		String dir;
		dir = directory;
		JFileChooser chooser = new JFileChooser(dir);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {

			dirField.setText(chooser.getSelectedFile().toString());
			search();
		}
	}

	private void open(String fileName) {
		imageName = fileName;
		approve();
	}

	private void search() {

		search.setEnabled(false);
		browse.setEnabled(false);

		String dir = dirField.getText().trim();
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
			dirField.setText(dir);
		}
		picPanel.setVisible(false);
		picPanel.removeAll();
		picPanel.setVisible(true);
		colCount = 1;
		grid.gridx = 0;
		grid.gridy = 0;

		Thread searchThread = new Thread() {

			//private ArrayList list;

			public void run() {
				//list = ((BrowserFilter) extBox.getSelectedItem()).getExtensions();

				try {
					File dir1 = new File(dirField.getText().trim());
					open(dir1);
					directory = dirField.getText().trim();
					info("Search finished.");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					Runnable runner = new Runnable() {

						public void run() {
							browse.setEnabled(true);
							search.setEnabled(true);
						}
					};
					SwingUtilities.invokeLater(runner);
				}
			}

			private void open(File dir1) throws Exception {

				info("Searching in " + dir1.toString() + "...");
				sleep(100);
				File[] files = dir1.listFiles();
				BrowserFilter filter = (BrowserFilter) extBox.getSelectedItem();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						open(files[i]);
					}
					else {
						if (filter.accept(files[i])) {
							addImage(files[i].toString());
						}
					}
				}
			}

		};
		searchThread.start();
	}

	///////////////////////////////////////////////////// Inner classes

	//--------------------------------------------------  Inner class 1

	private final class IconListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
				JLabel label = (JLabel) e.getSource();
				String fileName = label.getToolTipText();
				fileName = fileName.substring(0, fileName.indexOf('(') - 1);
				String dir = dirField.getText().trim();
				if (!dir.endsWith(File.separator))
					dir = dir + File.separator;
				fileName = dir + fileName;

				final StandardDialog frame =
					new StandardDialog(
						SearchFrame.this,
						label.getToolTipText(),
						false);
				frame.setModal(true);
				JPanel panel = frame.main;				
				ImageIcon image = getIcon(fileName);
				JLabel l = new JLabel(image);
				panel.add(l, BorderLayout.CENTER);
				final JLabel fLabel = new JLabel(fileName);
				fLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
				fLabel.setFont(PLAIN);
				panel.add(fLabel, BorderLayout.NORTH);
				panel.setBorder(new EmptyBorder(10, 10, 10, 10));
				frame.setContentPane(panel);

				JButton open = new JButton("Open");
				open.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e1) {
						frame.setVisible(false);
						frame.dispose();
						open(fLabel.getText());
					}
				});
				frame.addRightButton(open);
				frame.getCancelButton().setText("Close");
				frame.addRightButton(frame.getCancelButton());
				open.setFocusPainted(false);
				frame.setDefaultBorder();
				frame.addButtonPanel();
				
				frame.setUndecorated(false);
				frame.getRootPane().setDefaultButton(open);				
				frame.open();				
			}
		}
	}
}