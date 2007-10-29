package jdomain.jdraw.action;

import jdomain.jdraw.gui.Tool;

/*
 * ResetAlphaValuesAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class ResetAlphaValuesAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   // private int result = 0;

   protected ResetAlphaValuesAction() {
      super( "Reset Alpha Values" );
      setToolTipText( "Removes the alpha channel in this picture" );
   }

   public boolean prepareAction() {
      return true;
   }

   public void startAction() {
      Tool.getPicture().removeAlphaChannel();

   }

   public void finishAction() {

   }
}
