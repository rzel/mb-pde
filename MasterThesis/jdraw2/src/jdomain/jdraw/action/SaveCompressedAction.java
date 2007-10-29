package jdomain.jdraw.action;

import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gui.MainFrame;

/*
 * SaveCompressedAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SaveCompressedAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SaveCompressedAction() {
      super( "Save Compressed" );
      setToolTipText( "Compresses and saves the current image" );
   }

   public boolean prepareAction() {
      return true;
   }

   public void startAction() {
      Picture picture = MainFrame.INSTANCE.getPicture();
      picture.compress();
      DrawAction.getAction( SaveAction.class ).actionPerformed();
   }

   public void finishAction() {
   }

}
