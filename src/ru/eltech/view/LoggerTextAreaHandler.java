package ru.eltech.view;

import javax.swing.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoggerTextAreaHandler extends Handler {
    private final JTextPane textPane;

    public LoggerTextAreaHandler(JTextPane textPane) {
        this.textPane = textPane;
    }

    @Override
    public void publish(LogRecord record) {
        textPane.setText(textPane.getText() + String.format("%s %s\n", record.getLevel().getLocalizedName(), record.getMessage()));
    }

    @Override
    public void flush() {
        // Nothing
    }

    @Override
    public void close() throws SecurityException {
        textPane.setText("");
    }
}
