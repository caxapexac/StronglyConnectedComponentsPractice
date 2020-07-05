package ru.eltech.view;

import javax.swing.*;
import java.awt.*;

/**
 * Контектстное меню при клике на пустую область
 */
public class GraphPopupMenuEmpty extends JPopupMenu {
    private int x;
    private int y;

    public GraphPopupMenuEmpty (GraphEditor graphEditor) {
        JMenuItem newNodeMenuItem = new JMenuItem("Создать узел");
        newNodeMenuItem.addActionListener((action) -> graphEditor.createNewNode(x, y));
        add(newNodeMenuItem);
    }

    @Override
    public void show(Component invoker, int x, int y) {
        this.x = x;
        this.y = y;
        super.show(invoker, x, y);
    }
}
