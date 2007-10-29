package jdomain.jdraw.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import jdomain.jdraw.Settings;
import jdomain.jdraw.action.AboutAction;
import jdomain.jdraw.action.AddColourAction;
import jdomain.jdraw.action.AddFrameAction;
import jdomain.jdraw.action.CompressAction;
import jdomain.jdraw.action.CompressPaletteAction;
import jdomain.jdraw.action.CropAction;
import jdomain.jdraw.action.DecreaseZoomAction;
import jdomain.jdraw.action.DrawAction;
import jdomain.jdraw.action.EditFrameSettingsAction;
import jdomain.jdraw.action.ExitAction;
import jdomain.jdraw.action.FlipClipHorizontallyAction;
import jdomain.jdraw.action.FlipClipVerticallyAction;
import jdomain.jdraw.action.GrayScaleAction;
import jdomain.jdraw.action.HelpAction;
import jdomain.jdraw.action.IncreaseZoomAction;
import jdomain.jdraw.action.InsertAction;
import jdomain.jdraw.action.LoadAction;
import jdomain.jdraw.action.NewAction;
import jdomain.jdraw.action.RedoAction;
import jdomain.jdraw.action.ReduceColoursAction;
import jdomain.jdraw.action.ReducePaletteColoursAction;
import jdomain.jdraw.action.RemoveColourAction;
import jdomain.jdraw.action.RemoveFrameAction;
import jdomain.jdraw.action.ReopenAction;
import jdomain.jdraw.action.ResetAlphaValuesAction;
import jdomain.jdraw.action.ResetPaletteAlphaValuesAction;
import jdomain.jdraw.action.ResizeAction;
import jdomain.jdraw.action.RotateAction;
import jdomain.jdraw.action.RotateClipAction;
import jdomain.jdraw.action.SaveAction;
import jdomain.jdraw.action.SaveAsAction;
import jdomain.jdraw.action.SaveCompressedAction;
import jdomain.jdraw.action.ScaleAction;
import jdomain.jdraw.action.SearchDialogAction;
import jdomain.jdraw.action.SetMaxZoomAction;
import jdomain.jdraw.action.SetMinZoomAction;
import jdomain.jdraw.action.SetPreviousZoomAction;
import jdomain.jdraw.action.SettingsAction;
import jdomain.jdraw.action.SortPaletteAction;
import jdomain.jdraw.action.SwapColoursAction;
import jdomain.jdraw.action.ToggleGridAction;
import jdomain.jdraw.action.ToggleLocalPaletteAction;
import jdomain.jdraw.action.ToggleTransparencyAction;
import jdomain.jdraw.action.ToggleViewsAction;
import jdomain.jdraw.action.UndoAction;
import jdomain.jdraw.action.ViewAnimationAction;
import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.util.Log;
import jdomain.util.gui.FontDialog;
import jdomain.util.gui.GUIUtil;

