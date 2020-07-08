package ru.eltech.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Обёртка над панелью меню в {@link MainWindow}
 */
@SuppressWarnings("FieldCanBeLocal")
public final class MainMenuBar extends JMenuBar implements ActionListener {
    private final MainWindow parent;

    private final JMenu fileMenu = new JMenu("File");
    private final JMenuItem newGraphMenuItem = new JMenuItem("Новый граф");
    private final JMenuItem showExampleMenuItem = new JMenuItem("Загрузить пример графа");
    private final JMenuItem saveMenuItem = new JMenuItem("Сохранить...");
    private final JMenuItem loadMenuItem = new JMenuItem("Загрузить...");
    // TODO

    private final JMenu editMenu = new JMenu("Edit");
    private final JMenuItem nodesMenuItem = new JMenuItem("nodesMenuItem...");
    private final JMenuItem edgesMenuItem = new JMenuItem("edgesMenuItem...");
    // TODO

    private final JMenu viewMenu = new JMenu("View");
    private final JMenuItem startMenuItem = new JMenuItem("Запустить алгоритм");
    // TODO

    private final JMenu helpMenu = new JMenu("Help");
    private final JMenuItem appMenuItem = new JMenuItem("О программе...");
    private final JMenuItem authorMenuItem = new JMenuItem("Об авторах...");

    public MainMenuBar(MainWindow parent) {
        this.parent = parent;
        newGraphMenuItem.addActionListener(this);
        newGraphMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        showExampleMenuItem.addActionListener(this);
        showExampleMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        saveMenuItem.addActionListener(this);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        loadMenuItem.addActionListener(this);
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        nodesMenuItem.addActionListener(this);
        edgesMenuItem.addActionListener(this);
        startMenuItem.addActionListener(this);
        appMenuItem.addActionListener(this);
        appMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        authorMenuItem.addActionListener(this);

        fileMenu.add(newGraphMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(showExampleMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        add(fileMenu);

        editMenu.add(nodesMenuItem);
        editMenu.add(edgesMenuItem);
        add(editMenu);

        viewMenu.add(startMenuItem);
        add(viewMenu);

        helpMenu.add(appMenuItem);
        helpMenu.add(authorMenuItem);
        add(helpMenu);
    }

    /**
     * Проброс событий меню в {@link MainWindow}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object eSource = e.getSource();
        if (eSource == newGraphMenuItem) {
            parent.createNewGraph();
        } else if (eSource == showExampleMenuItem) {
            parent.loadExampleGraph();
        } else if (eSource == saveMenuItem) {
            parent.serializeGraph();
        } else if (eSource == loadMenuItem) {
            parent.deserializeGraph();
        } else if (eSource == nodesMenuItem) {
            parent.showNodesList();
        } else if (eSource == edgesMenuItem) {
            parent.showEdgesList();
        } else if (eSource == startMenuItem) {
            parent.startVisualizing();
        } else if (eSource == appMenuItem) {
            parent.showInstruction();
        } else if (eSource == authorMenuItem) {
            parent.showAuthorsInfo();
        }
    }
}
