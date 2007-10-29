package jdomain.jdraw.action;

import jdomain.jdraw.data.Palette;
import jdomain.jdraw.gui.PixelTool;
import jdomain.jdraw.gui.Tool;
import jdomain.jdraw.gui.ToolPanel;

/*
 * ReducePaletteColoursAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class ReducePaletteColoursAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private int result = 0;

   protected ReducePaletteColoursAction() {
      super( "Reduce palette to " + String.valueOf( Palette.GIF_MAX_COLOURS ) + " colours" );
      setToolTipText( "Reduces each frame using this palette to maximal 255 colours" );
   }

   public boolean prepareAction() {
      return true;
   }

   public void startAction() {
      ToolPanel.INSTANCE
            .setCurrentTool( PixelTool.INSTANCE, DrawAction.getAction( SetPixelToolAction.class ) );
      Palette pal = Tool.getCurrentPalette();
      result = pal.reduceColours( Palette.GIF_MAX_COLOURS );
   }

   public void finishAction() {
      CompressAction.showResult( result );
   }
}
