package jdomain.jdraw.action;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.FrameSettingsDialog;

/*
 * EditFrameSettingsAction - created on 03.11.2003
 * 
 * @author Michaela Behling
 */

public final class EditFrameSettingsAction extends BlockingDrawAction {

	/** */
   private static final long serialVersionUID = 1L;

   public EditFrameSettingsAction() {
		super("Frame Settings...", "frame_settings.png");
		setToolTipText("Edits the current frame settings");
		setAccelerators(
			new KeyStroke[] {
				 KeyStroke.getKeyStroke(
					new Character('E'),
					KeyEvent.CTRL_MASK)});
	}

	public boolean prepareAction() {
		FrameSettingsDialog dialog = new FrameSettingsDialog();
		dialog.open();
		if ( dialog.getResult() == FrameSettingsDialog.APPROVE_ACTION ) {
			return true;
		}
		return false;
	}

	public void startAction() {
		
	}

	public void finishAction() {
	}

}
