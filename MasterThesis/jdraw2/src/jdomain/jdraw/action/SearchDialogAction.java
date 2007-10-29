package jdomain.jdraw.action;

import java.awt.event.ActionEvent;

import jdomain.jdraw.gui.SearchFrame;

/*
 * SearchDialogAction.java - created on 19.12.2003
 * 
 * @author Michaela Behling
 */

public final class SearchDialogAction extends DrawAction implements ContinuedAction {

   /** */
   private static final long serialVersionUID = 1L;
   private String fileName;

   protected SearchDialogAction() {
      super( "Search Images..." );
      setToolTipText( "Searches a directory recursivly for images" );
   }

   protected boolean changesImage() {
      return false;
   }

   public void continueAction() {
      LoadAction.openImage( fileName );
   }

   protected void _actionPerformed( ActionEvent e ) {
      SearchFrame s = new SearchFrame();
      s.open();
      if ( s.getResult() == SearchFrame.APPROVE_ACTION ) {
         fileName = s.getImageName();
         LoseChanges.INSTANCE.checkUnsavedChanges( this );
      }
   }

}
