package ru.eltech.view;

import ru.eltech.logic.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public final class MainWindow extends JFrame {
    public static Logger log = Logger.getLogger(MainWindow.class.getName());

    private JPanel content;
    private JLabel loggerLabel;
    private GraphEditor graphEditor;
    private GraphPlayerToolBar graphPlayerToolBar;
    private JScrollPane scrollPane;
    JScrollPane scrolling;

    private Graph graphOrigin;
    private GraphPlayer graphPlayer;

    private final KosarajuAlgorithm algorithm = new KosarajuAlgorithm();

    private final String AUTOSAVE_FILE = "autosave.graph";

    public MainWindow() {
        super("Strongly Connected Components");
        $$$setupUI$$$();
        initialize();
        pack();
    }

    private void initialize() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setContentPane(content);
        setMinimumSize(new Dimension(1024, 768));
        setResizable(true);
        setLocationRelativeTo(null);
        setJMenuBar(new MainMenuBar(this));
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                System.exit(0);
            }

            @Override
            public void windowClosing(WindowEvent event) {
                int reply = JOptionPane.showConfirmDialog(null, "Сохранить текущий граф в файл " + AUTOSAVE_FILE + "?", "Выход", JOptionPane.YES_NO_CANCEL_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    serializeGraph(new File(AUTOSAVE_FILE), true);
                } else if (reply == JOptionPane.CANCEL_OPTION) {
                    return;
                }
                windowClosed(event);
            }
        });
        graphOrigin = new Graph();
        graphPlayer = new GraphPlayer(graphPlayerToolBar);
        graphPlayerToolBar.setParent(this);
        log.addHandler(new LoggerTextAreaHandler(scrollPane, loggerLabel));
        deserializeGraph(new File(AUTOSAVE_FILE), true);
        scrolling = new JScrollPane(graphEditor,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrolling.setPreferredSize(new Dimension(800, 600));
        scrolling.setAutoscrolls(true);
        content.add(scrolling);
    }

    public GraphEditor getGraphEditor() {
        return graphEditor;
    }

    public GraphPlayer getGraphPlayer() {
        return graphPlayer;
    }

    /**
     * @return актуальная версия графа
     */
    public Graph getGraphOrigin() {
        if (!graphEditor.isReadOnly && graphEditor.isModified) {
            graphOrigin = graphEditor.getGraphCopy();
        }
        return graphOrigin;
    }

    /**
     * Принудительно заменяет текущий граф и его визуализацию
     */
    public void setGraphOrigin(Graph graph) {
        graphPlayer.setState(GraphPlayer.State.Stop);
        graphOrigin = graph;
        graphEditor.setGraphCopy(graph);
        graphEditor.isReadOnly = false;
    }

    // region LIFECYCLE

    public void playerPlaying() {
        //log.info("playerPlaying");
        if (!graphEditor.isReadOnly) {
            graphEditor.isReadOnly = true;
            graphOrigin = graphEditor.getGraphCopy();
            graphPlayer.setFrameList(algorithm.process(new Graph(getGraphOrigin())));
        }
    }

    public void playerPausing() {
        //log.info("playerPausing");
    }

    public void playerStopping() {
        //log.info("playerStopping");
        if (graphEditor.isReadOnly) {
            graphEditor.isReadOnly = false;
            graphEditor.setGraphCopy(graphOrigin);
        }
    }

    public void playerVisualizing(Graph graph) {
        graphEditor.setGraphCopy(graph);
        if (graph.state != null)
            this.log.info(graph.state);
    }

    public void setImmediateReverse(boolean immediateReverse) {
        algorithm.setImmediateReverse(immediateReverse);
        switch (graphPlayer.getState()) {
            case Play:
                log.warning("Попытка изменения анимации во время проигрывания");
                break;
            case Pause:
                log.warning("Изменение анимации на паузе не предусмотрено");
            case Stop:
                graphPlayer.setFrameList(algorithm.process(new Graph(getGraphOrigin())));
                break;
        }
    }

    // endregion

    //region ACTIONS

    public void createNewGraph() {
        setGraphOrigin(new Graph());
    }

    public void loadExampleGraph() {
        Graph graph = new Graph();
        Node n1 = graph.createNode(100, 100);
        Node n2 = graph.createNode(100, 500);
        Node n3 = graph.createNode(500, 100);
        Node n4 = graph.createNode(500, 500);
        Node n5 = graph.createNode(300, 300);
        graph.createEdge(n2, n1);
        graph.createEdge(n1, n3);
        graph.createEdge(n1, n5);
        graph.createEdge(n5, n4);
        graph.createEdge(n5, n2);
        setGraphOrigin(graph);
    }

    public void serializeGraph() {
        JFileChooser fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(" *.graph", "graph");
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(filter);

        int chosenOption = fc.showSaveDialog(this);
        if (chosenOption == JFileChooser.APPROVE_OPTION) {
            serializeGraph(fc.getSelectedFile(), false);
        }
    }

    public void serializeGraph(File file, boolean silently) {
        String fileName = file.getAbsolutePath();
        if (!fileName.endsWith(".graph")) file = new File(fileName + ".graph");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            getGraphOrigin().save(fos);
            if (!silently) JOptionPane.showMessageDialog(null, "Граф сохранён успешно " + file.getAbsolutePath());
        } catch (IOException e) {
            if (!silently)
                JOptionPane.showMessageDialog(this, e.getMessage(), "Файл не найден!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deserializeGraph() {
        JFileChooser fc = new JFileChooser(".");
        fc.setMultiSelectionEnabled(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(" *.graph", "graph");
        fc.addChoosableFileFilter(filter);
        fc.setFileFilter(filter);

        int chosenOption = fc.showOpenDialog(this);
        if (chosenOption == JFileChooser.APPROVE_OPTION) {
            deserializeGraph(fc.getSelectedFile(), false);
        }
    }

    public void deserializeGraph(File file, boolean silently) {
        try (FileInputStream fis = new FileInputStream(file)) {
            setGraphOrigin(new Graph().load(fis));
            if (!silently) JOptionPane.showMessageDialog(null, "Граф загружен успешно " + file.getAbsolutePath());
        } catch (IOException e) {
            if (!silently) e.printStackTrace();
            if (!silently)
                JOptionPane.showMessageDialog(this, e.getMessage(), "Файл не найден!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showNodesList() {
        Graph curGraph = graphPlayer.getFrameList() != null ? graphPlayer.getFrameList().get(graphPlayer.getCurrentFrame()) : getGraphOrigin();
        StringBuilder msg = new StringBuilder();
        for (Node node : curGraph.getNodes()) {
            msg.append(node.getDescription()).append("\n");
        }
        JOptionPane.showMessageDialog(this, msg.toString(), "Список вершин", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showEdgesList() {
        Graph curGraph = graphPlayer.getFrameList() != null ? graphPlayer.getFrameList().get(graphPlayer.getCurrentFrame()) : getGraphOrigin();
        StringBuilder msg = new StringBuilder();
        for (Edge edge : curGraph.getEdges()) {
            msg.append(edge.getDescription()).append("\n");
        }
        JOptionPane.showMessageDialog(this, msg.toString(), "Список ребер", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showInstruction() {
        showHtmlFormattedMessageDialog("/resources/docs/helpMessage.html", "Help", JOptionPane.PLAIN_MESSAGE);
    }

    public void showAuthorsInfo() {
        showHtmlFormattedMessageDialog("/resources/docs/authors.html", "Authors", JOptionPane.PLAIN_MESSAGE);
    }

    public void clearLog() {
        loggerLabel.setText("<html>Log:<br></html>");
    }

    //endregion

    /**
     * @param filename
     * @param title
     * @param messageType
     * @apiNote Shows message dialog, takes message text from a html-encoded file in resources folder
     */
    private void showHtmlFormattedMessageDialog(String filename, String title, int messageType) {
        String formattedText = readResourceFileAsString(filename, "UTF-8");
        JLabel message = new JLabel(formattedText);
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    /**
     * @return file as String
     * @apiNote Helper function to read html resources
     */
    private String readResourceFileAsString(String filename, String charsetName) {
        try (InputStreamReader is = new InputStreamReader(getClass().getResourceAsStream(filename), charsetName)) {
            return new BufferedReader(is).lines().collect(Collectors.joining("\n"));
        } catch (UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(this, "Incorrect encoding", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        content = new JPanel();
        content.setLayout(new BorderLayout(0, 0));
        content.setMinimumSize(new Dimension(622, 400));
        graphEditor = new GraphEditor();
        graphEditor.setMinimumSize(new Dimension(100, 10));
        graphEditor.setPreferredSize(new Dimension(512, 10));
        content.add(graphEditor, BorderLayout.CENTER);
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setMinimumSize(new Dimension(64, 22));
        toolBar1.setPreferredSize(new Dimension(256, 22));
        content.add(toolBar1, BorderLayout.EAST);
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(31);
        toolBar1.add(scrollPane);
        loggerLabel = new JLabel();
        loggerLabel.setText("Log:");
        loggerLabel.setVerticalAlignment(1);
        loggerLabel.setVerticalTextPosition(1);
        scrollPane.setViewportView(loggerLabel);
        graphPlayerToolBar = new GraphPlayerToolBar();
        graphPlayerToolBar.setMaximumSize(new Dimension(10000, 10000));
        graphPlayerToolBar.setMinimumSize(new Dimension(10, 10));
        graphPlayerToolBar.setPreferredSize(new Dimension(1000, 100));
        content.add(graphPlayerToolBar, BorderLayout.NORTH);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return content;
    }

}