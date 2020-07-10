package ru.eltech.view;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Логика движения мыши в {@link GraphEditor}
 */
public final class GraphEditorMouseMotionListener implements MouseMotionListener {
    @Override
    public void mouseDragged(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        Point pos = graphEditor.canvasToGraphSpace(e.getX(), e.getY());
        graphEditor.drag(e.getX(), e.getY(), pos.x, pos.y);
        graphEditor.connecting(e.getX(), e.getY());
        setMouseCursor(graphEditor, e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        setMouseCursor(graphEditor, e);
    }

    private void setMouseCursor(GraphEditor graph, MouseEvent e) {
        Point pos = graph.canvasToGraphSpace(e.getX(), e.getY());

        if (graph.isReadOnly) graph.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        else if (graph.findNode(pos.x, pos.y) != null) graph.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else if (graph.findEdge(pos.x, pos.y) != null) graph.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        else if (graph.isMoving()) graph.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        else if (graph.isConnecting()) graph.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        else if (e.getButton() == MouseEvent.BUTTON1) graph.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        else graph.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }
}
