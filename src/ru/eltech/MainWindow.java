package ru.eltech;

import javax.swing.*;

public class MainWindow extends JFrame {
    private JPanel content;
    private JSlider slider1;
    private JProgressBar progressBar1;
    private JTextArea Log;
    private GraphEditor graphEditor1;

    public MainWindow() {
        super();
        initialize();
        pack();
    }

    private void initialize() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(content);
        setSize(1024, 760);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);

        JMenuBar menuBar = new JMenuBar();
        // File
        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);
        JMenuItem itemOpen = new JMenuItem("Open");
        menuFile.add(itemOpen);
        //itemOpen.addActionListener((i) -> System.out.println("1"));
        JMenuItem itemSave = new JMenuItem("Save");
        menuFile.add(itemSave);
        // Edit
        JMenu menuEdit = new JMenu("Edit");
        menuBar.add(menuEdit);
        JMenuItem itemSomething = new JMenuItem("Something");
        menuEdit.add(itemSomething);
        // Help
        JMenu menuHelp = new JMenu("Help");
        menuBar.add(menuHelp);
        JMenuItem itemAbout = new JMenuItem("About");
        menuHelp.add(itemAbout);
        //
        setJMenuBar(menuBar);
    }

    private void createUIComponents() {
        graphEditor1 = new GraphEditor();
    }
}