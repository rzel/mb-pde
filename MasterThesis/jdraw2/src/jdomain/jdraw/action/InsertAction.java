package jdomain.jdraw.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import jdomain.jdraw.data.Frame;
import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gio.GIFReader;
import jdomain.jdraw.gio.ImageReader;
import jdomain.jdraw.gui.DrawBrowser;
import jdomain.jdraw.gui.MainFrame;
import jdomain.jdraw.gui.PixelTool;
import jdomain.jdraw.gui.ToolPanel;
import jdomain.jdraw.gui.undo.UndoManager;
import jdomain.util.Log;
import jdomain.util.Util;

/*
 * InsertAction.java - created on 29.11.2003
 * 
 * @author Michaela Behling
 */

public final class InsertAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private String fileName;
   private Picture picture;

   protected InsertAction() {
      super( "Insert Image..." );
      setToolTipText( "Inserts an image into the current frame" );
   }

   public static boolean isJDraw( String f ) {
      int index = f.lastIndexOf( '.' );
      if ( index == -1 ) {
         return false;
      }
      String extension = f.substring( index );
      return extension.equalsIgnoreCase( ".jd" );
   }

   public static boolean isGIF( String f ) {
      int index = f.lastIndexOf( '.' );
      if ( index == -1 ) {
         return false;
      }
      String extension = f.substring( index );
      return extension.equalsIgnoreCase( ".gif" );
   }

   public static boolean isPNG( String f ) {
      int index = f.lastIndexOf( '.' );
      if ( index == -1 ) {
         return false;
      }
      String extension = f.substring( index );
      return extension.equalsIgnoreCase( ".png" );
   }

   public boolean prepareAction() {
      picture = null;
      File file = DrawBrowser.INSTANCE.openImage();
      if ( file != null ) {
         fileName = file.getAbsolutePath();
         return true;
      }
      return false;
   }

   private Picture readJDraw( String aFileName ) {
      ObjectInputStream in = null;
      try {
         in = new ObjectInputStream( new FileInputStream( aFileName ) );
         return (Picture)in.readObject();
      }
      catch ( Exception e ) {
         Log.exception( e );
         return null;
      }
      finally {
         Util.close( in );
      }
   }

   public void startAction() {
      if ( isGIF( fileName ) ) {
         picture = GIFReader.readGIF( fileName );
      }
      else if ( isJDraw( fileName ) ) {
         picture = readJDraw( fileName );
      }
      else {
         picture = ImageReader.readImage( fileName );
      }
   }

   public void finishAction() {
      if ( picture != null ) {
         int w = picture.getWidth();
         int h = picture.getHeight();
         Picture pic = MainFrame.INSTANCE.getPicture();
         w = Math.max( pic.getWidth(), w );
         h = Math.max( pic.getHeight(), h );
         if ( (w != pic.getWidth()) || (h != pic.getHeight()) ) {
            pic.setSize( w, h );
         }
         Palette pal = pic.getCurrentPalette();
         Frame frame = picture.getFrame( 0 );
         frame.addIndex( pal.size() );
         Palette palette = frame.getPalette();
         final int size = palette.size();
         for ( int i = 0; i < size; i++ ) {
            pal.addColour( palette.getColour( i ).getColour() );
         }
         pic.getCurrentFrame().pasteClip( frame, 0, 0 );
         UndoManager.INSTANCE.reset();
         ToolPanel.INSTANCE.setCurrentTool( PixelTool.INSTANCE, DrawAction
               .getAction( SetPixelToolAction.class ) );
         Log.info( "Inserted." );
      }
   }
}
