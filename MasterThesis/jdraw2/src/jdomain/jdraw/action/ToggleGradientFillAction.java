package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import jdomain.jdraw.gui.ToolPanel;

/*
 * ToggleGradientFillAction.java - created on 26.11.2003
 * 
 * @author Michaela Behling
 */

public final class ToggleGradientFillAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private boolean gradientFill = false;

   protected ToggleGradientFillAction() {
      super( "Gradient Fill" );
      setToolTipText( "Turns gradient fill on/off" );
      setEnabled( false );
   }

   protected void setGradientFill( boolean flag ) {
      gradientFill = flag;
      ToolPanel.INSTANCE.selectGradientFill( flag );
   }

   public boolean gradientFillOn() {
      return isEnabled() && gradientFill;
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      gradientFill = !gradientFill;
   }

}
