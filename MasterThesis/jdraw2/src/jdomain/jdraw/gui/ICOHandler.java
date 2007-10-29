package jdomain.jdraw.gui;

import java.io.ByteArrayOutputStream;

import javax.swing.ImageIcon;

import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gio.IconReader;
import jdomain.jdraw.gio.PNGWriter;
import jdomain.util.Log;
import jdomain.util.Util;
import jdomain.util.gui.ImageHandler;

/*
 * ICOHandler.java - created on 09.12.2003 by J-Domain
 * 
 * @author Michaela Behling
 */

public class ICOHandler implements ImageHandler {

	public ICOHandler() {
	}

	public final boolean canHandleImage(String fileName) {
		return Util.getFileExtension(fileName).equalsIgnoreCase(".ico");
	}

	public ImageIcon createIconLabel(String fileName) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			Picture picture = IconReader.readIcon(fileName);
			if (picture != null) {				
				PNGWriter.writePNG(picture, stream);				
				return new ImageIcon(stream.toByteArray());																
			}
		}
		catch (Exception e) {
			Log.exception(e);
		}
		finally {
			Util.close(stream);
		}
		return null;
	}

}
