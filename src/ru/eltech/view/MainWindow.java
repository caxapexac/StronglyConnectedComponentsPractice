package ru.eltech.view;

import ru.eltech.logic.*;

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
                System.exit(0);
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
        // TODO
        /*StringBuilder helpMsg = new StringBuilder();
        try {
            String path = this.getClass().getClassLoader().getResource("resorces/helpMessage.html").getPath();
            File indexFile = new File(path);
            Scanner helpMessageReader = new Scanner(indexFile);
            while (helpMessageReader.hasNextLine()) {
                helpMsg.append(helpMessageReader.nextLine());
            }
            helpMessageReader.close();
            JOptionPane.showMessageDialog(this, helpMsg.toString(), "text", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException runtimeException) {
            JOptionPane.showMessageDialog(this, runtimeException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }*/
        String formattedText = "<html>\n" +
                "<font size=\"5\">\n" +
                "    <table border=\"2\", cellspacing=\"1\", title=\"Справка\", bgcolor=\"EEEEFF\">\n" +
                "        <caption><b><font size=\"6\", face=\"cursive, fantasy\">Доступные опции</font></b></caption>\n" +
                "        <tr>\n" +
                "            <th>Действие</th>\n" +
                "            <th>Клавиша</th>\n" +
                "        </tr>\n" +
                "   <tr><td>Создать вершину</td><td>ЛКМ двойной клик</td></tr>\n" +
                "   <tr><td>Открыть контекстное меню</td><td>ПКМ</td></tr>\n" +
                "   <tr><td>Выделить вершину</td><td>ЛКМ по вершине</td></tr>\n" +
                "   <tr><td>Переместить вершину</td><td>ЛКМ зажать</td></tr>\n" +
                "   <tr><td>Провести ребро от выделенной вершины</td><td>удерживать ПКМ</td></tr>\n" +
                "   <tr><td>Автовыполнение алгоритма</td><td>D</td></tr>\n" +
                "   <tr><td>Запустить алгоритм</td><td>S</td></tr>\n" +
                "   <tr><td>Пауза</td><td>P</td></tr>\n" +
                "   <tr><td>Остановить алгоритм</td><td>A</td></tr>\n" +
                "   <tr><td>Шаг вперед</td><td>D</td></tr>\n" +
                "   <tr><td>Шаг назад</td><td>D</td></tr>\n" +
                "   <tr><td>Удалить выделенные вершины</td><td>Delete</td></tr>\n" +
                "   </table>\n" +
                "</html>";
        JLabel message = new JLabel(formattedText);
        JOptionPane.showMessageDialog(this, message, "Help", JOptionPane.PLAIN_MESSAGE);

    }

    public void showAuthorsInfo() {
        // TODO
        String formattedText = "<html>\n" +
                "<font size=\"5\">\n" +
                "    <table border=\"2\", cellspacing=\"1\", title=\"Справка\", bgcolor=\"EEEEFF\">\n" +
                "        <caption><b><font size=\"6\", face=\"cursive, fantasy\">Авторы программы</font></b></caption>\n" +
                "        <tr>\n" +
                "            <th>Имя</th>\n" +
                "            <th>Группа</th>\n" +
                "            <th>Отвечает за</th>\n" +
                "        </tr>\n" +
                "        <tr><td>Сахаров Виктор</td><td>8381</td><td>графический интерфейс</td></tr>\n" +
                "        <tr><td>Самойлова Анна</td><td>8303</td><td>логика алгоритма</td></tr>\n" +
                "        <tr><td>Гоголев Евгений</td><td>8381</td><td>логика визуализации</td></tr>\n" +
                "    </table>\n" +
                "    <br>\n" +
                "    <center>\n" +
                "        СПбГЭТУ \"ЛЭТИ\" <br>\n" +
                "        2020 год\n" +
                "    </center>\n" +
                "</font>";
        JLabel message = new JLabel(formattedText);
        JOptionPane.showMessageDialog(this, message, "Authors", JOptionPane.PLAIN_MESSAGE);
    }

    //endregion
}