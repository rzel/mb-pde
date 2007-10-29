package jdomain.jdraw.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jdomain.jdraw.Settings;
import jdomain.jdraw.data.Palette;
import jdomain.jdraw.gui.FillTool.Tolerance;
import jdomain.util.Util;
import jdomain.util.gui.LinesBorder;
import jdomain.util.gui.WidgetFactory;

/*
 * SettingsDialog - created on 15.12.2005
 * 
 * @author Michaela Behling
 */

public class SettingsDialog extends DrawDialog implements DocumentListener, ChangeListener {

   /** */
   private static final long serialVersionUID = 1L;
   private final JSlider qualitySlider = createSlider();
   private final JTextField qualityField = new JTextField( 4 );
   private boolean qualitySliderChanged;
   private boolean qualityFieldChanged;

   private final JTextField redField = new JTextField( 6 );
   private final JTextField greenField = new JTextField( 6 );
   private final JTextField blueField = new JTextField( 6 );
   private final JTextField alphaField = new JTextField( 6 );

   public SettingsDialog() {
      super( "Settings" );
      setUndecorated( false );
      WidgetFactory.addFocusAdapter( qualityField );

      setDefaultBorder();
      setModal( true );
      JPanel p = new JPanel( new GridBagLayout() );
      GridBagConstraints gc = new GridBagConstraints();

      initComponents();

      Border sepBorder = new CompoundBorder( new EmptyBorder( 6, 0, 2, 0 ), new LinesBorder( true, false,
            false, false ) );
      JPanel sepPanel;

      gc.gridx = 0;
      gc.gridy = 0;
      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.insets = new Insets( 0, 0, 2, 6 );

      // jpeg quality

      gc.gridwidth = 1;
      JLabel l = new JLabel( "<html><b>JPEG Quality&nbsp;<b></html>" );
      p.add( l, gc );

      gc.gridx++;
      p.add( qualitySlider, gc );
      gc.gridx++;
      p.add( qualityField, gc );

      gc.gridx = 0;
      gc.gridy++;
      gc.gridwidth = 3;
      sepPanel = new JPanel();
      sepPanel.setBorder( sepBorder );
      p.add( sepPanel, gc );

      // fill tolerance
      gc.gridx = 0;
      gc.gridy++;

      JLabel label = new JLabel( "<html><b>Fill Tolerance</b></html>" );
      // label.setBorder(new EmptyBorder(0, 0, 10, 0));
      p.add( label, gc );

      gc.gridx++;
      gc.gridwidth = 1;
      label = new JLabel( "<html><small>(0=none, 255=highest)</small>&nbsp;</html>", JLabel.RIGHT );
      p.add( label, gc );

      gc.gridwidth = 1;
      gc.gridx = 1;
      gc.gridy++;
      label = new JLabel( "<html>Red Tolerance:&nbsp;</html>", JLabel.RIGHT );
      p.add( label, gc );

      gc.gridx++;
      p.add( redField, gc );

      gc.gridx = 1;
      gc.gridy++;
      label = new JLabel( "<html>Green Tolerance:&nbsp;</html>", JLabel.RIGHT );
      p.add( label, gc );

      gc.gridx++;
      p.add( greenField, gc );

      gc.gridx = 1;
      gc.gridy++;
      label = new JLabel( "<html>Blue Tolerance:&nbsp;</html>", JLabel.RIGHT );
      p.add( label, gc );

      gc.gridx++;
      p.add( blueField, gc );

      gc.gridx = 1;
      gc.gridy++;
      label = new JLabel( "<html>Alpha Tolerance:&nbsp;</html>", JLabel.RIGHT );
      p.add( label, gc );

      gc.gridx++;
      p.add( alphaField, gc );

      gc.gridx = 0;
      gc.gridy++;
      gc.gridwidth = 3;
      sepPanel = new JPanel();
      sepPanel.setBorder( sepBorder );
      p.add( sepPanel, gc );

      main.add( p, BorderLayout.CENTER );

      addRightButton( getApproveButton() );
      getRootPane().setDefaultButton( getApproveButton() );

      addRightButton( getCancelButton() );
      addButtonPanel();

      setFirstFocusComponent( qualityField );
   }

