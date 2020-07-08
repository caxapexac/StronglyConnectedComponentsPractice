package ru.eltech;

//import java.io.IOException;

import ru.eltech.view.MainWindow;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
//        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); java metal style
//        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); windows style
        MainWindow window = new MainWindow();
        window.setVisible(true);
    }
}