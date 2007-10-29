/*******************************************************************
 *
 *  $Author: Michaela $
 *  $Date: 2005/12/14 12:55:41 $
 *  $Revision: 1.5 $
 *
 *******************************************************************/

package jdomain.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;

/*
 * JAR Syntax
 * jar:file:/home/michaela/Java/RPGCreator/RPGCreator.jar!/rpg/base/special
 * 
 * FILE Syntax file:/home/michaela/Java/classes/rpg/base/special
 */

public final class ResourceFinder {

   // /////////////////////////////////////////////////// Constants

   private static final String FILE = "file:";
   private static final String JAR = "jar:";

   // /////////////////////////////////////////////////// Fields

   private final String path;

   private final ArrayList files = new ArrayList();
   private final boolean recursive;
   private final FileFilter filter;
   private final String[] extensions;

   private String prefix;

   // /////////////////////////////////////////////////// Constructors

   public ResourceFinder( String aPath ) {
      this( aPath, null, true );
   }

   public ResourceFinder( String aPath, String[] exts ) {
      this( aPath, exts, true );
   }

   public ResourceFinder( String aPath, String[] exts, boolean searchRecursive ) {
      path = aPath;
      recursive = searchRecursive;
      extensions = exts;
      filter = new Filter();
   }

   // /////////////////////////////////////////////////// Class Methods

   // /////////////////////////////////////////////////// Instance methods

   public String[] findResources() {
      try {
         files.clear();
         prefix = new String( path );
         URL url = getClass().getClassLoader().getResource( path );
         if ( (url == null) && (File.separatorChar == '\\') ) {
            // zip & win separator?
            prefix = path.replace( File.separatorChar, '/' );
            url = getClass().getClassLoader().getResource( prefix );
         }
         findResources( url );

         final int foundFiles = files.size();
         if ( foundFiles == 0 )
            return null;
         String[] fileNames = new String[foundFiles];
         for ( int i = 0; i < foundFiles; i++ ) {
            fileNames[i] = files.get( i ).toString();
         }

         return fileNames;
      }
      catch ( Throwable e ) {
         Log.exception( e );
         return null;
      }
   }

   public String getPath() {
      return prefix;
   }

   private void findResources( URL pathUrl ) throws Exception {

      if ( pathUrl == null ) {
         Log.warning( "path '" + path + "' not found" );
         return;
      }

      String url = pathUrl.toString();

      if ( url.startsWith( JAR ) ) {
         if ( File.separatorChar == '\\' ) {
            prefix = path.replace( File.separatorChar, '/' );
         }
         findJarResources( pathUrl );
      }
      else if ( url.startsWith( FILE ) ) {
         if ( File.separatorChar == '\\' ) {
            prefix = path.replace( '/', '\\' );
         }
         findFileResources( pathUrl );
      }
      else {
         Log.warning( "cannot handle " + pathUrl );
      }
   }

   private String convert( String s ) {
      try {
         s = URLDecoder.decode( s, "utf-8" );
      }
      catch ( UnsupportedEncodingException e ) {         
         e.printStackTrace();
      }
      return s;
   }

   private void findJarResources( URL url ) throws IOException {
      String s = url.toString().substring( JAR.length() );
      s = s.substring( FILE.length() );
      int index = s.indexOf( '!' );
      Assert.isTrue( index != -1, "Invalid jar url " + url.toString() );
      String jarFile = s.substring( 0, index );
      if ( File.separatorChar == '\\' ) {
         jarFile = jarFile.substring( 1 );
      }
      JarFile jFile = null;
      try {
         char c = jarFile.charAt( 0 );
         while ( (!Character.isLetter( c )) && (c != '/') && (c != '\\') ) {
            jarFile = jarFile.substring( 1 );
            c = jarFile.charAt( 0 );
         }
         jarFile = convert( jarFile );
         Log.info( "opening jar file '" + jarFile + "'" );
         jFile = new JarFile( jarFile );
         String aPath = s.substring( index + 1 );
         if ( aPath.startsWith( "/" ) )
            aPath = aPath.substring( 1 );
         Enumeration enumeration = jFile.entries();
         String entry;
         while ( enumeration.hasMoreElements() ) {
            entry = enumeration.nextElement().toString();
            if ( !entry.endsWith( "/" ) ) { // directory entry
               if ( entry.startsWith( aPath ) ) {
                  files.add( entry );
               }
            }
         }
      }
      finally {
         Util.close( jFile );
      }
   }

   private void findFileResources( URL url ) {

      findFilesInDirectory( new File( url.toString().substring( FILE.length() ) ) );

   }

   private void findFilesInDirectory( File dir ) {

      File[] fileList = dir.listFiles( filter );

      File f;
      for ( int i = 0; i < fileList.length; i++ ) {
         f = fileList[i];
         if ( f.isDirectory() )
            findFilesInDirectory( f );
         else
            files.add( f.toString() );
      }

   }


   // /////////////////////////////////////////////////// Inner classes

   // -------------------------------------------------- Inner class 1

   private class Filter implements FileFilter {

      public final boolean accept( File f ) {

         if ( f.isDirectory() ) {
            return recursive;
         }
         if ( extensions == null )
            return true;

         String fileName = f.toString();
         for ( int i = 0; i < extensions.length; i++ ) {
            if ( fileName.endsWith( extensions[i] ) )
               return true;
         }

         return false;
      }

   }
}
