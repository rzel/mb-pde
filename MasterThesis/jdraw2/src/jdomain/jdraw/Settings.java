package jdomain.jdraw;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import jdomain.jdraw.gui.DrawBrowser;
import jdomain.jdraw.gui.DrawMenu;
import jdomain.jdraw.gui.FillTool;
import jdomain.jdraw.gui.MainFrame;
import jdomain.jdraw.gui.FillTool.Tolerance;
import jdomain.util.Log;
import jdomain.util.Util;

/**
 * @author J-Domain [Michaela Behling]
 */
public final class Settings implements Serializable {

   private static final int MAX_LAST_FILES = 10;
   private static final long serialVersionUID = 1L;
   public static final Settings INSTANCE = createSettings();

   private float jpegQuality = 1.0f;
   private ArrayList fileList = new ArrayList();
   private String openDir = DrawBrowser.INSTANCE.getOpenDir();
   private String saveDir = openDir;

   private int redTolerance = 0;
   private int greenTolerance = 0;
   private int blueTolerance = 0;
   private int alphaTolerance = 0;

   private Dimension frameDim = new Dimension(800,720);
   private Point frameLoc = new Point( 0, 0 );

   private Settings() {
   }

   public Dimension getFrameDimension() {
      return frameDim;
   }

   public Point getFrameLocation() {
      return frameLoc;
   }

   public void saveWindowDimensions() {
      frameLoc = MainFrame.INSTANCE.getLocation();
      frameDim = MainFrame.INSTANCE.getSize();
   }

   public void setTolerance( Tolerance tolerance ) {
      redTolerance = tolerance.redDiff;
      greenTolerance = tolerance.greenDiff;
      blueTolerance = tolerance.blueDiff;
      alphaTolerance = tolerance.alphaDiff;
   }

   private static String getFilename() {
      StringBuffer buf = new StringBuffer();
      buf.append( System.getProperty( "user.home" ) );
      buf.append( File.separator ).append( "jdraw." );
      buf.append( Main.VERSION_NUMBER ).append( ".dat" );
      return buf.toString();
   }

   public ArrayList getFileList() {
      return fileList;
   }

   public float getJPEGQuality() {
      return jpegQuality;
   }

   public String getOpenDir() {
      return openDir;
   }

   public String getSaveDir() {
      return saveDir;
   }

   // opened == true file was loaded, else: file was saved
   public void addLastFile( String fileName ) {
      openDir = DrawBrowser.INSTANCE.getOpenDir();
      saveDir = DrawBrowser.INSTANCE.getSaveDir();
      final int index = fileList.indexOf( fileName );
      if ( index == 0 ) { // file already first in list
         return;
      }
      if ( index == -1 ) { // new file to add
         fileList.add( 0, fileName );
         if ( fileList.size() > MAX_LAST_FILES ) {
            fileList.remove( MAX_LAST_FILES - 1 );
         }
      }
      else { // file reopened. move to top of list
         fileList.remove( fileName );
         fileList.add( 0, fileName );
      }
      ((DrawMenu)MainFrame.INSTANCE.getJMenuBar()).buildLastFilesMenu();
   }

   public void setJPEGQuality( float quality ) {
      jpegQuality = quality;
   }

   private static Settings createSettings() {
      Log.debug( "loading settings..." );
      File file = new File( getFilename() );
      if ( file.exists() ) {
         ObjectInputStream in = null;
         try {
            in = new ObjectInputStream( new FileInputStream( file ) );
            Settings settings = (Settings)in.readObject();
            settings.check();
            return settings;
         }
         catch ( Exception e ) { // ignore
            Log.debug( "error loading setting. creating new instance..." );
            // Log.exception(e);
         }
         finally {
            Util.close( in );
         }
      }
      Settings settings = new Settings();
      settings.check();
      return settings;
   }

   private void check() {
      if ( openDir == null ) {
         openDir = System.getProperty( "user.dir" );
      }
      if ( saveDir == null ) {
         saveDir = System.getProperty( "user.dir" );
      }
      FillTool.INSTANCE.setTolerance( new Tolerance( redTolerance, greenTolerance, blueTolerance,
            alphaTolerance ) );

      if ( frameDim != null ) {
         final int w = frameDim.width;
         final int h = frameDim.height;

         if ( (w == 0) || (h == 0) ) {
            frameDim = null;
         }
      }
   }

   public void save() {
      Log.debug( "saving settings..." );
      ObjectOutputStream out = null;
      try {
         out = new ObjectOutputStream( new FileOutputStream( getFilename() ) );
         out.writeObject( this );
      }
      catch ( Exception e ) {
         Log.error( "saving settings failed" );
      }
      finally {
         Util.close( out );
      }
   }
}