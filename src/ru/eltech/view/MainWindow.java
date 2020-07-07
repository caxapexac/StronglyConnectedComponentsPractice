package ru.eltech.view;

import ru.eltech.logic.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.*;
import java.util.logging.Logger;

public class MainWindow extends JFrame {
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
                System.exit(0);
            }

            @Override
            public void windowClosing(WindowEvent event) {
                windowClosed(event);
            }
        });
        log.addHandler(new LoggerTextAreaHandler(loggerTextPane));
    }

    //region ACTIONS

    public void createNewGraph() {
        log.info("Create new graph");
        log.warning("Create new graph");
        log.severe("Create new graph");
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

        int chosenOption = fc.showOpenDialog(this);
        if (chosenOption == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                graphEditor.setRenderGraph(graph.load(fis));
                JOptionPane.showMessageDialog(null, "Граф загружен успешно " + selectedFile.getAbsolutePath());
                repaint();
            } catch (IOException e) {
                e.printStackTrace();
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
        //FrameList frames = KosarajuAlgorithm( graphEditor.getGraph() );
        //graphPlayer.play(frames);
    }

    public void showInstruction() {
        showHtmlFormattedMessageDialog("/resources/docs/helpMessage.html", "Help", JOptionPane.PLAIN_MESSAGE);
    }

    public void showAuthorsInfo() {
        showHtmlFormattedMessageDialog("/resources/docs/authors.html", "Authors", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * @param filename
     * @return file as String
     * @apiNote Helper function to read html resources
     */
    private String readResourceFileAsString(String filename, String charsetName) {
        InputStream in = getClass().getResourceAsStream(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, charsetName));
        } catch (UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(this, "Incorrect encoding", "Error", JOptionPane.ERROR_MESSAGE);
        }
        StringBuilder result = new StringBuilder();
        String line = null;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
            result.append(line);
        }
        return result.toString();
    }

    /**
     * @apiNote Shows message dialog, takes message text from a html-encoded file in resources folder
     * @param filename
     * @param title
     * @param messageType
     */
    private void showHtmlFormattedMessageDialog(String filename, String title, int messageType) {
        String formattedText = readResourceFileAsString(filename, "UTF-8");
        JLabel message = new JLabel(formattedText);
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    //endregion
}