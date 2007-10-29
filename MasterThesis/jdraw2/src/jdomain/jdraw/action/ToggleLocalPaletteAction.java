package jdomain.jdraw.action;

import java.awt.Color;
import java.awt.event.ActionEvent;

import jdomain.jdraw.data.Frame;
import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gui.Tool;

/*
 * ToggleLocalPaletteAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class ToggleLocalPaletteAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected ToggleLocalPaletteAction() {
      super( "Create Local Palette" );
      setToolTipText( "Creates a local palette for this frame" );
   }

   public void adjustMenuItems() {
      Palette pal = Tool.getCurrentFrame().getPalette();
      if ( pal.isGlobalPalette() ) {
         putValue( NAME, "Create Local Palette" );
      }
      else {
         putValue( NAME, "Discard Local Palette" );
      }
   }

   protected void _actionPerformed( ActionEvent e ) {
      Picture pic = Tool.getPicture();
      Frame frame = pic.getCurrentFrame();
      Palette global = pic.getPalette();
      Palette pal = frame.getPalette();
      if ( !pal.isGlobalPalette() ) { // switching from local -> global
         while ( global.size() < pal.size() ) {
            global.addColour( Color.BLACK );
         }
      }

      super._actionPerformed( e );
      frame.toggleLocalPalette();
      adjustMenuItems();
   }

}
