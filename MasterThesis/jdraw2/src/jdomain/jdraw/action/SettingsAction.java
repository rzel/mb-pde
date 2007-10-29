package jdomain.jdraw.action;

import jdomain.jdraw.Settings;
import jdomain.jdraw.gui.SettingsDialog;
import jdomain.util.Log;

/*
 * SettingsAction - created on 15.12.2005
 * 
 * @author Michaela Behling
 */

public final class SettingsAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;

   protected SettingsAction() {
      super( "Settings..." );
      setToolTipText( "JDraw settings" );
   }

   protected boolean changesImage() {
      return false;
   }

   public boolean prepareAction() {
      SettingsDialog d = new SettingsDialog();
      d.open();
      if ( d.getResult() == SettingsDialog.APPROVE_ACTION ) {
         Settings.INSTANCE.setJPEGQuality( d.getQuality() );
         Settings.INSTANCE.setTolerance( d.getTolerance() );
         Settings.INSTANCE.save();
         return true;
      }
      return false;
   }

   public void startAction() {

   }

   public void finishAction() {
      Log.info( "Settings updated." );
   }
}
