package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import jdomain.jdraw.gui.RectangleTool;
import jdomain.jdraw.gui.ToolPanel;

/*
 * SetFilledRectangleToolAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SetFilledRectangleToolAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetFilledRectangleToolAction() {
      super( "Filled Rectangle Tool", "filled_rectangle_tool.png" );
      setToolTipText( "Draws filled rectangles" );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      ToolPanel.INSTANCE.setCurrentTool( new RectangleTool( true ), DrawAction
            .getAction( SetFilledRectangleToolAction.class ) );
   }

}
