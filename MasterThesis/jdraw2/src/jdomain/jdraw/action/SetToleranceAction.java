package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import jdomain.jdraw.gui.ToleranceDialog;

/*
 * SetToleranceAction.java - created on 19.12.2003
 * 
 * @author Michaela Behling
 */

public final class SetToleranceAction extends DrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SetToleranceAction() {
      super( "Fill Tolerance..." );
      setToolTipText( "Sets the tolerance of the fill tool" );
   }

   protected boolean changesImage() {
      return false;
   }

   protected void _actionPerformed( ActionEvent e ) {
      ToleranceDialog td = new ToleranceDialog();
      td.open();
   }

}
