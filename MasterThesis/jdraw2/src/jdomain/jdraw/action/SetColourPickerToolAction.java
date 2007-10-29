package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.ColourPickerTool;
import jdomain.jdraw.gui.ToolPanel;

/*
 * SetColourPickerToolAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SetColourPickerToolAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetColourPickerToolAction() {
      super( "Colour Picker Tool", "colorpicker.png" );
      setToolTipText( "Selects the foreground or background colour" );

      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( '3' ), 0 ) } );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      ToolPanel.INSTANCE.setCurrentTool( new ColourPickerTool(), DrawAction
            .getAction( SetColourPickerToolAction.class ) );
   }

}
