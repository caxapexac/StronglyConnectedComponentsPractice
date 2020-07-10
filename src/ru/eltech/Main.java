package ru.eltech;

//import java.io.IOException;

import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;
import ru.eltech.view.MainWindow;
import javax.swing.*;

public final class Main {
    public static void main(String[] args) {
        FlatAtomOneDarkContrastIJTheme.install();
        //FlatGitHubContrastIJTheme.install();
        //FlatGitHubIJTheme.install();
        //FlatDraculaIJTheme.install();
        //FlatSolarizedDarkIJTheme.install();
        //FlatDarkPurpleIJTheme.install();
        //UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
        //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); java metal style
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); windows style
        MainWindow window = new MainWindow();
        window.setVisible(true);
    }
}