package ru.eltech.view;

import javax.swing.*;
import java.awt.*;

/**
 * Контекстное меню при клике ПКМ*2 на пустую область внутри {@link GraphEditor}
 */
public class GraphPopupMenuEmpty extends JPopupMenu {
    private int x;
    private int y;

    public GraphPopupMenuEmpty(GraphEditor graphEditor) {
        JMenuItem newNodeMenuItem = new JMenuItem("Создать узел");
        newNodeMenuItem.addActionListener((action) -> graphEditor.createNewNode(x, y));
        add(newNodeMenuItem);
        addSeparator();
        JMenuItem clearMenuItem = new JMenuItem("Очистить граф");
        clearMenuItem.addActionListener((action) -> graphEditor.clearGraph());
        add(clearMenuItem);
    }

    /**
     * Метод перегружен для того, чтобы перехватить и сохранить параметры x, y
     */
    @Override
    public void show(Component invoker, int x, int y) {
        this.x = x;
        this.y = y;
        super.show(invoker, x, y);
    }
}
