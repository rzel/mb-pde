package jdomain.jdraw.action;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import jdomain.jdraw.gui.DrawWindow;
import jdomain.jdraw.gui.MainFrame;
import jdomain.util.Log;
import jdomain.util.ResourceLoader;
import jdomain.util.Util;

/*
 * AnimTutorialAction.java - created on 26.12.2005
 * 
 * @author Michaela Behling
 */

public final class AnimTutorialAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private DrawWindow dialog;

   protected AnimTutorialAction() {
      super( "Animation Tutorial...           " );
      setToolTipText( "Shows how to create animated GIFs" );
      // setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new
      // Character( 'A' ), KeyEvent.CTRL_MASK ) } );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      if ( dialog == null ) {
         try {
            URL url = ResourceLoader.getResourceURL( "jdomain/jdraw/help/anim.html" );
            dialog = new DrawWindow( "Animation Tutorial", false );
            JEditorPane editor = new JEditorPane( url ) {

               /** */
               private static final long serialVersionUID = 1L;

               public void paint( Graphics gr ) {
                  Graphics2D g2 = (Graphics2D)gr;
                  Object oldTextAntialias = g2.getRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING );

                  g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

                  super.paint( g2 );

                  g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, oldTextAntialias );
               }
            };
            editor.addHyperlinkListener( new Hyperactive() );
            editor.setEditable( false );
            JScrollPane pane = new JScrollPane( editor );
            Dimension dim = MainFrame.INSTANCE.getSize();
            dim.width = dim.width - (dim.width / 3);
            dim.height = dim.height - (dim.height / 3);
            pane.setPreferredSize( dim );

            dialog.setDefaultBorder();
            dialog.setUndecorated( false );
            dialog.setResizable( true );

            dialog.main.add( pane, BorderLayout.CENTER );

            dialog.addRightButton( dialog.getApproveButton() );
            dialog.getApproveButton().setText( "Close" );
            dialog.getApproveButton().setMnemonic( 'c' );
            dialog.addButtonPanel();
         }
         catch ( IOException io ) {
            Log.exception( io );
         }
      }
      else { // help window != null
         int state = dialog.getExtendedState();
         if ( (state & DrawWindow.ICONIFIED) != 0 ) {
            state = state - DrawWindow.ICONIFIED;
            dialog.setExtendedState( state );
         }
      }
      if ( dialog.isVisible() ) {
         return;
      }
      dialog.open();
   }


   private class Hyperactive implements HyperlinkListener {

      public void hyperlinkUpdate( HyperlinkEvent e ) {
         if ( e.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {
            if ( e.getURL().toString().endsWith( "opensheep" ) ) {
               InputStream in = null;
               FileOutputStream out = null;
               final String fileName = System.getProperty( "user.home" ) + File.separator + "sheep.gif";
               try { // copy sheep.gif
                  in = ResourceLoader.getResourceStream( "jdomain/jdraw/help/images/sheep.gif" );
                  out = new FileOutputStream( fileName );
                  final int BUF = 2048;
                  byte[] buf = new byte[BUF];
                  int result = 0;
                  do {
                     result = in.read( buf );
                     if ( result > 0 ) {
                        out.write( buf, 0, result );
                     }
                  }
                  while ( result > 0 );
                  Util.close( in );
                  Util.close( out );
                  SwingUtilities.invokeLater( new Runnable() {
                     public void run() {
                        ReopenAction a = new ReopenAction( new File( fileName ) );
                        a.actionPerformed( null );
                     }
                  } );
               }
               catch ( Exception ex ) {
                  // ignore
               }
               finally {
                  Util.close( in );
                  Util.close( out );
               }

            }
         }
      }
   }

}
