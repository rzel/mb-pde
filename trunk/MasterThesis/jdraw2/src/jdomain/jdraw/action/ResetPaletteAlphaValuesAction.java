package jdomain.jdraw.action;

import jdomain.jdraw.gui.Tool;

/*
 * ResetPaletteAlphaValuesAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class ResetPaletteAlphaValuesAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   // private int result = 0;

   protected ResetPaletteAlphaValuesAction() {
      super( "Reset Alpha Values" );
      setToolTipText( "Removes the alpha channel in this palette" );
   }

   public boolean prepareAction() {
      return true;
   }

   public void startAction() {
      Tool.getCurrentPalette().removeAlphaValues();
   }

   public void finishAction() {
   }
}
