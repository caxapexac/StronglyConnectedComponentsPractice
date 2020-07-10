package ru.eltech.view;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class LoggerTextAreaHandler extends Handler {
    private final JScrollPane scrollPane;
    private final JLabel textLabel;

    public LoggerTextAreaHandler(JScrollPane scrollPane, JLabel textLabel) {
        this.scrollPane = scrollPane;
        this.textLabel = textLabel;
    }

    @Override
    public void publish(LogRecord record) {
        String previous = "<html>Log:<br>";
        if (textLabel.getText().contains("</html>")) previous = textLabel.getText().substring(0, textLabel.getText().indexOf("</html>"));
        Level cur = record.getLevel();
        String levelLabel = cur == Level.INFO ? "" : record.getLevel().getLocalizedName();
        textLabel.setText(previous + String.format("%s%s<br></html>", levelLabel, record.getMessage()));
        textLabel.setText(previous + String.format("%s %s<br></html>", record.getLevel().getLocalizedName(), record.getMessage()));
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }

    @Override
    public void flush() {
        // Nothing
    }

    @Override
    public void close() throws SecurityException {
        textLabel.setText("");
    }
}
