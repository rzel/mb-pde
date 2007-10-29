package jdomain.jdraw.action;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gio.GIFWriter;
import jdomain.jdraw.gui.DrawDialog;
import jdomain.jdraw.gui.MainFrame;
import jdomain.jdraw.gui.Tool;
import jdomain.util.Log;
import jdomain.util.gui.GUIUtil;

/*
 * ViewAnimationAction.java - created on 30.10.2003
 * 
 * @author Michaela Behling
 */

public final class ViewAnimationAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   private boolean success;

   private ByteArrayOutputStream out;

   public ViewAnimationAction() {
      super( "View Animation...", "view_anim.png" );
      setToolTipText( "Displays the animated frames" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'V' ), KeyEvent.CTRL_MASK ) } );
   }

   public static final boolean checkGIFColours() {
      return checkColours( Palette.GIF_MAX_COLOURS,
            "In GIF images the number of colours in each palette is limited to " + +Palette.GIF_MAX_COLOURS
                  + ". This picture uses " + String.valueOf( Tool.getPicture().getMaximalPaletteSize() )
                  + " colours." );
   }

   public static final boolean checkColours( int maxPalSize, String message ) {
      Picture pic = Tool.getPicture();
      if ( pic.getMaximalPaletteSize() > maxPalSize ) {
         if ( GUIUtil.question( MainFrame.INSTANCE, "Too many colours!", message + "\n\n"
               + "Do you want to reduce the number of colours?", "Reduce Colours", "Cancel" ) ) {
            pic.reduceColours( Palette.GIF_MAX_COLOURS ); // !ist richtig so
            return true;
         }
         return false;
      }
      return true;
   }

   public boolean prepareAction() {
      return (checkGIFColours());
   }

   public void startAction() {
      out = new ByteArrayOutputStream();
      success = GIFWriter.writeGIF( Tool.getPicture(), out );
   }

   public void finishAction() {
      if ( success ) {
         final DrawDialog dialog = new DrawDialog( "Animation" );

         ImageIcon icon = new ImageIcon( out.toByteArray() );
         JLabel label = new JLabel( icon );
         dialog.setModal( true );
         dialog.main.add( label, BorderLayout.CENTER );
         dialog.setUndecorated( false );
         dialog.setDefaultBorder();
         dialog.addRightButton( dialog.getApproveButton() );
         dialog.getApproveButton().setText( "Close" );
         dialog.getApproveButton().setMnemonic( 'c' );
         dialog.addButtonPanel();
         dialog.open();
         return;
      }
      Log.warning( "Couldn't create animation." );
   }
}