package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import jdomain.jdraw.gui.ClipTool;
import jdomain.jdraw.gui.ToolPanel;

/*
 * SetClipToolAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SetClipToolAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetClipToolAction() {
      super( "Clip Tool", "clip_tool.png" );
      setToolTipText( "Offers clip functionality" );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      // GUIUtil.info(
      // MainFrame.INSTANCE,
      // "Not implemented yet!",
      // "The Clip Tool isn't fully implemented yet. Check for available
      // updates..."
      // + "\n\nSorry about that!",
      // "Oops");
      ToolPanel.INSTANCE.setCurrentTool( new ClipTool(), DrawAction.getAction( SetClipToolAction.class ) );
   }
}
