package jdomain.jdraw.action;

import jdomain.jdraw.data.Frame;
import jdomain.jdraw.data.Palette;
import jdomain.jdraw.gui.ColourEditor;
import jdomain.jdraw.gui.Tool;

/*
 * EditColourAction.java - created on 29.10.2003
 * 
 * @author Michaela Behling
 */

public final class EditColourAction extends BlockingDrawAction {

   /** */
   private static final long serialVersionUID = 1L;
   private ColourEditor editor;
   private int colIndex;

   // private Color newColour;

   protected EditColourAction() {
      super( "Edit Colour..." );
      setToolTipText( "Edits the current foreground colour" );
   }

   public boolean prepareAction() {
      editor = ColourEditor.INSTANCE;
      Frame frame = Tool.getCurrentFrame();
      Palette p = frame.getPalette();
      colIndex = Tool.getPicture().getForeground();

      editor.setColour( p.getColour( colIndex ).getColour() );
      editor.open();
      return editor.getResult() == ColourEditor.APPROVE_ACTION;
   }

   public void startAction() {
      Tool.getCurrentFrame().getPalette().setColour( colIndex, editor.getColour() );
   }

   public void finishAction() {
   }
}
