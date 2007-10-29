package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import jdomain.jdraw.gui.Tool;
import jdomain.util.Log;

/*
 * RemoveFrameAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class RemoveFrameAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected RemoveFrameAction() {
      super( "Remove Frame", "delete_frame.png" );
      setToolTipText( "Removes the current frame" );

      setEnabled( false );
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      Tool.getPicture().removeCurrentFrame();
      setEnabled( Tool.getPicture().getFrameCount() > 1 );
      if ( Log.DEBUG )
         Log.info( "Frame removed." );
   }

}
