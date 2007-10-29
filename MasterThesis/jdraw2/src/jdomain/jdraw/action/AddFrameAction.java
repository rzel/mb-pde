package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import jdomain.jdraw.gui.Tool;
import jdomain.util.Log;

/*
 * AddFrameAction.java - created on 30.10.2003
 * 
 * @author Michaela Behling
 */

public final class AddFrameAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected AddFrameAction() {
      super( "Add Frame", "frame_new.png" );
      setToolTipText( "Adds a new frame" );
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      Tool.getPicture().addFrame();
      DrawAction.getAction( RemoveFrameAction.class ).setEnabled( true );
      Log.info( "Frame added" );
   }

}
