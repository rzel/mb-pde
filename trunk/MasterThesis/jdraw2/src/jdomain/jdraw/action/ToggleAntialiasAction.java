package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import jdomain.jdraw.gui.ToolPanel;

/*
 * ToggleAntialiasAction.java - created on 16.11.2003
 * 
 * @author Michaela Behling
 */

public final class ToggleAntialiasAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private boolean antialiasOn = true;

   protected ToggleAntialiasAction() {
      super( "Antialias" );
      setToolTipText( "Turns antialias on/off" );
      setEnabled( false );
   }

   protected void setAntialias( boolean flag ) {
      antialiasOn = flag;
      ToolPanel.INSTANCE.selectAntialias( flag );
   }

   public boolean antialiasOn() {
      return isEnabled() && antialiasOn;
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      antialiasOn = !antialiasOn;
   }

}
