package jdomain.jdraw.gui;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.ImageIcon;

import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gio.GIFWriter;
import jdomain.util.Log;
import jdomain.util.Util;
import jdomain.util.gui.ImageHandler;

/*
 * ICOHandler.java - created on 09.12.2003 by J-Domain
 * 
 * @author Michaela Behling
 */

public class JDHandler implements ImageHandler {

	public JDHandler() {
	}

	public final boolean canHandleImage(String fileName) {
		return Util.getFileExtension(fileName).equalsIgnoreCase(".jd");
	}

	public ImageIcon createIconLabel(String fileName) {
		ObjectInputStream in = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			in = new ObjectInputStream(new FileInputStream(fileName));
			Picture picture = (Picture)in.readObject();
			if (picture != null) {				
				GIFWriter.writeGIF(picture, stream);
				return new ImageIcon(stream.toByteArray());						
			}
		}
		catch (Exception e) {
			Log.exception(e);
		}
		finally {
			Util.close(stream);
			Util.close(in);
		}
		return null;
	}

}
