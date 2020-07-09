package ru.eltech;

//import java.io.IOException;

import ru.eltech.view.MainWindow;
import javax.swing.*;

public final class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        }
        //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); java metal style
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); windows style
        MainWindow window = new MainWindow();
        window.setVisible(true);
    }
}