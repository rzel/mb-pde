package jdomain.util.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import jdomain.util.BrowserLauncher;

/**
 * @author J-Domain [Michaela Behling]
 */
public final class LinkLabel extends JLabel {

   private static final long serialVersionUID = 0L;

   private static final ActionListener BROWSER_ACTION = new ActionListener() {
      public void actionPerformed( ActionEvent e ) {
         try {
            BrowserLauncher.openURL( ((LinkLabel)(e.getSource())).getURL() );
         }
         catch ( Exception ex ) {
            ex.printStackTrace( System.err );
         }
      }
   };

   private ActionListener actionListener = BROWSER_ACTION;
   private boolean underline;
   private String url;

   public LinkLabel( String text, Icon icon, int horizontalAlignment ) {
      super( text, icon, horizontalAlignment );
      setup();
   }

   public LinkLabel( String text, int horizontalAlignment ) {
      super( text, horizontalAlignment );
      setup();
   }

   public LinkLabel( String text ) {
      super( text );
      setup();
   }

   public LinkLabel( Icon image, int horizontalAlignment ) {
      super( image, horizontalAlignment );
      setup();
   }

   public LinkLabel( Icon image ) {
      super( image );
      setup();
   }

   public LinkLabel() {
      super();
      setup();
   }

   public void setActionListener( ActionListener l ) {
      actionListener = l;
   }

   public void setURL( String url ) {
      this.url = url;
   }

   public String getURL() {
      return url;
   }

   private void setup() {
      url = getText();
      setForeground( Color.BLUE );
      setHorizontalAlignment( SwingConstants.LEFT );
      addMouseListener( new MouseAdapter() {
         public void mouseEntered( MouseEvent e ) {
            underline = true;
            setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
            repaint();
         }

         public void mouseExited( MouseEvent e ) {
            underline = false;
            setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
            repaint();
         }

         public void mouseClicked( MouseEvent e ) {
            if ( (actionListener != null) && (e.getButton() == MouseEvent.BUTTON1) ) {
               actionListener.actionPerformed( new ActionEvent( LinkLabel.this, 0, "" ) );
            }
         }
      } );
   }

   public void paint( Graphics g ) {
      super.paint( g );
      if ( underline ) {
         FontMetrics fm = getFontMetrics( getFont() );
         Dimension dim = getSize();
         Insets insets = getInsets();
         dim.width -= insets.left + insets.right;
         dim.height -= insets.top + insets.bottom;
         Rectangle viewR = new Rectangle( dim );
         Rectangle iconR = new Rectangle();
         Rectangle textR = new Rectangle();
         SwingUtilities.layoutCompoundLabel( fm, getText(), getIcon(), getVerticalAlignment(),
               getHorizontalAlignment(), getVerticalTextPosition(), getHorizontalTextPosition(), viewR,
               iconR, textR, getIconTextGap() );
         g.setColor( getForeground() );
         final int x = textR.x;
         final int y = textR.y + textR.height - 1;
         final int x2 = x + textR.width - 1;
         g.drawLine( x, y, x2, y );
      }
   }
}