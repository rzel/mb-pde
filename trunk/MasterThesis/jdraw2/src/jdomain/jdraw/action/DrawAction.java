package jdomain.jdraw.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import jdomain.jdraw.gui.undo.UndoManager;
import jdomain.util.Assert;
import jdomain.util.Log;
import jdomain.util.ResourceLoader;

/*
 * DrawAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public abstract class DrawAction extends AbstractAction {

   private static HashMap instances = new HashMap();

   private KeyStroke[] keyStrokes = null;

   public DrawAction( String name ) {
      super( name );
      addInstance( this );
   }

   public DrawAction( String name, String iconName ) {
      super( name, ResourceLoader.getImage( "jdomain/jdraw/images/" + iconName, 24 ) );
      addInstance( this );
   }

   protected boolean changesImage() {
      return true;
   }

   public final String getToolTipText() {
      String desc = (String)getValue( LONG_DESCRIPTION );
      if ( desc != null ) {
         return desc;
      }
      return (String)getValue( NAME );
   }

   protected final void setToolTipText( String text ) {
      putValue( LONG_DESCRIPTION, text );
   }

   private static void addInstance( DrawAction action ) {
      Assert.isNull( instances.get( action.getClass() ), "action: duplicate action" );
      instances.put( action.getClass(), action );
   }

   public static DrawAction getAction( Class aClass ) {
      try {
         DrawAction action = (DrawAction)instances.get( aClass );
         if ( action == null ) {
            action = (DrawAction)aClass.newInstance();
            instances.put( aClass, action );
         }
         return action;
      }
      catch ( Exception e ) {
         Log.exception( e );
         return null;
      }
   }

   public final KeyStroke[] getAccelerators() {
      return keyStrokes;
   }

   protected final void setAccelerators( KeyStroke[] strokes ) {
      putValue( ACCELERATOR_KEY, strokes[0] );
      if ( strokes.length > 0 ) {
         keyStrokes = strokes;
      }
   }

   public final void actionPerformed( ActionEvent e ) {
      _actionPerformed( e );
   }

   protected void _actionPerformed( ActionEvent e ) {
      if ( changesImage() ) {
         Log.debug( getClass().getName() + " changes image..." );
         UndoManager.INSTANCE.setHasChanges( true );
      }
   }

   public final void actionPerformed() {
      if ( isEnabled() ) {
         actionPerformed( null );
      }
   }

   public boolean hasFinished() {
      return true;
   }

   public final boolean equals( Object o ) {
      return o.getClass() == this.getClass();
   }

   public final int hashCode() {
      return getClass().getName().hashCode();
   }
}
