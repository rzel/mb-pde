package jdomain.util.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * Eine Border, die einzelne oder mehrere Linien eines Rechtecks zeichnet.
 * 
 * @author J-Domain [Michaela Behling]
 */
public class LinesBorder implements Border {

   public static final Color DEFAULT_COLOUR = UIManager.getColor( "controlDkShadow" );

   /** Konstante für "Linie ein" */
   public static final boolean SHOW_LINE = true;
   /** Konstante für "keine Linie" */
   public static final boolean NO_LINE = false;

   private final boolean oben;
   private final boolean links;
   private final boolean unten;
   private final boolean rechts;
   private final int[] insets = new int[4];

   private Color lineColor;

   /**
    * Konstruktur.
    * 
    * @param lineColour Farbe der Linie
    * @param oben Flag, ob oben eine Linie gezeichnet werden soll
    * @param links Flag, ob links eine Linie gezeichnet werden soll
    * @param unten Flag, ob unten eine Linie gezeichnet werden soll
    * @param rechts Flag, ob rechts eine Linie gezeichnet werden soll
    */
   public LinesBorder( Color lineColour, boolean oben, boolean links, boolean unten, boolean rechts ) {
      this.oben = oben;
      this.links = links;
      this.unten = unten;
      this.rechts = rechts;
      this.lineColor = lineColour;

      if ( oben ) {
         insets[0] = 1;
      }
      if ( links ) {
         insets[1] = 1;
      }
      if ( unten ) {
         insets[2] = 1;
      }
      if ( rechts ) {
         insets[3] = 1;
      }
   }

   /**
    * Konstruktur.
    * 
    * @param oben Flag, ob oben eine Linie gezeichnet werden soll
    * @param links Flag, ob links eine Linie gezeichnet werden soll
    * @param unten Flag, ob unten eine Linie gezeichnet werden soll
    * @param rechts Flag, ob rechts eine Linie gezeichnet werden soll
    */
   public LinesBorder( boolean oben, boolean links, boolean unten, boolean rechts ) {
      this( DEFAULT_COLOUR, oben, links, unten, rechts );
   }

   /**
    * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
    */
   public Insets getBorderInsets( Component c ) {
      return new Insets( insets[0], insets[1], insets[2], insets[3] );
   }

   /**
    * @see javax.swing.border.Border#isBorderOpaque()
    */
   public boolean isBorderOpaque() {
      return false;
   }

   /**
    * @see javax.swing.border.Border#paintBorder(java.awt.Component,
    *      java.awt.Graphics, int, int, int, int)
    */
   public void paintBorder( Component c, Graphics g, int x, int y, int width, int height ) {
      g.translate( x, y );
      g.setColor( lineColor );
      final int maxX = width - 1;
      final int maxY = height - 1;
      if ( oben ) {
         g.drawLine( 0, 0, maxX, 0 );
      }
      if ( links ) {
         g.drawLine( 0, 0, 0, maxY );
      }
      if ( unten ) {
         g.drawLine( 0, maxY, maxX, maxY );
      }
      if ( rechts ) {
         g.drawLine( maxX, 0, maxX, maxY );
      }
   }
}
