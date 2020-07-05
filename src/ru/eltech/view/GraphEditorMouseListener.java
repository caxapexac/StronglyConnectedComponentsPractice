package ru.eltech.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GraphEditorMouseListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        if (e.getClickCount() == 2) {
            if (graphEditor.hasSelected()) graphEditor.destroySelected();
            else graphEditor.createNode(e.getX(), e.getY());
            e.consume();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        graphEditor.endDrag(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        if (e.getButton() == MouseEvent.BUTTON1) {
            graphEditor.select(e.getX(), e.getY());
            graphEditor.startDrag(e.getX(), e.getY());
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            graphEditor.startConnecting(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        if (e.getButton() == MouseEvent.BUTTON1) {
            graphEditor.endDrag(e.getX(), e.getY());
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            graphEditor.endConnecting(e.getX(), e.getY(), true, true);
        }
    }
}
