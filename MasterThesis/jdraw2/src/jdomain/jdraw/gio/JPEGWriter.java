package jdomain.jdraw.gio;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import jdomain.jdraw.Settings;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gui.FolderPanel;
import jdomain.util.Log;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public final class JPEGWriter {

   private JPEGWriter() {
      super();
   }

   public static boolean writeJPEG( Picture pic, String fileName ) {
      try {
         OutputStream o = new BufferedOutputStream( new FileOutputStream( fileName ) );
         JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder( o );
         BufferedImage img = (BufferedImage)FolderPanel.INSTANCE.createOffScreenImage();
         JPEGEncodeParam param = enc.getDefaultJPEGEncodeParam( img );
         param.setQuality( Settings.INSTANCE.getJPEGQuality(), true );

         enc.encode( img, param );
         o.close();
         return true;
      }
      catch ( Exception e ) {
         Log.exception( e );
         return false;
      }
   }
}
