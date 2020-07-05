package ru.eltech.view;

import ru.eltech.logic.Graph;

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

    private Graph graph = new Graph();


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
        //graphPanel.deserializeGraph(AUTOSAVE_FILE);
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

    public void serializeGraph() {
        if (graphEditor.getGraph() == null) return;
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
                graphEditor.getGraph().save(fos);
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
                graphEditor.getGraph().load(fis);
                JOptionPane.showMessageDialog(null, "Граф загружен успешно " + selectedFile.getAbsolutePath());
                repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Файл не найден!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showNodesList() {
        //String nodesList = graphEditor.getGraph().getListOfNodes();
        //JOptionPane.showMessageDialog(this, nodesList,"Lista wкzіуw grafu", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showEdgesList() {
        //String nodesList = graphEditor.getGraph().getListOfEdges();
        //JOptionPane.showMessageDialog(this, nodesList,"Lista wкzіуw grafu", JOptionPane.INFORMATION_MESSAGE);
    }
}

class TemporaryGraphEditor extends JFrame {
    private static final long serialVersionUID = 508317535368185508L;
    private static final String APP_INFO =
            "Aby wyњwietliж graf przykіadowy:\n"
                    + "Menu \"Plik\" => \"Pokaї przykіad\"\n"
                    + "(powoduje usuniкcie obecnego wyњwietlanego grafu).\n\n"
                    + "Obsіuga myszy:\n"
                    + "(akcja zaleїna od poіoїenia kursora):\n"
                    + "- LPM - przesuwanie caіego grafu/wybranego wкzіa/wybranej krawкdzi\n"
                    + "- PPM - menu kontekstowe ogуlne/wybranego wкzіa/wybranej krawкdzi\n\n"
                    + "Skrуty klawiszowe:\n"
                    + "- \"q\" - wі№cz/wyі№cz siatkк pomocnicz№\n"
                    + "- \"z\" - dodaj wкzкі w miejscu kursora\n"
                    + "- \"x\" - dodaj krawкdџ wychodz№c№ z wкzіa bкd№cego pod kursorem\n"
                    + "- Alt + \"LITERA\" - otwiera menu odpowiadaj№ce literze podkreњlonej\n"
                    + "                   na pasku menu\n"
                    + "- Ctrl + \"s\" - zapisz graf do pliku\n"
                    + "- Ctrl + \"o\" - wczytaj graf z pliku\n\n"
                    + "Gdy kursor znajduje siк nad wкzіem/krawкdzi№ specjaln№\n"
                    + "- \"r\", \"g\", \"b\" - ustaw kolor czerwony/zielony/niebieski\n"
                    + "- \"+\" - zwiкksz promieс wкzіa/gruboњж krawкdzi\n"
                    + "- \"-\" - zmiejsz promieс wкzіa/gruboњж krawкdzi\n"
                    + "\n\n Program posiada funkcjк autozapisu i autowczytania grafu.";
    private static final String AUTHOR_INFO =
            "Program do edycji grafуw.\n"
                    + "Program:  " + "Graph Editor" + "\n"
                    + "Autor:    Michaі Tkacz \n"
                    + "Data:     listopad 2019 r.";
    private static final String AUTOSAVE_FILE = "AUTOSAVE.bin";

    private void showInstruction() {
        JOptionPane.showMessageDialog(this, APP_INFO, "Informacje o programie", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAuthorInfo() {
        JOptionPane.showMessageDialog(this, AUTHOR_INFO, "Informacje o autorze", JOptionPane.INFORMATION_MESSAGE);
    }

}