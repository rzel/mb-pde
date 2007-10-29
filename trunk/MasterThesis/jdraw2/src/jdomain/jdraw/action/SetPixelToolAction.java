package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.PixelTool;
import jdomain.jdraw.gui.ToolPanel;

/*
 * SetPixelToolAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SetPixelToolAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetPixelToolAction() {
      super( "Pixel Tool", "pixel_tool.png" );
      setToolTipText( "Draws pixels in the current foreground or background colour" );

      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( '1' ), 0 ) } );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      ToolPanel.INSTANCE
            .setCurrentTool( PixelTool.INSTANCE, DrawAction.getAction( SetPixelToolAction.class ) );
   }

}
