package jdomain.util.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/*
 * UpperCaseField.java - created on 20.11.2003 by J-Domain
 * 
 * @author Michaela Behling
 */

public class UpperCaseField extends JTextField {

   private static final long serialVersionUID = 0L;

   public UpperCaseField() {
      super();
   }

   public UpperCaseField( String text ) {
      super( text );
   }

   public UpperCaseField( String text, int columns ) {
      super( text, columns );
   }

   public UpperCaseField( Document doc, String text, int columns ) {
      super( doc, text, columns );
   }

   public UpperCaseField( int cols ) {
      super( cols );
   }

   protected Document createDefaultModel() {
      return new UpperCaseDocument();
   }


   private class UpperCaseDocument extends PlainDocument {
      /** */
      private static final long serialVersionUID = 1L;

      public void insertString( int offs, String str, AttributeSet a ) throws BadLocationException {

         if ( str == null ) {
            return;
         }
         char[] upper = str.toCharArray();
         for ( int i = 0; i < upper.length; i++ ) {
            upper[i] = Character.toUpperCase( upper[i] );
         }
         super.insertString( offs, new String( upper ), a );
      }
   }
}