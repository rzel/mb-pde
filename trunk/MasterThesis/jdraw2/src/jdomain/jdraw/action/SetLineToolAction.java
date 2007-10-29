package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

import jdomain.jdraw.gui.LineTool;
import jdomain.jdraw.gui.ToolPanel;

/*
 * SetLineToolAction.java - created on 15.11.2003
 * 
 * @author Michaela Behling
 */

public final class SetLineToolAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetLineToolAction() {
      super( "Line Tool", "line_tool.png" );
      setToolTipText( "Draws lines" );

      setAccelerators( new KeyStroke[] { KeyStroke.getKeyStroke( new Character( '4' ), 0 ) } );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      ToolPanel.INSTANCE.setCurrentTool( new LineTool(), DrawAction.getAction( SetLineToolAction.class ) );
   }

}
