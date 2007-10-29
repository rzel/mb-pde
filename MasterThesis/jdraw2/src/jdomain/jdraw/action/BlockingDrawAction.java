package jdomain.jdraw.action;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import jdomain.jdraw.gui.DrawDialog;
import jdomain.jdraw.gui.MainFrame;
import jdomain.util.Log;
import jdomain.util.ResourceLoader;
import jdomain.util.Util;
import jdomain.util.gui.GUIUtil;

/*
 * BlockingDrawAction.java - created on 30.10.2003
 * 
 * @author Michaela Behling
 */

public abstract class BlockingDrawAction extends DrawAction {

   private DrawDialog dialog;
   private boolean finished;

   public BlockingDrawAction( String name ) {
      super( name );
   }

   public BlockingDrawAction( String name, String iconName ) {
      super( name, iconName );
   }

   public void setBusyCursor() {
      MainFrame.INSTANCE.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
   }

   public void setDefaultCursor() {
      MainFrame.INSTANCE.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
   }

   public abstract boolean prepareAction();

   public abstract void startAction();

   public abstract void finishAction();

   public void prepare() {
      Log.info( "Please wait..." );
      dialog = new DrawDialog( "Please wait...", true );
      dialog.setModal( false );
      dialog.getContentPane().add( new JLabel( ResourceLoader.getImage( "jdomain/jdraw/images/busy.png" ) ),
            BorderLayout.CENTER );
      dialog.open();
   }

   public void start() {
      try {
         startAction();
      }
      catch ( Throwable e ) {
         // ignore
      }
      finally {
         actionFinished();
      }
   }

   private void finish() {
      dialog.close();
      Log.info( "" );
   }

   public boolean hasFinished() {
      return finished;
   }

   protected final void _actionPerformed( ActionEvent e ) {
      finished = false;
      if ( this instanceof ContinuedAction ) {
         LoseChanges.INSTANCE.checkUnsavedChanges( (ContinuedAction)this );
      }
      else {
         continueAction();
      }
   }

   public void continueAction() {
      if ( !prepareAction() ) {
         return;
      }
      Thread t = new Thread() {
         public void run() {
            try {
               GUIUtil.invokeLater( BlockingDrawAction.this, "setBusyCursor" );
               Util.delay( 200 );
               GUIUtil.invokeLater( BlockingDrawAction.this, "prepare" );
               Util.delay( 400 );
               GUIUtil.invokeLater( BlockingDrawAction.this, "start" );
            }
            catch ( Throwable e ) {
               // ignore
            }
            finally {
               finished = true;
            }
         }
      };
      t.start();
   }

   public void actionFinished() {
      finish();
      GUIUtil.invokeLater( this, "setDefaultCursor" );
      GUIUtil.invokeLater( this, "finishAction" );
      super._actionPerformed( null );
   }

}