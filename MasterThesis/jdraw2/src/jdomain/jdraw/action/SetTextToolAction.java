package jdomain.jdraw.action;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JCheckBox;

import jdomain.jdraw.data.Palette;
import jdomain.jdraw.data.Picture;
import jdomain.jdraw.gui.MainFrame;
import jdomain.jdraw.gui.TextFontDialog;
import jdomain.jdraw.gui.TextTool;
import jdomain.jdraw.gui.Tool;
import jdomain.jdraw.gui.ToolPanel;

/*
 * SetTextToolAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class SetTextToolAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private Font font;
   private String text;
   private TextFontDialog dialog;

   private final JCheckBox gradientBox = new JCheckBox( "Gradient Fill" );
   private final JCheckBox outlineBox = new JCheckBox( "Outline" );
   private final JCheckBox transBox = new JCheckBox( "Transparent Background" );

   protected SetTextToolAction() {
      super( "Text Tool", "text_tool.png" );
      setToolTipText( "Offers text functionality" );
   }

   private TextFontDialog getDialog() {
      if ( dialog == null ) {
         JCheckBox[] boxes = new JCheckBox[3];
         boxes[0] = gradientBox;
         boxes[1] = outlineBox;
         boxes[2] = transBox;
         dialog = new TextFontDialog( MainFrame.INSTANCE, "Font Dialog", boxes );
      }
      Picture pic = Tool.getPicture();
      gradientBox.setSelected( Tool.isGradientFillOn() );
      transBox.setSelected( pic.getTransparent() == pic.getBackground() );
      return dialog;
   }

   public boolean prepareAction() {
      font = null;
      ToolPanel.INSTANCE.setCurrentTool( TextTool.INSTANCE, DrawAction.getAction( SetTextToolAction.class ) );
      TextFontDialog d = getDialog();

      ToggleAntialiasAction a = (ToggleAntialiasAction)DrawAction.getAction( ToggleAntialiasAction.class );
      Palette pal = Tool.getCurrentPalette();
      Picture pic = Tool.getPicture();
      dialog.setAntialiased( a.antialiasOn() );
      dialog.setFontForeground( pal.getColour( pic.getForeground() ).getColour() );
      dialog.setFontBackground( pal.getColour( pic.getBackground() ).getColour() );
      d.open();

      // if ( d.getResult() == FontDialog.APPROVE ) {
      // a.setAntialias( d.isAntialiased() );
      // }
      // font = d.getFont();
      // text = d.getText();
      // return (font != null) && (text != null) && (text.length() > 0);
      return false;
   }

   public void apply() {
      TextFontDialog d = getDialog();
      ToggleAntialiasAction a = (ToggleAntialiasAction)DrawAction.getAction( ToggleAntialiasAction.class );
      a.setAntialias( d.isAntialiased() );
      font = d.getFont();
      text = d.getText();
      startAction();
   }

   public void startAction() {
      if ( gradientBox.isSelected() != Tool.isGradientFillOn() ) {
         DrawAction.getAction( ToggleGradientFillAction.class ).actionPerformed();
         ToolPanel.INSTANCE.selectGradientFill( gradientBox.isSelected() );
      }
      Picture pic = Tool.getPicture();
      if ( transBox.isSelected() ) {
         Palette pal = Tool.getCurrentPalette();
         int trans = pic.getTransparent();
         if ( (trans == -1) ) {
            // neue transparente farbe erzeugen
            pal.addColour( Color.black );
            pic.setTransparent( pal.size() - 1 );
            pic.setBackground( pal.size() - 1 );
         }
         else {
            int back = pic.getBackground();
            if ( back != trans ) {
               pic.setBackground( trans );
            }
         }
      }
      TextTool.INSTANCE.process( font, text, outlineBox.isSelected() );
   }

   public void finishAction() {

   }
}