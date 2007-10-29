package jdomain.jdraw.gui;

import jdomain.jdraw.data.Clip;

import java.awt.Cursor;
import java.awt.Point;

/*
 * Created on 29-Oct-2003
 * 
 * @author michaela
 */

public final class ClipTool extends RectangularSelectionTool {

    private final ClipPanel clipPanel = new ClipPanel( true );

    protected void processSelection( int mouseButton ) {
        if ( isValidSelection() ) {
            Clip clip = Tool.getCurrentFrame().createClip( upperLeft.x,
                    upperLeft.y, width, height );
            clipPanel.setClip( upperLeft.x, upperLeft.y, clip );
        }
    }

    public void pressed( int button, Point p, int modifier ) {
        if ( button == LEFT_BUTTON ) {
            clipPanel.deactivate();
            setSwallowKeys( true );
            if ( isDragged() ) {
                lastAction = DRAGGED;
                if ( p != null ) {
                    drawRubberBand(); // löschen
                    current = p;
                    drawRubberBand();
                }
            }
            else {
                lastAction = PRESSED;
                start = p;
                current = null;
            }
        }
    }

    public void activate() {
        if ( !isActive ) {
            super.activate();
            MouseHandler.INSTANCE.addClip( clipPanel );
            FolderPanel.addGridListener( clipPanel );
        }
    }

    public void deactivate() {
        if ( isActive ) {
            super.deactivate();
            clipPanel.deactivate();
            MouseHandler.INSTANCE.deleteClip( clipPanel );
            MainFrame.INSTANCE.setCursor( Cursor.getDefaultCursor() );
            clipPanel.deactivate();
            FolderPanel.removeGridListener( clipPanel );
        }
    }

}