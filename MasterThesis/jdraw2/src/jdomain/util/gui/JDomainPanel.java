package jdomain.util.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jdomain.util.ResourceLoader;

/*
 * JDomainPanel.java - created on 02.01.2004 by J-Domain
 * 
 * @author Michaela Behling
 */

public class JDomainPanel extends JPanel {

   private static final long serialVersionUID = 0L;

   public static final Color DARK_ORANGE = new Color( 0xfe, 0x80, 0x11 );
   public static final Color ORANGE = new Color( 0xff, 0xa1, 0x55 );
   public static final Color JD_BLUE = new Color( 0x31, 0x53, 0xb7 );
   public static final Color LIGHT_ORANGE = new Color( 0xff, 0xe1, 0xa5 );

   public JDomainPanel( String title ) {
      this( title, new Font( "Dialog", Font.BOLD, 22 ) );
   }

   public JDomainPanel( String title, Font font ) {
      super( new BorderLayout() );
      JLabel logo = new JLabel( ResourceLoader.getImage( GUIUtil.IMAGE_PATH + "logo.png" ) );
      add( logo, BorderLayout.EAST );
      JLabel l = new JLabel( title );
      l.setBorder( new EmptyBorder( 0, 4, 0, 0 ) );
      l.setFont( font );
      l.setForeground( Color.black );
      add( l, BorderLayout.WEST );
   }

   public void paint( Graphics g ) {
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

      Dimension size = getSize();
      g.setColor( LIGHT_ORANGE );
      g.fillRect( 0, 0, size.width, size.height );

      g.setColor( DARK_ORANGE );
      g.fillRect( 0, 29, size.width, 24 );
      paintChildren( g );
   }
}
