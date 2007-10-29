package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import jdomain.jdraw.gui.RectangleTool;
import jdomain.jdraw.gui.ToolPanel;

/*
 * SetRectangleToolAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SetRectangleToolAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetRectangleToolAction() {
      super( "Rectangle Tool", "rectangle_tool.png" );
      setToolTipText( "Draws rectangles" );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      ToolPanel.INSTANCE.setCurrentTool( new RectangleTool(), DrawAction
            .getAction( SetRectangleToolAction.class ) );
   }

}
