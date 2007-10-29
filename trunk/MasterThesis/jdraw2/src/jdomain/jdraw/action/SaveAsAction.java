package jdomain.jdraw.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.DrawBrowser;
import jdomain.jdraw.gui.MainFrame;
import jdomain.util.gui.GUIUtil;

/*
 * SaveAsAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SaveAsAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private boolean saveInterlaced = false;

   protected SaveAsAction() {
      super( "Save As...", "save_as.png" );
      setToolTipText( "Saves the current image under a new file name" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'A' ), KeyEvent.CTRL_MASK ) } );
   }

   protected boolean changesImage() {
      return false;
   }

   public void actionPerformed( DrawAction saveAction ) {
      ((SaveAction)DrawAction.getAction( SaveAction.class )).success = false;
      saveInterlaced = false;
      File file = DrawBrowser.INSTANCE.saveImage();
      if ( file != null ) {
         if ( file.exists() ) {
            boolean overwrite = GUIUtil.question( MainFrame.INSTANCE, "Replace existing file?",
                  "The file <font color=blue>" + file.getName()
                        + "</font> already exists. Do you want to replace it?", "Replace", "Cancel" );
            if ( !overwrite ) {
               return;
            }
         }
         saveInterlaced = DrawBrowser.INSTANCE.saveInterlaced();
         MainFrame.INSTANCE.setFileName( file.getAbsolutePath() );
         saveAction.actionPerformed();
      }
   }

   public boolean saveInterlaced() {
      return saveInterlaced;
   }

   protected void _actionPerformed( ActionEvent e ) {
      actionPerformed( DrawAction.getAction( SaveAction.class ) );
   }

}
