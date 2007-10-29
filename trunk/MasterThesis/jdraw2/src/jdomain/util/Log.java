/*******************************************************************************
 * 
 * $Author: Michaela $ $Date: 2004/08/14 13:46:29 $ $Revision: 1.4 $
 *  
 ******************************************************************************/

package jdomain.util;

import java.util.ArrayList;

public final class Log {
   //////////////////////////////////////////// Members

   private static ArrayList listeners = new ArrayList();

   private static boolean on = true;

   public static final boolean DEBUG = System.getProperty( "log.debug" ) != null;
   private static String debugPrefix = ":: ";
   private static boolean exitOnException = false;

   //////////////////////////////////////////// Constructors

   private Log() {
   }

   //////////////////////////////////////////// Static Methods

   public static void turnOff() {
      on = false;
   }

   public static void turnOn() {
      on = true;
   }

   public static void addLogListener( LogListener aListener ) {
      listeners.add( aListener );
   }

   public static void removeListener( LogListener aListener ) {
      listeners.remove( aListener );
   }

   private static int getListenerCount() {
      if (!on) {
         return 0;
      }
      return listeners.size();
   }

   public static void close() {
      final int max = getListenerCount();
      LogListener l;
      for ( int i = 0; i < max; i++ ) {
         l = (LogListener) listeners.get( i );
         l.close();
      }
   }

   // Log messages

   public static void debug( String message ) {
      if (!DEBUG)
         return;

      final int max = getListenerCount();
      LogListener l;
      for ( int i = 0; i < max; i++ ) {
         l = (LogListener) listeners.get( i );
         l.debug( debugPrefix + message );
      }
   }

   public static void info( String message ) {
      final int max = getListenerCount();
      LogListener l;
      for ( int i = 0; i < max; i++ ) {
         l = (LogListener) listeners.get( i );
         l.info( message );
      }
   }

   public static void warning( String message ) {
      final int max = getListenerCount();
      LogListener l;
      for ( int i = 0; i < max; i++ ) {
         l = (LogListener) listeners.get( i );
         l.warning( message );
      }
   }

   public static void warning( String message, Object o ) {
      final int max = getListenerCount();
      LogListener l;
      for ( int i = 0; i < max; i++ ) {
         l = (LogListener) listeners.get( i );
         l.warning( message, o );
      }
   }

   public static void error( String message ) {
      final int max = getListenerCount();
      LogListener l;
      for ( int i = 0; i < max; i++ ) {
         l = (LogListener) listeners.get( i );
         l.error( message );
      }
   }

   public static void exception( Throwable e ) {
      final int max = getListenerCount();
      LogListener l;
      for ( int i = 0; i < max; i++ ) {
         l = (LogListener) listeners.get( i );
         l.exception( e );
      }
      if (exitOnException) {
         System.exit( 1 );
      }
      else if (on) {
         e.printStackTrace( System.err );
      }
   }

   public static void setDebugPrefix( String debugPrefix ) {
      Log.debugPrefix = debugPrefix;
   }

   public static void setExitOnException( boolean exitOnException ) {
      Log.exitOnException = exitOnException;
   }

   //////////////////////////////////////////// Instance Methods

   //////////////////////////////////////////// Inner classes

}