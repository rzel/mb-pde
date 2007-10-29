package jdomain.jdraw.gui;

import jdomain.util.gui.StandardWindow;

/*
 * DrawDialog.java - created on 30.10.2003
 * 
 * @author Michaela Behling
 */

public class DrawWindow extends StandardWindow {

	/** */
   private static final long serialVersionUID = 1L;

   public DrawWindow(String title, boolean ignoreESCKey) {
		super(MainFrame.INSTANCE, title, ignoreESCKey );
	}

}