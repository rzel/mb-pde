package jdomain.jdraw.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import jdomain.jdraw.action.AddFrameAction;
import jdomain.jdraw.action.CropAction;
import jdomain.jdraw.action.DrawAction;
import jdomain.jdraw.action.LoadAction;
import jdomain.jdraw.action.RedoAction;
import jdomain.jdraw.action.RemoveFrameAction;
import jdomain.jdraw.action.SaveAction;
import jdomain.jdraw.action.SetClipToolAction;
import jdomain.jdraw.action.SetColourPickerToolAction;
import jdomain.jdraw.action.SetFillToolAction;
import jdomain.jdraw.action.SetFilledOvalToolAction;
import jdomain.jdraw.action.SetFilledRectangleToolAction;
import jdomain.jdraw.action.SetLineToolAction;
import jdomain.jdraw.action.SetOvalToolAction;
import jdomain.jdraw.action.SetPixelToolAction;
import jdomain.jdraw.action.SetRectangleToolAction;
import jdomain.jdraw.action.SetTextToolAction;
import jdomain.jdraw.action.ToggleAntialiasAction;
import jdomain.jdraw.action.ToggleGradientFillAction;
import jdomain.jdraw.action.UndoAction;
import jdomain.jdraw.action.ViewAnimationAction;
import jdomain.util.Assert;
import jdomain.util.ResourceLoader;
import jdomain.util.gui.AntialiasPanel;

/*
 * Created on 28-Oct-2003
 *
 * @author michaela
 */

public final class ToolPanel extends JPanel {

	/** */
   private static final long serialVersionUID = 1L;

   private static final Insets INSETS = new Insets(1, 1, 0, 0);

	public static final int ICON_SIZE = 28;
	public static final ToolPanel INSTANCE = new ToolPanel();
	private static final Dimension BUTTON_DIMENSION = new Dimension(38, 38);
	private static final int SKIP = 8;

	private final HashMap toolMap = new HashMap();

	private Tool currentTool;
	public final ButtonGroup toolGroup = new ButtonGroup();	
	private ToolButton antialiasButton;
	private ToolButton gradientFillButton;

	private ToolPanel() {
		super(new BorderLayout(6, 0));
		setBorder(new EmptyBorder(0, 0, 4, 0));
		currentTool = PixelTool.INSTANCE;
		createGui();
	}

	public Tool getCurrentTool() {
		return currentTool;
	}

	public void setCurrentTool(Tool aTool, DrawAction activator) {
		if (currentTool != aTool) {
			if (currentTool != null) {
				currentTool.deactivate();
			}			
			currentTool = aTool;			
			ToolButton button = (ToolButton) toolMap.get(activator);
			Assert.notNull(button, "gui: tool button not found!");
			button.setSelected(true);
			currentTool.activate();
			DrawAction.getAction(ToggleAntialiasAction.class).setEnabled(
				currentTool.supportsAntialias());
			DrawAction.getAction(ToggleGradientFillAction.class).setEnabled(
				currentTool.supportsGradientFill());

		}
	}

	private void createGui() {
		JPanel clickBar = createClickBar();
		add(clickBar, BorderLayout.WEST);

		JScrollPane pane = new JScrollPane(Tool.getPreview());
		Border border = pane.getBorder();
		pane.setBorder(new CompoundBorder(new TitledBorder("Preview"), border));
		pane.setPreferredSize(new Dimension(10, 10));
		add(pane, BorderLayout.CENTER);
	}

	private JPanel createClickBar() {
		JPanel panel = new AntialiasPanel(new GridBagLayout());
		JPanel tools = createTools();
		JPanel actions = createActions();

		// panel anordnen				
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 1;
		gc.gridheight = 1;

		panel.add(actions, gc);

		gc.gridx++;
		panel.add(tools, gc);

		return panel;
	}

	private JPanel createActions() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(new TitledBorder("Shortcuts"));

		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = INSETS;
		gc.gridx = 0;
		gc.gridy = 0;

		ActionButton button;
		button =
			new ActionButton(DrawAction.getAction(SaveAction.class), "save.png");
		p.add(button, gc);

		gc.gridx++;
		button =
			new ActionButton(DrawAction.getAction(LoadAction.class), "open.png");
		p.add(button, gc);

		gc.gridx++;
		button =
			new ActionButton(DrawAction.getAction(UndoAction.class), "undo.png");
		p.add(button, gc);

		gc.gridx++;
		button =
			new ActionButton(DrawAction.getAction(RedoAction.class), "redo.png");
		p.add(button, gc);

		gc.gridx = 0;
		gc.gridy++;
		button =
			new ActionButton(
				DrawAction.getAction(AddFrameAction.class),
				"frame_new.png");
		p.add(button, gc);

		gc.gridx++;
		button =
			new ActionButton(
				DrawAction.getAction(RemoveFrameAction.class),
				"delete_frame.png");
		p.add(button, gc);

		gc.gridx++;
		button =
			new ActionButton(
				DrawAction.getAction(ViewAnimationAction.class),
				"view_anim.png");
		p.add(button, gc);

		gc.gridx++;
		//		button =
		//			new ActionButton(DrawAction.getAction(HelpAction.class), "help.png");
		button =
			new ActionButton(DrawAction.getAction(CropAction.class), "crop.png");
		p.add(button, gc);

