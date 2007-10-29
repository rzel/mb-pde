package jdomain.jdraw.action;

import jdomain.jdraw.gui.OvalTool;
import jdomain.jdraw.gui.ToolPanel;

import java.awt.event.ActionEvent;

/*
 * SetFilledOvalToolAction.java - created on 15.11.2003
 * 
 * @author Michaela Behling
 */

public final class SetFilledOvalToolAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetFilledOvalToolAction() {
      super( "Filled Oval Tool", "filled_oval_tool.png" );
      setToolTipText( "Draws filled ovals" );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      ToolPanel.INSTANCE.setCurrentTool( new OvalTool( true ), DrawAction
            .getAction( SetFilledOvalToolAction.class ) );
   }

}
