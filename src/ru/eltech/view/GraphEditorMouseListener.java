package ru.eltech.view;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Логика нажатия клавиш мыши в {@link GraphEditor}
 */
public final class GraphEditorMouseListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        if (e.getClickCount() == 2) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (graphEditor.hasSelected()) graphEditor.destroySelected();
                else graphEditor.createNode(e.getX(), e.getY());
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            JPopupMenu menu = createPopupMenu(graphEditor, e.getX(), e.getY());
            menu.show(graphEditor, e.getX(), e.getY());
        }
        e.consume();
    }

    private JPopupMenu createPopupMenu(GraphEditor graph, int x, int y) {
        Integer foundNode = graph.findNode(x, y);
        if (foundNode != null) {
            return new GraphPopupMenuNode(graph, foundNode);
        }
        Integer foundEdge = graph.findEdge(x, y);
        if (foundEdge != null) {
            return new GraphPopupMenuEdge(graph, foundEdge);
        }
        return new GraphPopupMenuEmpty(graph);
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
