package ru.eltech.view;

import javax.swing.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public final class LoggerTextAreaHandler extends Handler {
    private final JLabel textLabel;

    public LoggerTextAreaHandler(JLabel textLabel) {
        this.textLabel = textLabel;
    }

    @Override
    public void publish(LogRecord record) {
        String previous = "<html>Log:<br>";
        if (textLabel.getText().contains("</html>")) previous = textLabel.getText().substring(0, textLabel.getText().indexOf("</html>"));
        textLabel.setText(previous + String.format("%s %s<br></html>", record.getLevel().getLocalizedName(), record.getMessage()));
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
