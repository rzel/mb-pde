package jdomain.jdraw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import jdomain.jdraw.action.DrawAction;
import jdomain.jdraw.action.SetTextToolAction;
import jdomain.jdraw.data.DataChangeListener;
import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.data.event.ChangeEvent;
import jdomain.util.gui.FontDialog;
import jdomain.util.gui.StandardMainFrame;
import jdomain.util.gui.FontDialog.IApplyCallback;

/**
 * @author J-Domain [Michaela Behling]
 */
public class TextFontDialog extends FontDialog implements DataChangeListener,
      IApplyCallback {

   /** */
   private static final long serialVersionUID = 1L;

   public TextFontDialog( StandardMainFrame parent, String title,
         JCheckBox[] boxes ) {
      super( parent, title, boxes );
      setModal( false );
      setApplyCallback( this );

      getApproveButton().addActionListener( new ActionListener() {
         public void actionPerformed( ActionEvent e ) {
            apply();
         }
      } );
   }

   public void setVisible( boolean b ) {
      super.setVisible( b );
      Picture pic = Tool.getPicture();
      Palette pal = Tool.getCurrentPalette();
      if ( b ) {
         pic.addDataChangeListener( this );
         pal.addDataChangeListener( this );
      }
      else {
         pic.removeDataChangeListener( this );
         pal.removeDataChangeListener( this );
      }
   }

   private void updateBackground() {
      Palette pal = Tool.getCurrentPalette();
      final int background = Tool.getPicture().getBackground();
      setFontBackground( pal.getColour( background ).getColour() );
   }

   private void updateForeground() {
      Palette pal = Tool.getCurrentPalette();
      final int foreground = Tool.getPicture().getForeground();
      setFontForeground( pal.getColour( foreground ).getColour() );
   }

   public void dataChanged( ChangeEvent e ) {
      switch ( e.changeType ) {
         case Palette.PICTURE_BACKGROUND_COLOUR_CHANGED:
            updateBackground();
            break;
         case Palette.PICTURE_FOREGROUND_COLOUR_CHANGED:
            updateForeground();
            break;
         case Palette.PALETTE_COLOUR_CHANGED:
            updateForeground();
            updateBackground();
            break;
         case Picture.PALETTE_CHANGED:
            close();
      }
   }

   public final void apply() {
      setResult( APPROVE_ACTION );
      SetTextToolAction action = (SetTextToolAction) DrawAction
            .getAction( SetTextToolAction.class );
      action.apply();
   }
}