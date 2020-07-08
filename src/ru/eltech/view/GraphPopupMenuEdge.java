package ru.eltech.view;

import javax.swing.*;

/**
 * Контекстное меню при клике ПКМ*2 на {@link ru.eltech.logic.Edge}
 */
public final class GraphPopupMenuEdge extends JPopupMenu {
    public GraphPopupMenuEdge(GraphEditor graphEditor, Integer id) {
        JMenuItem removeEdgeMenuItem = new JMenuItem("Удалить дугу");
        removeEdgeMenuItem.addActionListener((action) -> graphEditor.destroyEdge(id));
        add(removeEdgeMenuItem);
        addSeparator();
        JMenuItem changeEdgeStrokeMenuItem = new JMenuItem("Изменить толщину дуги");
        changeEdgeStrokeMenuItem.addActionListener((action) -> graphEditor.changeEdgeStroke(id));
        add(changeEdgeStrokeMenuItem);
        JMenuItem changeEdgeColorMenuItem = new JMenuItem("Изменить цвет дуги");
        changeEdgeColorMenuItem.addActionListener((action) -> graphEditor.changeEdgeColor(id));
        add(changeEdgeColorMenuItem);
    }
}