		return p;
	}

	public void selectAntialias(boolean flag) {
		antialiasButton.setSelected(flag);
	}

	public void selectGradientFill(boolean flag) {
		gradientFillButton.setSelected(flag);
	}

	private JPanel createTools() {

		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(new TitledBorder("Tools"));
		ToolButton button;
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = INSETS;
		gc.gridx = 0;
		gc.gridy = 0;

		button =
			new ToolButton(
				DrawAction.getAction(SetPixelToolAction.class),
				"pixel_tool.png");
		toolGroup.add(button);
		p.add(button, gc);
		toolGroup.setSelected(button.getModel(), true);
//		pixelButton = button;

		gc.gridx++;
		button =
			new ToolButton(
				DrawAction.getAction(SetFillToolAction.class),
				"fill_tool.png");
		toolGroup.add(button);
		p.add(button, gc);

		gc.gridx++;
		button =
			new ToolButton(
				DrawAction.getAction(SetColourPickerToolAction.class),
				"colorpicker.png");
		toolGroup.add(button);
		p.add(button, gc);

		gc.gridx++;
		button =
			new ToolButton(
				DrawAction.getAction(SetLineToolAction.class),
				"line_tool.png");
		toolGroup.add(button);
		p.add(button, gc);

		gc.gridx++;
		p.add(Box.createHorizontalStrut(SKIP), gc);

		gc.gridx++;
		button =
			new ToolButton(
				DrawAction.getAction(ToggleGradientFillAction.class),
				"gradient_fill.png");
		p.add(button, gc);
		gradientFillButton = button;

		gc.gridx++;
		button =
			new ToolButton(
				DrawAction.getAction(ToggleAntialiasAction.class),
				"antialias_on.png");
		p.add(button, gc);
		antialiasButton = button;
		button.setSelected(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JToggleButton button1 = (JToggleButton) e.getSource();
				if (button1.isSelected()) {
					button1.setIcon(
						ResourceLoader.getImage(
							"jdomain/jdraw/images/antialias_on.png",
							ICON_SIZE));
				}
				else {
					button1.setIcon(
						ResourceLoader.getImage(
							"jdomain/jdraw/images/antialias_off.png",
							ICON_SIZE));
				}
			}
		});

		gc.gridx = 0;
		gc.gridy++;

		button =
			new ToolButton(
				DrawAction.getAction(SetRectangleToolAction.class),
				"rectangle_tool.png");
		toolGroup.add(button);
		p.add(button, gc);

		gc.gridx++;
		button =
			new ToolButton(
				DrawAction.getAction(SetFilledRectangleToolAction.class),
				"filled_rectangle_tool.png");
		toolGroup.add(button);
		p.add(button, gc);

		gc.gridx++;
		button =
			new ToolButton(
				DrawAction.getAction(SetOvalToolAction.class),
				"oval_tool.png");
		toolGroup.add(button);
		p.add(button, gc);

		gc.gridx++;
		button =
			new ToolButton(
				DrawAction.getAction(SetFilledOvalToolAction.class),
				"filled_oval_tool.png");
		toolGroup.add(button);
		p.add(button, gc);

		gc.gridx++;
		p.add(Box.createHorizontalStrut(SKIP), gc);

		gc.gridx++;
		button =
			new ToolButton(
				DrawAction.getAction(SetClipToolAction.class),
				"clip_tool.png");
		toolGroup.add(button);
		p.add(button, gc);

		gc.gridx++;
		button =
			new ToolButton(
				DrawAction.getAction(SetTextToolAction.class),
				"text_tool.png");
		toolGroup.add(button);
		p.add(button, gc);

		return p;
	}

	// Tool Button

	public final class ToolButton extends JToggleButton {

		/** */
      private static final long serialVersionUID = 1L;
      public ToolButton(DrawAction action, String iconName) {
			super(action);
			toolMap.put(action, this);
			setFocusPainted(false);
			this.setToolTipText(action.getToolTipText());
			setText(null);
			setIcon(
				ResourceLoader.getImage("jdomain/jdraw/images/" + iconName, ICON_SIZE));
			this.setHorizontalAlignment(SwingConstants.CENTER);
			this.setVerticalAlignment(SwingConstants.CENTER);
		}

		//	warum ist das nötig? muss ein bug sein.
		protected void processComponentKeyEvent(KeyEvent e) {
			e.consume();
		}
		public Dimension getPreferredSize() {
			return BUTTON_DIMENSION;
		}
	}

	//	Action Button

	public final class ActionButton extends JButton {

		/** */
      private static final long serialVersionUID = 1L;

      public ActionButton(DrawAction action, String iconName) {
			super(action);
			setFocusPainted(false);
			this.setToolTipText(action.getToolTipText());
			setText(null);
			setIcon(
				ResourceLoader.getImage("jdomain/jdraw/images/" + iconName, ICON_SIZE));
		}

		// warum ist das nötig? muss ein bug sein.
		protected void processComponentKeyEvent(KeyEvent e) {
			e.consume();
		}

		public Dimension getPreferredSize() {
			return BUTTON_DIMENSION;
		}
	}

}
