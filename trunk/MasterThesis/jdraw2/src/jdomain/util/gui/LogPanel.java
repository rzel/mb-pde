package jdomain.util.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;

import jdomain.util.Log;
import jdomain.util.LogListener;
import jdomain.util.ResourceLoader;

/**
 * @author J-Domain [Michaela Behling]
 */
public class LogPanel extends JPanel implements LogListener {

   private static final long serialVersionUID = 0L;
   
   private static final int TYPE_DEBUG = 0;
   private static final int TYPE_INFO = 1;
   private static final int TYPE_WARNING = 2;
   private static final int TYPE_ERROR = 3;
   private static final int TYPE_EXCEPTION = 4;

   private Font logFont = new Font( "SansSerif", Font.PLAIN, 10 );

   private final JList logList = new JList( new DefaultListModel() );

   public LogPanel() {
      super( new BorderLayout( 0, 0 ) );
      setBackground( Color.WHITE );
      logList.setVisibleRowCount( 8 );
      logList.setCellRenderer( new CellRenderer() );
      add( new JScrollPane( logList ), BorderLayout.CENTER );

      logList.addMouseListener( new MouseAdapter() {
         public void mousePressed( MouseEvent e ) {
            if ( e.getButton() == MouseEvent.BUTTON3 ) {
               if ( logList.getModel().getSize() > 0 ) {
                  JPopupMenu menu = new JPopupMenu();
                  String title = "Clear Messages";
                  if ( Locale.getDefault().toString().equals("de") ) {
                     title="Nachrichten Löschen";
                  }
                  menu.add( new AbstractAction( title, ResourceLoader
                        .getImage( "jdomain/util/images/eraser.gif" ) ) {
                     /** */
                           private static final long serialVersionUID = 1L;

                     public void actionPerformed( ActionEvent e2 ) {
                        clear();
                     }
                  } );
                  menu.show( logList, e.getX(), e.getY() );
               }
            }

         }
      } );
      Log.addLogListener( this );
   }

   public void close() {
   }

   public void clear() {
      logList.setModel( new DefaultListModel() );
   }

   public void addSelectionListener( ListSelectionListener l ) {
      logList.addListSelectionListener( l );
   }

   public void removeSelectionListener( ListSelectionListener l ) {
      logList.removeListSelectionListener( l );
   }

   private void addElement( final LogMessage m ) {
      Runnable runner = new Runnable() {
         public void run() {

            DefaultListModel model = (DefaultListModel) logList.getModel();
            model.addElement( m );
            logList.ensureIndexIsVisible( model.size() - 1 );
         }
      };
      SwingUtilities.invokeLater( runner );
   }

   public void debug( String message ) {
      addElement( new LogMessage( TYPE_DEBUG, message ) );
   }

   public void error( String message ) {
      addElement( new LogMessage( TYPE_ERROR, message ) );
   }

   public void exception( Throwable e ) {
      addElement( new LogMessage( TYPE_EXCEPTION, e.toString() ) );
   }

   public void info( String message ) {
      addElement( new LogMessage( TYPE_INFO, message ) );
   }

   public void warning( String message, Object o ) {
      addElement( new LogMessage( TYPE_WARNING, message + " [" + o + "]", o ) );
   }

   public void warning( String message ) {
      addElement( new LogMessage( TYPE_WARNING, message ) );
   }

   public Object getSelection() {
      LogMessage msg = (LogMessage) logList.getSelectedValue();
      if ( msg == null ) {
         return null;
      }
      return msg.obj;
   }


   private class LogMessage {
      public final int type;
      public final String message;
      public final Object obj;

      public LogMessage( int type, String message ) {
         this( type, message, null );
      }

      public LogMessage( int type, String message, Object obj ) {
         this.type = type;
         this.message = message;
         this.obj = obj;
      }

   }


   private final class CellRenderer extends JLabel implements ListCellRenderer {

      /** */
      private static final long serialVersionUID = 1L;

      public CellRenderer() {
         this.setFont( logFont );
         this.setBorder( new EmptyBorder( 0, 4, 0, 4 ) );
         this.setIconTextGap(8);
      }
      
      public Dimension getPreferredSize() {
         Dimension dim = super.getPreferredSize();
         dim.height = 18;
         return dim;
      }

      public Component getListCellRendererComponent( JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus ) {

         LogMessage msg = (LogMessage) value;
         setText( msg.message );

         if ( isSelected ) {
            setOpaque( true );
            setForeground( list.getSelectionForeground() );
            setBackground( list.getSelectionBackground() );
         }
         else {
            setOpaque( false );

            switch ( msg.type ) {
               case TYPE_DEBUG:
                  this.setForeground( Color.GRAY );
                  this.setIcon( null );
                  break;
               case TYPE_INFO:
                  this.setForeground( Color.BLUE );
                  this.setIcon( ResourceLoader
                        .getImage( "jdomain/util/images/info_small.png" ) );
                  break;
               case TYPE_EXCEPTION:
               case TYPE_ERROR:
                  this.setForeground( Color.RED );
                  this.setIcon( ResourceLoader
                        .getImage( "jdomain/util/images/error_small.png" ) );
                  break;
               case TYPE_WARNING:
                  this.setForeground( Color.MAGENTA );
                  this.setIcon( ResourceLoader
                        .getImage( "jdomain/util/images/warning_small.png" ) );
                  break;
            }
            if ( msg.obj != null ) {
               this.setIcon( ResourceLoader
                     .getImage( "jdomain/util/images/object_small.png" ) );
            }
         }

         return this;
      }
   }

}