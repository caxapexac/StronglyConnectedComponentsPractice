package ru.eltech.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Логика нажатия клавиш мыши в {@link GraphEditor}
 */
public final class GraphEditorMouseListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        Point pos = graphEditor.canvasToGraphSpace(e.getX(), e.getY());
        if (e.getClickCount() == 2) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (graphEditor.hasSelected()) graphEditor.destroySelected();
                else graphEditor.createNode(pos.x, pos.y);
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3 && !graphEditor.isReadOnly) {
            JPopupMenu menu = createPopupMenu(graphEditor, pos.x, pos.y);
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
        return new GraphPopupMenuEmpty(graph, x, y);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        Point pos = graphEditor.canvasToGraphSpace(e.getX(), e.getY());
        graphEditor.endDrag(e.getX(), e.getY(), pos.x, pos.y);
        if (e.getClickCount() == 2) {
            //graphEditor.scrollAreaAroundPoint(pos.x, pos.y, 100);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        Point pos = graphEditor.canvasToGraphSpace(e.getX(), e.getY());
        if (e.getButton() == MouseEvent.BUTTON1) {
            graphEditor.select(pos.x, pos.y);
            graphEditor.startDrag(e.getX(), e.getY(), pos.x, pos.y);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            graphEditor.startConnecting(pos.x, pos.y);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        Point pos = graphEditor.canvasToGraphSpace(e.getX(), e.getY());
        if (e.getButton() == MouseEvent.BUTTON1) {
            graphEditor.endDrag(e.getX(), e.getY(), pos.x, pos.y);
            //если создали новую ноду
            if (e.getClickCount() == 2) {
                //graphEditor.scrollAreaAroundPoint(e.getX(), e.getY(), 100);
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            graphEditor.endConnecting(pos.x, pos.y, true, true);
        }
    }


}