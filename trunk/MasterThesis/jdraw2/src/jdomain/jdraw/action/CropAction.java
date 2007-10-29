package jdomain.jdraw.action;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import jdomain.jdraw.gui.ClipPanel;
import jdomain.jdraw.gui.PixelTool;
import jdomain.jdraw.gui.Tool;
import jdomain.jdraw.gui.ToolPanel;
import jdomain.jdraw.gui.undo.UndoManager;
import jdomain.util.Log;

/*
 * CropAction.java - created on 26.11.2003
 * 
 * @author Michaela Behling
 */

public final class CropAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected CropAction() {
      super( "Crop Image", "crop.png" );
      setToolTipText( "Crops this image to the selected rectangle" );
      setEnabled( false );
   }

   protected void _actionPerformed( ActionEvent e ) {
      ClipPanel clipPanel = Tool.getCurrentFramePanel().getLayeredPane().getClipPanel();
      Rectangle r = Tool.getRealBounds( clipPanel.getBounds() );
      int x = Math.max( 0, r.x );
      int y = Math.max( 0, r.y );
      int x2 = Math.min( Tool.getPictureWidth() - 1, r.x + r.width - 1 );
      int y2 = Math.min( Tool.getPictureHeight() - 1, r.y + r.height - 1 );
      int w = (x2 - x) + 1;
      int h = (y2 - y) + 1;

      if ( Tool.getPicture().crop( x, y, w, h ) ) {
         setEnabled( false );
         UndoManager.INSTANCE.reset();
         ToolPanel.INSTANCE.setCurrentTool( PixelTool.INSTANCE, DrawAction
               .getAction( SetPixelToolAction.class ) );
         Log.info( "Cropped." );
      }
   }

}
