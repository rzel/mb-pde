package jdomain.jdraw.action;

import jdomain.jdraw.data.Palette;
import jdomain.jdraw.gui.Tool;

import java.awt.Color;
import java.awt.event.ActionEvent;

import jdomain.util.Log;

/*
 * AddColourAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class AddColourAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected AddColourAction() {
      super( "Add Colour" );
      setToolTipText( "Adds a new colour to this palette" );
   }

   protected void _actionPerformed( ActionEvent e ) {
      super._actionPerformed( e );
      Palette p = Tool.getCurrentPalette();
      p.addColour( Color.black );
      Log.info( "Colour #" + String.valueOf( p.size() ) + " created." );
   }

}