   private void initComponents() {
      // jpeg quality
      qualitySlider.setValue( (int)(Settings.INSTANCE.getJPEGQuality() * 100) );
      qualityField.setText( String.valueOf( qualitySlider.getValue() ) );
      qualitySlider.addChangeListener( this );
      qualityField.getDocument().addDocumentListener( this );

      // fill tolerance
      Tolerance t = FillTool.INSTANCE.getTolerance();

      redField.addFocusListener( WidgetFactory.TEXTFIELD_FOCUS_ADAPTER );
      redField.setText( String.valueOf( t.redDiff ) );
      redField.getDocument().addDocumentListener( this );

      greenField.addFocusListener( WidgetFactory.TEXTFIELD_FOCUS_ADAPTER );
      greenField.setText( String.valueOf( t.greenDiff ) );
      greenField.getDocument().addDocumentListener( this );

      blueField.addFocusListener( WidgetFactory.TEXTFIELD_FOCUS_ADAPTER );
      blueField.setText( String.valueOf( t.blueDiff ) );
      blueField.getDocument().addDocumentListener( this );

      alphaField.addFocusListener( WidgetFactory.TEXTFIELD_FOCUS_ADAPTER );
      alphaField.setText( String.valueOf( t.alphaDiff ) );
      alphaField.getDocument().addDocumentListener( this );
   }

   public float getQuality() {
      float quality = qualitySlider.getValue() / 100f;
      return quality;
   }

   public Tolerance getTolerance() {
      int r = Util.asInt( redField.getText().trim() );
      int g = Util.asInt( greenField.getText().trim() );
      int b = Util.asInt( blueField.getText().trim() );
      int a = Util.asInt( alphaField.getText().trim() );
      return new Tolerance( r, g, b, a );
   }

   private JLabel createLabel( String text, int value ) {
      JLabel label = new JLabel();
      label.setFont( MainFrame.TINY_FONT );
      if ( text == null ) {
         label.setText( String.valueOf( value ) );
      }
      else {
         label.setText( text );
      }
      return label;
   }

   private JSlider createSlider() {
      JSlider slider = new JSlider( JSlider.HORIZONTAL, 0, 100, 100 );

      Hashtable map = new Hashtable();
      slider.setMajorTickSpacing( 10 );
      slider.setMinorTickSpacing( 5 );
      slider.setPaintTicks( true );
      map.put( new Integer( 0 ), createLabel( "Lowest", 0 ) );
      map.put( new Integer( 50 ), createLabel( "Medium", 50 ) );
      map.put( new Integer( 100 ), createLabel( "Highest", 100 ) );
      slider.setLabelTable( map );
      slider.setPaintLabels( true );
      return slider;
   }

   private void checkInput() {
      boolean noErrors;
      String p = qualityField.getText().trim();
      if ( !Util.isNumber( p ) ) {
         noErrors = false;
      }
      else {
         noErrors = (Util.isIn( Util.asInt( p ), 0, 100 ));
         if ( !qualitySliderChanged ) {
            if ( getApproveButton().isEnabled() ) {
               qualityFieldChanged = true;
               qualitySlider.setValue( Util.asInt( p ) );
               qualityFieldChanged = false;
            }
         }
      }

      String[] f = new String[4];
      f[0] = redField.getText().trim();
      f[1] = greenField.getText().trim();
      f[2] = blueField.getText().trim();
      f[3] = alphaField.getText().trim();

      for ( int i = 0; i < 4; i++ ) {
         if ( (!Util.isNumber( f[i] )) || (!Util.isIn( Util.asInt( f[i] ), 0, Palette.MAX_RGB_VALUE )) ) {
            noErrors = false;
            break;
         }
      }
      getApproveButton().setEnabled( noErrors );
   }

   public void changedUpdate( DocumentEvent e ) {
      checkInput();
   }

   public void insertUpdate( DocumentEvent e ) {
      checkInput();
   }

   public void removeUpdate( DocumentEvent e ) {
      checkInput();
   }

   public void stateChanged( ChangeEvent e ) {
      if ( qualityFieldChanged ) {
         return;
      }
      qualitySliderChanged = true;
      qualityField.setText( String.valueOf( qualitySlider.getValue() ) );
      qualitySliderChanged = false;
   }

}
