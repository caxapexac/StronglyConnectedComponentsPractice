package ru.eltech.view;

import ru.eltech.logic.Algorithm;
import ru.eltech.logic.Graph;
import ru.eltech.logic.GraphPlayer;
import ru.eltech.logic.KosarajuAlgorithm;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.*;

public class MainWindow extends JFrame {
    private JPanel content;
    private JSlider slider1;
    private JProgressBar progressBar1;
    private JTextArea Log;
    private GraphEditor graphEditor;

    private final Graph graph = new Graph();
    private final GraphPlayer graphPlayer = new GraphPlayer();
    private final Algorithm algorithm = new KosarajuAlgorithm();


    public MainWindow() {
        super("Strongly Connected Components");
        initialize();
        pack();
    }

    private void initialize() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(content);
        setSize(1024, 760);
        setLocationRelativeTo(null);
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //setUndecorated(true);
        //UIManager.put("OptionPane.messageFont", new Font("Monospaced", Font.BOLD, 12));
        //graphPanel.deserializeGraph(AUTOSAVE_FILE); TODO auto load on start
        setJMenuBar(new MainMenuBar(this));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                //graphPanel.serializeGraph(AUTOSAVE_FILE); TODO save
            }

            @Override
            public void windowClosing(WindowEvent event) {
                windowClosed(event);
            }
        });
    }

    //region ACTIONS

    public void createNewGraph() {
        // TODO
    }

    public void loadExampleGraph() {
        // TODO
    }

    public void serializeGraph() {
        if (graphEditor.getGraph(graph) == null) return;
        JFileChooser fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(" *.graph", "graph");
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(filter);

        int choosenOption = fc.showSaveDialog(this);
        if (choosenOption == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            String fileName = selectedFile.getAbsolutePath();
            if (!fileName.endsWith(".graph")) selectedFile = new File(fileName + ".graph");
            try (FileOutputStream fos = new FileOutputStream(selectedFile)) {
                graphEditor.getGraph(graph).save(fos);
                JOptionPane.showMessageDialog(null, "Граф сохранён успешно " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Файл не найден!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void deserializeGraph() {
        JFileChooser fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(" *.graph", "graph");
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(filter);

        int choosenOption = fc.showOpenDialog(this);
        if (choosenOption == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(selectedFile)){
                graphEditor.getGraph(graph).load(fis);
                JOptionPane.showMessageDialog(null, "Граф загружен успешно " + selectedFile.getAbsolutePath());
                repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Файл не найден!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showNodesList() {
        // TODO
        //String nodesList = graphEditor.getGraph().getListOfNodes();
        //JOptionPane.showMessageDialog(this, nodesList, "text", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showEdgesList() {
        // TODO
    }

    public void startAlgorithm() {
        // TODO
    }

    public void showInstruction() {
        // TODO
    }

    public void showAuthorsInfo() {
        // TODO
    }

    //endregion
}