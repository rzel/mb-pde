package jdomain.jdraw.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

import jdomain.jdraw.action.DrawAction;
import jdomain.jdraw.action.RemoveFrameAction;
import jdomain.jdraw.action.ToggleLocalPaletteAction;
import jdomain.jdraw.data.Frame;
import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.data.event.ChangeEvent;
import jdomain.util.Assert;
import jdomain.util.ResourceLoader;

/*
 * Created on 28-Oct-2003
 * 
 * @author michaela
 */

public final class FolderPanel extends JPanel implements ChangeListener {

   /** */
   private static final long serialVersionUID = 1L;

   private static int grid;

   private static final ArrayList GRID_LISTENERS = new ArrayList();
   public static final FolderPanel INSTANCE = new FolderPanel();

   public static final int MAX_GRID = 25;
   public static final int MIN_GRID = 1;

   private final JTabbedPane frameFolder = new JTabbedPane();
   private final ImageIcon frameIcon = ResourceLoader.getImage( "jdomain/jdraw/images/frame.png", 16 );

   private Picture picture;
   private boolean justUpdating = false;

   private boolean showGrid = true;

   private FolderPanel() {
      super( new BorderLayout( 0, 0 ) );

      add( frameFolder, BorderLayout.CENTER );
      frameFolder.addChangeListener( this );
      frameFolder.putClientProperty( "jgoodies.embeddedTabs", Boolean.TRUE );
      frameFolder.setTabPlacement( JTabbedPane.BOTTOM );
      frameFolder.setTabLayoutPolicy( JTabbedPane.SCROLL_TAB_LAYOUT );
      setGrid( 15 );
   }

   public static int getGrid() {
      return grid;
   }

   public static void setGrid( int g ) {
      if ( g == grid ) {
         return;
      }
      Assert.isTrue( (g >= MIN_GRID) && (g <= MAX_GRID), "gui: grid out of range." );
      final int oldGrid = grid;
      grid = g;

      Iterator it = GRID_LISTENERS.iterator();
      while ( it.hasNext() ) {
         ((GridListener)it.next()).gridChanged( oldGrid, grid );
      }
   }

   private void addFrame( int index ) {
      FramePanel f = new FramePanel();
      f.setFrame( picture.getFrame( index ) );
      frameFolder.insertTab( "#" + String.valueOf( index + 1 ), frameIcon, f, null, index );
      frameChanged();
   }

   public static void addGridListener( GridListener aListener ) {
      GRID_LISTENERS.add( aListener );
   }

   public void changeFrameAdded( ChangeEvent e ) {
      addFrame( e.getIntValue() );
   }

   public void changeFrameRemoved( ChangeEvent e ) {
      removeFrame( e.getIntValue() );
   }

   public void changeFrameSet( ChangeEvent e ) {
      FramePanel framePanel = (FramePanel)frameFolder.getComponentAt( e.getNewInt() );
      frameFolder.setSelectedComponent( framePanel );
      frameChanged();
   }

   public Image createOffScreenImage() {
      Frame f = Tool.getCurrentFrame();
      return createOffScreenImage( 0, 0, f.getWidth(), f.getHeight() );
   }

   public Image createOffScreenImage( int x, int y, int width, int height ) {
      DrawPanel draw = Tool.getDrawPanel();

      Image image = draw.createImage( width, height );
      Graphics g = image.getGraphics();
      Palette palette = Tool.getCurrentPalette();
      Picture pic = Tool.getPicture();
      Frame f = getCurrentFrame();
      int backCol = pic.getBackground();
      if ( f.getTransparentColour() != -1 ) {
         backCol = f.getTransparentColour();
      }
      Color back = palette.getColour( backCol ).getColour();
      g.setColor( back );
      g.translate( -x, -y );
      g.fillRect( x, y, width, height );
      final int oldGrid = grid;
      grid = 1;
      draw.paintClip( g, false );
      g.translate( x, y );
      grid = oldGrid;
      return image;
   }

   private void frameChanged() {
      MouseHandler.INSTANCE.frameChanged();

      PalettePanel.INSTANCE.setPalette( Tool.getCurrentPalette() );
      PreviewPanel.INSTANCE.setClip( Tool.getCurrentFrame() );
      final int frames = getFrameCount();
      DrawAction.getAction( RemoveFrameAction.class ).setEnabled( frames > 1 );
      ((ToggleLocalPaletteAction)DrawAction.getAction( ToggleLocalPaletteAction.class )).adjustMenuItems();

      ImageIcon inactive = ResourceLoader.getImage( "jdomain/jdraw/images/frame_inactive.png" );
      ImageIcon active = ResourceLoader.getImage( "jdomain/jdraw/images/frame.png" );
      final int current = Tool.getPicture().getCurrentFrameIndex();
      for ( int i = 0; i < frames; i++ ) {
         if ( i == current ) {
            frameFolder.setIconAt( i, active );
         }
         else {
            frameFolder.setIconAt( i, inactive );
         }
      }
      getCurrentFramePanel().revalidateFrame();
   }

   public Frame getCurrentFrame() {
      return Tool.getCurrentFrame();
   }

   public FramePanel getCurrentFramePanel() {
      return (FramePanel)frameFolder.getComponentAt( picture.getCurrentFrameIndex() );
   }

   public int getFrameCount() {
      return frameFolder.getTabCount();
   }

   public Picture getPicture() {
      return picture;
   }

   private void removeFrame( int index ) {
      frameFolder.removeTabAt( index );
      index--;
      if ( index == -1 ) {
         index = 0;
      }
      picture.setCurrentFrame( index );
      final int frames = getFrameCount();
      for ( int i = 0; i < frames; i++ ) {
         frameFolder.setTitleAt( i, "#" + String.valueOf( i + 1 ) );
      }
      frameChanged();
   }

   private void removeFramePanels() {
      final int frames = frameFolder.getTabCount();
      FramePanel f;
      for ( int i = 0; i < frames; i++ ) {
         f = (FramePanel)frameFolder.getComponentAt( 0 );
         frameFolder.removeTabAt( 0 );
         removeGridListener( f.getLayeredPane() );
      }
   }

   public static void removeGridListener( GridListener aListener ) {
      GRID_LISTENERS.remove( aListener );
   }

   public void setPicture( Picture aPicture ) {
      justUpdating = (aPicture == picture);
      final int currentFrame = frameFolder.getSelectedIndex();
      picture = aPicture;
      setVisible( false );

      removeFramePanels();
      final int count = picture.getFrameCount();
      FramePanel f;
      for ( int i = 0; i < count; i++ ) {
         f = new FramePanel();
         f.setFrame( picture.getFrame( i ) );
         frameFolder.addTab( "#" + String.valueOf( i + 1 ), frameIcon, f );
      }
      if ( justUpdating ) {
         frameFolder.setSelectedIndex( currentFrame );
         justUpdating = false;
      }
      frameChanged();

      setVisible( true );
   }

   public void setShowGrid( boolean f ) {
      if ( f != showGrid ) {
         showGrid = f;
         repaint();
      }
   }

   public boolean showGrid() {
      return (grid > 1) && showGrid;
   }

   public void stateChanged( javax.swing.event.ChangeEvent e ) {
      if ( justUpdating ) {
         return;
      }
      final int frame = frameFolder.getSelectedIndex();
      if ( (frame != -1) && (frame < getPicture().getFrameCount()) ) {
         getPicture().setCurrentFrame( frame );
      }
   }

}