package jdomain.util;

import java.awt.Dimension;
import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

import jdomain.util.gui.GUIUtil;

/*
 * ResourceLoader.java - created on 27.10.2003
 * 
 * @author Michaela Behling
 */

public final class ResourceLoader {

   public static int scalingHint = Image.SCALE_FAST;

   private static final HashMap RESOURCE_MAP = new HashMap( 200 );

   private ResourceLoader() {
   }

   public static ImageIcon getImage( String name ) {
      return getImage( name, true );
   }

   public static ImageIcon getImage( String name, boolean doStore ) {
      ImageIcon icon = (ImageIcon)RESOURCE_MAP.get( name );
      if ( icon == null ) {
         URL url = ResourceLoader.class.getClassLoader().getResource( name );
         if ( (url == null) && Log.DEBUG ) {
            Log.debug( "Image '" + name + "' not found." );
         }
         try {
            icon = new ImageIcon( url );
            if ( doStore ) {
               RESOURCE_MAP.put( name, icon );
            }
            GUIUtil.waitForImage( icon );
         }
         catch ( Exception e ) {
            if ( Log.DEBUG )
               Log.debug( "Couldn't read " + name );
         }
      }
      return icon;
   }

   public static void store( String key, ImageIcon icon ) {
      RESOURCE_MAP.put( key, icon );
   }

   public static URL getResourceURL( String name ) {
      URL url = ResourceLoader.class.getClassLoader().getResource( name );
      if ( (url == null) && Log.DEBUG ) {
         Log.debug( "Resource '" + name + "' not found." );
      }
      return url;
   }

   public static InputStream getResourceStream( String name ) {
      InputStream in = ResourceLoader.class.getClassLoader().getResourceAsStream( name );
      if ( (in == null) && Log.DEBUG ) {
         Log.debug( "Resource '" + name + "' not found." );
      }
      return in;
   }

   public static ImageIcon getImage( String name, int size ) {
      return getImage( name, size, true );
   }

   public static ImageIcon getImage( String name, int size, boolean doStore ) {
      ImageIcon icon = getImage( name, doStore );

      if ( icon.getIconWidth() > icon.getIconHeight() ) {
         return scaleImage( icon, size, -1 );
      }
      return scaleImage( icon, -1, size );
   }

   public static ImageIcon scaleImage( ImageIcon icon, Dimension dim ) {
      return scaleImage( icon, dim.width, dim.height );
   }

   public static ImageIcon scaleImage( ImageIcon icon, Dimension dim, int hint ) {
      return scaleImage( icon, dim.width, dim.height, hint );
   }

   public static ImageIcon scaleImage( ImageIcon icon, int width, int height, int hint ) {
      if ( (icon.getIconWidth() == -1) || (icon.getIconHeight() == -1) ) {
         return icon;
      }
      if ( (icon.getIconWidth() == width) && (icon.getIconHeight() == height) ) {
         return icon;
      }
      ImageIcon i = new ImageIcon( icon.getImage().getScaledInstance( width, height, hint ) );
      GUIUtil.waitForImage( i );
      return i;
   }

   public static ImageIcon scaleImage( ImageIcon icon, int width, int height ) {
      return scaleImage( icon, width, height, scalingHint );
   }

   public static ImageIcon scaleImage( ImageIcon icon, int size ) {
      if ( icon.getIconWidth() > icon.getIconHeight() ) {
         return scaleImage( icon, size, -1 );
      }
      return scaleImage( icon, -1, size );
   }
}