package jdomain.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author J-Domain [Michaela Behling]
 */
public final class PropertyHelper {

   private static final String MISSING_VALUE = "???";

   private final ResourceBundle bundle;
   private final MessageFormat format;

   public PropertyHelper( ResourceBundle bundle ) {
      this.bundle = bundle;
      format = new MessageFormat( "" );
      format.setLocale( bundle.getLocale() );
   }

   public Object getObject( String key ) {
      try {
         return bundle.getObject( key );
      }
      catch ( MissingResourceException e ) {
         Log.warning( "Property " + key + " not found." );
         return MISSING_VALUE;
      }
   }

   public String getString( String key ) {
      try {
         return bundle.getString( key );
      }
      catch ( MissingResourceException e ) {
         Log.warning( "Property " + key + " not found." );
         return MISSING_VALUE;
      }
   }

   public String getString( String key, Object[] args ) {
      try {
         format.applyPattern( getString( key ) );
         return format.format( args );
      }
      catch ( Exception e ) {
         Log.exception( e );
         return MISSING_VALUE;
      }
   }

}