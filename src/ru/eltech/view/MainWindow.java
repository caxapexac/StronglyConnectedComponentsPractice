package ru.eltech.view;

import ru.eltech.logic.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public final class MainWindow extends JFrame implements ActionListener, ChangeListener {
    public static Logger log = Logger.getLogger(MainWindow.class.getName());

    private JPanel content;
    private JTextPane loggerTextPane;
    private GraphEditor graphEditor;
    private JButton toolBarAutoButton;
    private JButton toolBarStartButton;
    private JButton toolBarStepBackButton;
    private JButton toolBarStepForwardButton;
    private JButton toolBarPauseButton;
    private JButton toolBarStopButton;
    private JSlider toolBarSpeedSlider;
    private JProgressBar bottomProgressBar;

    private Graph graphOrigin = new Graph();
    private final GraphPlayer graphPlayer = new GraphPlayer(new GraphPlayerListener(this));
    private final Algorithm algorithm = new KosarajuAlgorithm();

    private final String AUTOSAVE_FILE = "autosave.graph";


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
        setJMenuBar(new MainMenuBar(this));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                serializeGraph(new File(AUTOSAVE_FILE), true);
                System.exit(0);
            }

            @Override
            public void windowClosing(WindowEvent event) {
                windowClosed(event);
            }
        });
        toolBarAutoButton.addActionListener(this);
        toolBarStartButton.addActionListener(this);
        toolBarStepBackButton.addActionListener(this);
        toolBarStepForwardButton.addActionListener(this);
        toolBarPauseButton.addActionListener(this);
        toolBarStopButton.addActionListener(this);
        toolBarSpeedSlider.addChangeListener(this);
        log.addHandler(new LoggerTextAreaHandler(loggerTextPane));
        deserializeGraph(new File(AUTOSAVE_FILE), true);
    }

    /**
     * Восстановление оригинального графа при завершении анимации
     */
    private void copyOriginToEditor() {
        graphEditor.setGraphCopy(graphOrigin);
        graphEditor.isReadOnly = false;
        repaint();
    }

    /**
     * Сохранение оригинального графа при старте анимации
     */
    private void copyEditorToOrigin() {
        graphOrigin = graphEditor.getGraphCopy();
        graphEditor.isReadOnly = true;
    }

    //region ACTIONS

    public void createNewGraph() {
        graphOrigin = new Graph();
        copyOriginToEditor();
    }

    public void loadExampleGraph() {
        graphOrigin = new Graph();
        Node n1 = graphOrigin.createNode(100, 100);
        Node n2 = graphOrigin.createNode(100, 500);
        Node n3 = graphOrigin.createNode(500, 100);
        Node n4 = graphOrigin.createNode(500, 500);
        Node n5 = graphOrigin.createNode(300, 300);
        graphOrigin.createEdge(n2, n1);
        graphOrigin.createEdge(n1, n3);
        graphOrigin.createEdge(n1, n5);
        graphOrigin.createEdge(n5, n4);
        graphOrigin.createEdge(n5, n2);
        copyOriginToEditor();
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
            graphOrigin.save(fos);
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
            graphOrigin = new Graph().load(fis);
            copyOriginToEditor();
            if (!silently) JOptionPane.showMessageDialog(null, "Граф загружен успешно " + file.getAbsolutePath());
        } catch (IOException e) {
            if (!silently) e.printStackTrace();
            if (!silently)
                JOptionPane.showMessageDialog(this, e.getMessage(), "Файл не найден!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showNodesList() {
        // TODO
        //String nodesList = origin.getListOfNodes();
        //JOptionPane.showMessageDialog(this, nodesList, "text", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showEdgesList() {
        // TODO
    }

    public void startVisualizing() {
        log.info("startVisualizing");
        copyEditorToOrigin();
        graphPlayer.setFrameList(algorithm.process(new Graph(graphOrigin)));
    }

    public void stepVisualizing(Graph graph) {
        //log.info("stepVisualizing");
        if (!graphEditor.isReadOnly) startVisualizing();
        graphEditor.setGraphCopy(graph);
    }

    public void stopVisualizing() {
        log.info("stopVisualizing");
        copyOriginToEditor();
    }

    public void showInstruction() {
        showHtmlFormattedMessageDialog("/resources/docs/helpMessage.html", "Help", JOptionPane.PLAIN_MESSAGE);
    }

    public void showAuthorsInfo() {
        showHtmlFormattedMessageDialog("/resources/docs/authors.html", "Authors", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object eSource = e.getSource();
        if (eSource == toolBarAutoButton) {
            startVisualizing();
        } else if (eSource == toolBarStartButton) {
            if (graphPlayer.getState() == GraphPlayer.State.Empty) startVisualizing();
            graphPlayer.setState(GraphPlayer.State.Started);
        } else if (eSource == toolBarStepBackButton) {
            graphPlayer.stepBackward();
        } else if (eSource == toolBarStepForwardButton) {
            graphPlayer.stepForward();
        } else if (eSource == toolBarPauseButton) {
            graphPlayer.setState(GraphPlayer.State.Paused);
        } else if (eSource == toolBarStopButton) {
            graphPlayer.setState(GraphPlayer.State.Stoped);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider) e.getSource();
        if (slider == toolBarSpeedSlider) {
            int speed = slider.getValue();
            graphPlayer.setDelay(speed);
            log.info("speed changed to " + speed);
        }
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
     * @param filename
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
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
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setMinimumSize(new Dimension(613, 64));
        toolBar1.setOrientation(1);
        toolBar1.setPreferredSize(new Dimension(777, 100));
        content.add(toolBar1, BorderLayout.NORTH);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.setPreferredSize(new Dimension(10, 50));
        toolBar1.add(panel1);
        toolBarAutoButton = new JButton();
        toolBarAutoButton.setIcon(new ImageIcon(getClass().getResource("/resources/icons/repeat-24px.png")));
        toolBarAutoButton.setText("");
        panel1.add(toolBarAutoButton);
        toolBarStartButton = new JButton();
        toolBarStartButton.setIcon(new ImageIcon(getClass().getResource("/resources/icons/play_arrow-24px.png")));
        toolBarStartButton.setText("");
        panel1.add(toolBarStartButton);
        toolBarStepBackButton = new JButton();
        toolBarStepBackButton.setIcon(new ImageIcon(getClass().getResource("/resources/icons/skip_previous-24px.png")));
        toolBarStepBackButton.setText("");
        panel1.add(toolBarStepBackButton);
        toolBarStepForwardButton = new JButton();
        toolBarStepForwardButton.setIcon(new ImageIcon(getClass().getResource("/resources/icons/skip_next-24px.png")));
        toolBarStepForwardButton.setText("");
        panel1.add(toolBarStepForwardButton);
        toolBarPauseButton = new JButton();
        toolBarPauseButton.setIcon(new ImageIcon(getClass().getResource("/resources/icons/pause-24px.png")));
        toolBarPauseButton.setText("");
        panel1.add(toolBarPauseButton);
        toolBarStopButton = new JButton();
        toolBarStopButton.setIcon(new ImageIcon(getClass().getResource("/resources/icons/stop-24px.png")));
        toolBarStopButton.setText("");
        panel1.add(toolBarStopButton);
        toolBarSpeedSlider = new JSlider();
        toolBarSpeedSlider.setMaximum(5000);
        toolBarSpeedSlider.setMaximumSize(new Dimension(300, 31));
        toolBarSpeedSlider.setMinimum(1);
        toolBarSpeedSlider.setPaintLabels(false);
        toolBarSpeedSlider.setPaintTicks(true);
        toolBarSpeedSlider.setValue(1000);
        panel1.add(toolBarSpeedSlider);
        final JLabel label1 = new JLabel();
        label1.setText("Delay");
        panel1.add(label1);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        panel1.add(toolBar$Separator1);
        final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
        panel1.add(toolBar$Separator2);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel2.setMinimumSize(new Dimension(70, 10));
        panel2.setPreferredSize(new Dimension(206, 10));
        toolBar1.add(panel2);
        final JLabel label2 = new JLabel();
        label2.setText("Progress");
        panel2.add(label2);
        bottomProgressBar = new JProgressBar();
        bottomProgressBar.setValue(30);
        panel2.add(bottomProgressBar);
        graphEditor = new GraphEditor();
        graphEditor.setMinimumSize(new Dimension(100, 10));
        graphEditor.setPreferredSize(new Dimension(512, 10));
        content.add(graphEditor, BorderLayout.CENTER);
        final JToolBar toolBar2 = new JToolBar();
        toolBar2.setMinimumSize(new Dimension(64, 22));
        toolBar2.setPreferredSize(new Dimension(256, 22));
        content.add(toolBar2, BorderLayout.EAST);
        loggerTextPane = new JTextPane();
        toolBar2.add(loggerTextPane);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return content;
    }

}