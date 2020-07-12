package ru.eltech.view;

import javax.swing.*;

/**
 * Контекстное меню при клике ПКМ*2 на пустую область внутри {@link GraphEditor}
 */
public final class GraphPopupMenuEmpty extends JPopupMenu {
    private int x;
    private int y;

    public GraphPopupMenuEmpty(GraphEditor graphEditor, int x, int y) {
        this.x = x;
        this.y = y;
        JMenuItem newNodeMenuItem = new JMenuItem("Создать узел");
        newNodeMenuItem.addActionListener((action) -> graphEditor.createNewNode(x, y));
        add(newNodeMenuItem);
        addSeparator();
        JMenuItem clearMenuItem = new JMenuItem("Очистить граф");
        clearMenuItem.addActionListener((action) -> graphEditor.clearGraph());
        add(clearMenuItem);
        JMenuItem scrollDownMenuItem = new JMenuItem("Расширить холст вниз");
        //scrollDownMenuItem.addActionListener((action) -> graphEditor.scrollDown(100)); TODO remove
        add(scrollDownMenuItem);
        JMenuItem scrollRightMenuItem = new JMenuItem("Расширить холст вправо");
        //scrollRightMenuItem.addActionListener((action) -> graphEditor.scrollRight(100)); TODO remove
        add(scrollRightMenuItem);
    }
}
