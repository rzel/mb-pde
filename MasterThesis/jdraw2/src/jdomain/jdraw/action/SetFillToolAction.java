package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.FillTool;
import jdomain.jdraw.gui.ToolPanel;

/*
 * SetFillToolAction.java - created on 15.11.2003
 * 
 * @author Michaela Behling
 */

public final class SetFillToolAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetFillToolAction() {
      super( "Fill Tool", "fill_tool.png" );
      setToolTipText( "Fills regions" );

      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( '2' ), 0 ) } );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      ToolPanel.INSTANCE.setCurrentTool( FillTool.INSTANCE, DrawAction.getAction( SetFillToolAction.class ) );
   }

}
