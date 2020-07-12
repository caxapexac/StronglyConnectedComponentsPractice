package ru.eltech.view;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class GraphEditorMouseWheelListener implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        Point pos = graphEditor.canvasToGraphSpace(e.getX(), e.getY());
        graphEditor.zoom(pos.x, pos.y, -e.getWheelRotation());
    }
}
