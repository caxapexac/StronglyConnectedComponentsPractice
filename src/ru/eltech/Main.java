package ru.eltech;

import java.io.IOException;

import ru.eltech.view.MainWindow;

public class Main {
    public static void main(String[] args) throws IOException {
        //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); java metal style
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); windows style
        MainWindow window = new MainWindow();
        window.setVisible(true);
    }
}