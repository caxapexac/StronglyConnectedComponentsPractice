package ru.eltech.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GraphEditorMouseMotionListener implements MouseMotionListener {
    @Override
    public void mouseDragged(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        graphEditor.drag(e.getX(), e.getY());
        graphEditor.connecting(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
