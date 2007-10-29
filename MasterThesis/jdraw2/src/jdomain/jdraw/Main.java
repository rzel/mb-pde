package jdomain.jdraw;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import jdomain.jdraw.action.LoadAction;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gui.DrawBrowser;
import jdomain.jdraw.gui.DrawDialog;
import jdomain.jdraw.gui.DrawMenu;
import jdomain.jdraw.gui.MainFrame;
import jdomain.jdraw.gui.ToolPanel;
import jdomain.util.Log;
import jdomain.util.ResourceFinder;
import jdomain.util.ResourceLoader;
import jdomain.util.SimpleLogListener;
import jdomain.util.Util;
import jdomain.util.gui.FileBrowserUI;
import jdomain.util.gui.GUIUtil;

/*
 * Main.java - created on 26.10.2003
 * 
 * @author Michaela Behling
 */

public final class Main {

   private static final int SPLASH_DELAY = 1000;
   static { // statische initialisierungen
      SimpleLogListener listener = new SimpleLogListener( System.out );
      Log.addLogListener( listener );
      if ( System.getProperty( "log2file" ) != null ) {
         try {
            String lf = System.getProperty( "user.home" ) + File.separatorChar + "jdraw.log";
            Log.addLogListener( new SimpleLogListener( new PrintStream( new FileOutputStream( lf ) ) ) );
         }
         catch ( FileNotFoundException e ) {
            Log.exception( e );
         }
      }
   }

   private Main() {
   }

   public static final String APP_NAME = "JDraw";
   public static final String VERSION_NUMBER = "v1.1.5";
   public static final String VERSION = VERSION_NUMBER + " - December 2005";
   public static final String EMAIL = "<font color=blue>jdraw@j-domain.de</font>";
   public static final String WWW = "<font color=blue>www.j-domain.de</font>";
   public static final String WWW_JDRAW = WWW + "/de/software/jdraw";
   public static final String SF_WWW_JDRAW = "jdraw.sourceforge.net";

   private static void setupUI( String[] args ) {
      LookAndFeel lf = GUIUtil.findLF( args );
      try {
         UIManager.setLookAndFeel( lf );
         Object[] uiDefaults = { "FileChooserUI", FileBrowserUI.FILE_BROWSER_UI, "TitledBorder.font",
               new FontUIResource( MainFrame.DEFAULT_FONT ) };
         UIManager.getDefaults().putDefaults( uiDefaults );
      }
      catch ( Exception e ) {
         Log.exception( e );
      }
   }

   private static void preload() {
      String[] exts = new String[] { ".gif", ".png" };
      String prefix = "jdomain/jdraw/images";
      ResourceFinder finder = new ResourceFinder( prefix, exts );
      String[] files = finder.findResources();
      prefix = finder.getPath();
      final int len = files.length;
      String resource;
      for ( int i = 0; i < len; i++ ) {
         resource = files[i].substring( files[i].lastIndexOf( prefix ) );
         resource = resource.replace( '\\', '/' );
         ResourceLoader.getImage( resource );
      }
   }

   public static boolean isVisible( Picture pic ) {
      return pic == MainFrame.INSTANCE.getPicture();
   }

   public static final void setPicture( Picture pic ) {
      MainFrame.INSTANCE.setPicture( pic );
   }

   public static final void main( String[] args ) {
      ResourceLoader.scalingHint = Image.SCALE_SMOOTH;
      setupUI( args );

      DrawDialog splash = new DrawDialog( null, APP_NAME + " Splash", true );
      splash.setModal( false );
      ImageIcon icon = ResourceLoader.getImage( "jdomain/jdraw/images/logo.png" );
      splash.getContentPane().add( new JLabel( icon ) );
      splash.open();

      preload();
      Picture picture = null;
      final int len = args.length;
      if ( len > 0 ) {
         for ( int i = 0; i < len; i++ ) {
            if ( args[i] != null ) {
               picture = LoadAction.readImage( args[i] );
               break;
            }
         }
      }
      if ( picture == null ) {
         picture = Picture.createDefaultPicture();
      }
      setPicture( picture );
      ResourceLoader.getImage( "jdomain/jdraw/images/background.gif" );

      Util.delay( SPLASH_DELAY );
      splash.close();
      ((DrawMenu)MainFrame.INSTANCE.getJMenuBar()).buildLastFilesMenu();      
      DrawBrowser.INSTANCE.setOpenDir( Settings.INSTANCE.getOpenDir() );
      DrawBrowser.INSTANCE.setSaveDir( Settings.INSTANCE.getSaveDir() );
      ToolPanel.INSTANCE.getCurrentTool().activate();
      MainFrame.INSTANCE.setVisible( true );

      Log.info( "Happy drawing!" );
   }

}