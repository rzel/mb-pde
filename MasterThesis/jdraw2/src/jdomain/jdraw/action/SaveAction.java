package jdomain.jdraw.action;

import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.KeyStroke;

import jdomain.jdraw.Settings;
import jdomain.jdraw.gio.GIFWriter;
import jdomain.jdraw.gio.IconWriter;
import jdomain.jdraw.gio.JPEGWriter;
import jdomain.jdraw.gio.PNGWriter;
import jdomain.jdraw.gui.MainFrame;
import jdomain.jdraw.gui.Tool;
import jdomain.jdraw.gui.undo.UndoManager;
import jdomain.util.Log;
import jdomain.util.Util;
import jdomain.util.gui.GUIUtil;

/*
 * SaveAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SaveAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private String fileName;
   protected boolean success;

   protected SaveAction() {
      super( "Save", "save.png" );
      setToolTipText( "Saves the current image" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'S' ), KeyEvent.CTRL_MASK ) } );
   }

   public boolean successfullySaved() {
      return success;
   }

   public boolean prepareAction() {
      fileName = MainFrame.INSTANCE.getFileName();
      if ( fileName == null ) {
         DrawAction.getAction( SaveAsAction.class ).actionPerformed();
         return false;
      }

      if ( (Tool.getPicture().getFrameCount() > 1) && (LoadAction.isPNG( fileName )) ) {
         boolean proceed = GUIUtil.question( MainFrame.INSTANCE, "Single Frame Format",
               "The PNG format supports a single frame only. "
                     + "If you proceed, only the current frame will be saved. Do you realise this?",
               "Yeah, yeah", "Oops" );
         return proceed;
      }
      return true;
   }

   private boolean saveJDraw( String aFileName ) {
      ObjectOutputStream out = null;
      try {
         out = new ObjectOutputStream( new FileOutputStream( aFileName ) );
         out.writeObject( Tool.getPicture() );
         return true;
      }
      catch ( Exception e ) {
         Log.exception( e );
         return false;
      }
      finally {
         Util.close( out );
      }
   }

   private boolean saveInterlaced() {
      SaveAsAction action = (SaveAsAction)getAction( SaveAsAction.class );
      return action.saveInterlaced();
   }

   public void startAction() {
      if ( LoadAction.isGIF( fileName ) ) {
         success = ViewAnimationAction.checkGIFColours()
               && GIFWriter.writeGIF( MainFrame.INSTANCE.getPicture(), fileName, saveInterlaced() );
      }
      else if ( LoadAction.isJDraw( fileName ) ) {
         success = saveJDraw( fileName );
      }
      else if ( LoadAction.isICO( fileName ) ) {
         success = IconWriter.writeIcon( MainFrame.INSTANCE.getPicture(), fileName );
      }
      else if ( LoadAction.isPNG( fileName ) ) {
         success = PNGWriter.writePNG( MainFrame.INSTANCE.getPicture(), fileName, saveInterlaced() );
      }
      else if ( LoadAction.isJPEG( fileName ) ) {
         success = JPEGWriter.writeJPEG( MainFrame.INSTANCE.getPicture(), fileName );
      }
      else {
         success = false;
         Log.warning( "Image format not supported." );
      }
   }

   public void finishAction() {
      if ( success ) {
         Settings.INSTANCE.addLastFile( fileName );
         Settings.INSTANCE.save();
         UndoManager.INSTANCE.reset();
         Log.info( "Saved to " + fileName );
      }
   }

}
