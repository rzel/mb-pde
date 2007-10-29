package jdomain.jdraw.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.Main;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gui.MainFrame;
import jdomain.jdraw.gui.PixelTool;
import jdomain.jdraw.gui.ToolPanel;
import jdomain.jdraw.gui.undo.UndoManager;
import jdomain.util.Log;

/*
 * NewAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class NewAction extends DrawAction implements ContinuedAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected NewAction() {
      super( "New", "new.png" );
      setToolTipText( "Creates a new empty image" );
      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( 'N' ), KeyEvent.CTRL_MASK ) } );
   }

   protected boolean changesImage() {
      return false;
   }

   public void continueAction() {
      super._actionPerformed( null );
      ToolPanel.INSTANCE
            .setCurrentTool( PixelTool.INSTANCE, DrawAction.getAction( SetPixelToolAction.class ) );
      MainFrame.INSTANCE.setFileName( null );
      Main.setPicture( Picture.createDefaultPicture() );
      UndoManager.INSTANCE.reset();
      Log.info( "New image created." );
   }

   protected void _actionPerformed( ActionEvent e ) {
      LoseChanges.INSTANCE.checkUnsavedChanges( this );
   }

}
