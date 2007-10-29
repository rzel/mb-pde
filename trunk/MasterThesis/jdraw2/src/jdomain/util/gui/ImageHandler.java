package jdomain.util.gui;

import javax.swing.ImageIcon;

public interface ImageHandler {
	public ImageIcon createIconLabel(String fileName);
	public boolean canHandleImage(String fileName);
}
