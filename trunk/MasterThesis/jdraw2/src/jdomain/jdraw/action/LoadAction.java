package jdomain.jdraw.action;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.KeyStroke;

import jdomain.jdraw.Main;
import jdomain.jdraw.Settings;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gio.GIFReader;
import jdomain.jdraw.gio.IconReader;
import jdomain.jdraw.gio.ImageReader;
import jdomain.jdraw.gui.DrawBrowser;
import jdomain.jdraw.gui.MainFrame;
import jdomain.jdraw.gui.PixelTool;
import jdomain.jdraw.gui.ToolPanel;
import jdomain.jdraw.gui.undo.UndoManager;
import jdomain.util.Log;
import jdomain.util.Util;
import jdomain.util.gui.GUIUtil;

/*
 * LoadAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class LoadAction extends BlockingDrawAction implements ContinuedAction {

   /** */
   private static final long serialVersionUID = 1L;
   private Picture picture;
   private String newFileName;

   protected LoadAction() {
      super( "Open...", "open.png" );
      setToolTipText( "Opens an image file" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'O' ), KeyEvent.CTRL_MASK ) } );
   }

   public static boolean isJDraw( String f ) {
      int index = f.lastIndexOf( '.' );
      if ( index == -1 ) {
         return false;
      }
      String extension = f.substring( index );
      return extension.equalsIgnoreCase( ".jd" );
   }

   public static boolean isICO( String f ) {
      int index = f.lastIndexOf( '.' );
      if ( index == -1 ) {
         return false;
      }
      String extension = f.substring( index );
      return extension.equalsIgnoreCase( ".ico" );
   }

   public static boolean isGIF( String f ) {
      int index = f.lastIndexOf( '.' );
      if ( index == -1 ) {
         return false;
      }
      String extension = f.substring( index );
      return extension.equalsIgnoreCase( ".gif" );
   }

   public static boolean isJPEG( String f ) {
      int index = f.lastIndexOf( '.' );
      if ( index == -1 ) {
         return false;
      }
      String extension = f.substring( index );
      return extension.equalsIgnoreCase( ".jpg" ) || extension.equalsIgnoreCase( ".jpeg" );
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
         newFileName = file.getAbsolutePath();
         return true;
      }
      return false;
   }

   private Picture readJDraw( String fileName ) {
      ObjectInputStream in = null;
      try {
         in = new ObjectInputStream( new FileInputStream( fileName ) );
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

   public static Picture readImage( String filename ) {
      LoadAction action = (LoadAction)DrawAction.getAction( LoadAction.class );
      action.newFileName = filename;
      action.startAction();
      return action.picture;
   }

   public static void openImage( String name ) {
      LoadAction action = (LoadAction)DrawAction.getAction( LoadAction.class );
      action.newFileName = name;
      action.startAction();
      action.finishAction();
   }

   public void startAction() {
      if ( isGIF( newFileName ) ) {
         picture = GIFReader.readGIF( newFileName );
         if ( picture == null ) {
            boolean result = GUIUtil.question( MainFrame.INSTANCE, "Retry?",
                  "JDraw couldn't read this GIF image. Do you want "
                        + "to retry using Java's graphics library?", "Retry", "Cancel" );
            if ( result ) {
               picture = ImageReader.readImage( newFileName );
            }
         }
      }
      else if ( isJDraw( newFileName ) ) {
         picture = readJDraw( newFileName );
      }
      else if ( isICO( newFileName ) ) {
         picture = IconReader.readIcon( newFileName );
      }
      else {
         picture = ImageReader.readImage( newFileName );
      }
   }

   public void finishAction() {
      if ( picture != null ) {
         Settings.INSTANCE.addLastFile( newFileName );
         Settings.INSTANCE.save();
         MainFrame.INSTANCE.setFileName( newFileName );
         Main.setPicture( picture );
         UndoManager.INSTANCE.reset();
         ToolPanel.INSTANCE.setCurrentTool( PixelTool.INSTANCE, DrawAction
               .getAction( SetPixelToolAction.class ) );
         Log.info( "Opened." );
      }
   }
}
