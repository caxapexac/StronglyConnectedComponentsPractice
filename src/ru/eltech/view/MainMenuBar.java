package ru.eltech.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainMenuBar extends JMenuBar implements ActionListener {
    private MainWindow parent;

    private final JMenu fileMenu = new JMenu("File");
    private final JMenuItem newGraphMenuItem = new JMenuItem("Новый граф", KeyEvent.VK_N);
    private final JMenuItem showExampleMenuItem = new JMenuItem("Загрузить пример графа", KeyEvent.VK_P);
    private final JMenuItem saveMenuItem = new JMenuItem("Сохранить...", KeyEvent.VK_Z);
    private final JMenuItem loadMenuItem = new JMenuItem("Загрузить...", KeyEvent.VK_W);

    private final JMenu editMenu = new JMenu("Edit");
    private final JMenuItem nodesMenuItem = new JMenuItem("nodesMenuItem...", KeyEvent.VK_W);
    private final JMenuItem edgesMenuItem = new JMenuItem("edgesMenuItem...", KeyEvent.VK_K);

    private final JMenu viewMenu = new JMenu("View");
    private final JMenuItem drawGridCheckBoxMenuItem = new JCheckBoxMenuItem("Показать сетку");

    private final JMenu helpMenu = new JMenu("Help");
    private final JMenuItem appMenuItem = new JMenuItem("О программе...", KeyEvent.VK_P);
    private final JMenuItem authorMenuItem = new JMenuItem("Об авторах...", KeyEvent.VK_A);

    public MainMenuBar(MainWindow parent) {
        this.parent = parent;
    }

    private void initializeMenus() {
        newGraphMenuItem.addActionListener(this);
        showExampleMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        loadMenuItem.addActionListener(this);
        nodesMenuItem.addActionListener(this);
        edgesMenuItem.addActionListener(this);
        drawGridCheckBoxMenuItem.addActionListener(this);
        appMenuItem.addActionListener(this);
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

        viewMenu.add(drawGridCheckBoxMenuItem);
        add(viewMenu);

        helpMenu.add(appMenuItem);
        helpMenu.add(authorMenuItem);
        add(helpMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object eSource = e.getSource();
        if (eSource == newGraphMenuItem) {
            //graphPanel.createNewGraph();
        } else if (eSource == showExampleMenuItem) {
            //graphPanel.showExampleGraph();
        } else if (eSource == saveMenuItem) {
            parent.serializeGraph();
        } else if (eSource == loadMenuItem) {
            parent.deserializeGraph();
        } else if (eSource == nodesMenuItem) {
            parent.showNodesList();
        } else if (eSource == edgesMenuItem) {
            parent.showEdgesList();
        } else if (eSource == drawGridCheckBoxMenuItem) {
            //graphPanel.enableGrid(drawGridCheckBoxMenuItem.isSelected());
        } else if (eSource == appMenuItem) {
            //showInstruction();
        } else if (eSource == authorMenuItem) {
            //showAuthorInfo();
        }
    }
}
