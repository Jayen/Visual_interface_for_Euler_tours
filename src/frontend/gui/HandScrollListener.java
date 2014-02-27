package frontend.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: Jayen
 * Date: 25/02/14, Time: 11:39
 */
public class HandScrollListener extends MouseAdapter {
    private final Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    private final Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private final Point point = new Point();
    private GraphVisualiserPanel graphVisualiserPanel;

    public HandScrollListener(GraphVisualiserPanel graphVisualiserPanel) {
        this.graphVisualiserPanel = graphVisualiserPanel;
    }

    public void mouseDragged(final MouseEvent e) {
        JViewport viewport = (JViewport)e.getSource();
        Point cursorPoint = e.getPoint();
        Point viewportPoint = viewport.getViewPosition();
        viewportPoint.translate(point.x - cursorPoint.x, point.y - cursorPoint.y);
        graphVisualiserPanel.scrollRectToVisible(new Rectangle(viewportPoint, viewport.getSize()));
        point.setLocation(cursorPoint);
    }

    public void mousePressed(MouseEvent e) {
        graphVisualiserPanel.setCursor(handCursor);
        point.setLocation(e.getPoint());
    }

    public void mouseReleased(MouseEvent e) {
        graphVisualiserPanel.setCursor(defaultCursor);
        graphVisualiserPanel.repaint();
    }
}