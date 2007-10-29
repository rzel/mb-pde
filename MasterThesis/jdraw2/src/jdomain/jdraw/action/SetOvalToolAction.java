package jdomain.jdraw.action;

import jdomain.jdraw.gui.OvalTool;
import jdomain.jdraw.gui.ToolPanel;

import java.awt.event.ActionEvent;

/*
 * SetOvalToolAction.java - created on 15.11.2003
 * 
 * @author Michaela Behling
 */

public final class SetOvalToolAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetOvalToolAction() {
      super( "Oval Tool", "oval_tool.png" );
      setToolTipText( "Draws ovals" );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      ToolPanel.INSTANCE.setCurrentTool( new OvalTool( false ), DrawAction
            .getAction( SetOvalToolAction.class ) );
   }

}