/*
 * DrawMenu.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class DrawMenu extends JMenuBar {

   /** */
   private static final long serialVersionUID = 1L;
   private JMenu paletteMenu;
   private JMenu lastFilesMenu;

   public DrawMenu() {
      createFileMenu();
      createEditMenu();
      createViewMenu();
      createFrameMenu();
      createPaletteMenu();
      createHelpMenu();
      createDebugMenu();
   }

   public JMenu getPaletteMenu() {
      return paletteMenu;
   }

   private void createFileMenu() {
      JMenu menu = new JMenu( "File" );
      menu.setMnemonic( 'f' );

      menu.add( new DrawMenuItem( DrawAction.getAction( NewAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( LoadAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( SearchDialogAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( SaveAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( SaveAsAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( SaveCompressedAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( SettingsAction.class ) ) );
      lastFilesMenu = new JMenu( "Last Files" );
      menu.add( lastFilesMenu );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( ExitAction.class ) ) );
      add( menu );
   }

   public void buildLastFilesMenu() {
      lastFilesMenu.removeAll();
      ArrayList fList = Settings.INSTANCE.getFileList();
      final int len = fList.size();
      String f;
      JMenuItem item;
      ReopenAction action;
      for ( int i = 0; i < len; i++ ) {
         f = (String)fList.get( i );
         action = new ReopenAction( (new File( f )) );
         item = new JMenuItem( action );
         item.setToolTipText( (String)action.getValue( ReopenAction.LONG_DESCRIPTION ) );
         lastFilesMenu.add( item );
      }
   }

   private void createFrameMenu() {
      JMenu menu = new JMenu( "Frame" );
      menu.setMnemonic( 'm' );

      menu.add( new DrawMenuItem( DrawAction.getAction( AddFrameAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( RemoveFrameAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( RotateAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( InsertAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( GrayScaleAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( EditFrameSettingsAction.class ) ) );
      add( menu );
   }

   private void createEditMenu() {
      JMenu menu = new JMenu( "Edit" );
      menu.setMnemonic( 'e' );

      menu.add( new DrawMenuItem( DrawAction.getAction( UndoAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( RedoAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( ResizeAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( CropAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( ScaleAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( FlipClipHorizontallyAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( FlipClipVerticallyAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( RotateClipAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( CompressAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( ReduceColoursAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( ResetAlphaValuesAction.class ) ) );
      add( menu );
   }

   private void createViewMenu() {
      JMenu menu = new JMenu( "View" );
      menu.setMnemonic( 'v' );

      menu.add( new DrawMenuItem( DrawAction.getAction( IncreaseZoomAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( DecreaseZoomAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( SetPreviousZoomAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( SetMaxZoomAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( SetMinZoomAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( ToggleGridAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( ToggleViewsAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( ToggleTransparencyAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( ViewAnimationAction.class ) ) );
      add( menu );
   }

   private void createPaletteMenu() {
      JMenu menu = new JMenu( "Palette" );
      menu.setMnemonic( 'p' );
      menu.add( new DrawMenuItem( DrawAction.getAction( ToggleLocalPaletteAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( AddColourAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( SwapColoursAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( RemoveColourAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( SortPaletteAction.class ) ) );
      menu.addSeparator();
      menu.add( new DrawMenuItem( DrawAction.getAction( CompressPaletteAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( ReducePaletteColoursAction.class ) ) );
      menu.add( new DrawMenuItem( DrawAction.getAction( ResetPaletteAlphaValuesAction.class ) ) );
      paletteMenu = menu;
      add( menu );
   }

   private void createHelpMenu() {
      JMenu menu = new JMenu( "Help" );
      menu.setMnemonic( 'h' );

      menu.add( new DrawMenuItem( DrawAction.getAction( AboutAction.class ) ) );      
      menu.add( new DrawMenuItem( DrawAction.getAction( HelpAction.class ) ) );
      add( menu );
   }

   private void createDebugMenu() {
      if ( Log.DEBUG ) {
         JMenu menu = new JMenu( "Debug" );
         menu.setMnemonic( 'D' );

         JMenuItem item;

         final String text = "This is a simple test text!" + " And so that we really see an effect, "
               + "let's have a longer text passage right here." + "\nAnd a new line starts here."
               + "\n\nCan you see a blank line?";

         item = new JMenuItem( "Show Warning" );
         item.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
               GUIUtil.warning( MainFrame.INSTANCE, text );
            }
         } );
         menu.add( item );

         item = new JMenuItem( "Show Error" );
         item.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
               GUIUtil.error( MainFrame.INSTANCE, text );
            }
         } );
         menu.add( item );

         item = new JMenuItem( "Show Info" );
         item.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
               GUIUtil.info( MainFrame.INSTANCE, text );
            }
         } );
         menu.add( item );

         item = new JMenuItem( "Show Yes/No" );
         item.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
               GUIUtil.yesNo( MainFrame.INSTANCE, "Do you really wanna click?", text );
            }
         } );
         menu.add( item );

         menu.addSeparator();

         item = new JMenuItem( "Show Font-Dialog..." );
         item.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
               FontDialog d = new FontDialog( MainFrame.INSTANCE );
               Palette pal = Tool.getCurrentPalette();
               Picture pic = Tool.getPicture();
               d.setFontForeground( pal.getColour( pic.getForeground() ).getColour() );
               d.setFontBackground( pal.getColour( pic.getBackground() ).getColour() );
               d.open();
            }
         } );
         menu.add( item );

         menu.addSeparator();

         menu.add( new JMenuItem( GUIUtil.createSaveLookFeelPropertiesAction( "properties.log" ) ) );

         // item = new JMenuItem( "Show palette info..." );
         // item.addActionListener( new ActionListener() {
         // public void actionPerformed( ActionEvent e ) {
         // Frame f = Tool.getPicture().getFrame( 0 );
         // Log.debug( "frame0" );
         // Log.debug( "global palette? " + f.getPalette().isGlobalPalette() );
         // Log.debug( "col0 = " + f.getPalette().getColour( 0 ) );
         // Log.debug( "id = "+f.hashCode());
         // f = Tool.getPicture().getFrame( 1 );
         // Log.debug( "frame1" );
         // Log.debug( "global palette? " + f.getPalette().isGlobalPalette() );
         // Log.debug( "col0 = " + f.getPalette().getColour( 0 ) );
         // Log.debug( "id = "+f.hashCode());
         // }
         // } );
         // menu.add( item );

         add( menu );
      }
   }


   private class DrawMenuItem extends JMenuItem {
      /** */
      private static final long serialVersionUID = 1L;

      public DrawMenuItem( DrawAction action ) {
         super( action );
         this.setToolTipText( action.getToolTipText() );
         if ( getIcon() != null ) {
            Dimension dim = getPreferredSize();
            dim.height = getIcon().getIconHeight() + 2;
            setPreferredSize( dim );
         }
      }
   }

}