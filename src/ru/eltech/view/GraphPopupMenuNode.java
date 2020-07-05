package ru.eltech.view;

import javax.swing.*;

/**
 * Контектстное меню при клике на ноду
 */
public class GraphPopupMenuNode extends JPopupMenu {
    public GraphPopupMenuNode (GraphEditor graphEditor, Integer id) {
        JMenuItem removeNodeMenuItem = new JMenuItem("Удалить узел");
        removeNodeMenuItem.addActionListener((action) -> graphEditor.removeNode(id));
        add(removeNodeMenuItem);
        addSeparator();
        JMenuItem addEdgeMenuItem = new JMenuItem("Создать дугу");
        addEdgeMenuItem.addActionListener((action) -> graphEditor.initializeAddEdge(id));
        add(addEdgeMenuItem);
        addSeparator();
        JMenuItem changeNodeRadiusMenuItem = new JMenuItem("Изменить радиус узла");
        changeNodeRadiusMenuItem.addActionListener((action) -> graphEditor.changeNodeRadius(id));
        add(changeNodeRadiusMenuItem);
        JMenuItem changeNodeColorMenuItem = new JMenuItem("Изменить цвет узла");
        changeNodeColorMenuItem.addActionListener((action) -> graphEditor.changeNodeColor(id));
        add(changeNodeColorMenuItem);
        JMenuItem changeTextMenuItem = new JMenuItem("Изменить имя узла");
        changeTextMenuItem.addActionListener((action) -> graphEditor.changeNodeText(id));
        add(changeTextMenuItem);
    }
}