package ru.eltech.view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class LoggerTextAreaHandler extends Handler {
    private final JScrollPane scrollPane;
    private final JTextPane textPane;
    private final ArrayList<String> messages = new ArrayList<>();

    public LoggerTextAreaHandler(JScrollPane scrollPane, JTextPane textPane) {
        this.scrollPane = scrollPane;
        this.textPane = textPane;
        this.textPane.setContentType("text/html");
        render();
    }

    @Override
    public void publish(LogRecord record) {
        String levelLabel = record.getLevel() == Level.INFO ? "" : record.getLevel().getLocalizedName();
        messages.add(String.format("%s %s", levelLabel, record.getMessage()));
        render();
    }

    private void render() {
        StringBuilder content = new StringBuilder();
        //content.append("<html><body style=\"text-align: justify; text-justify: inter-word;\">Log:<br>");
        content.append("<html><body>Log:<br>");
        for (String message: messages) {
            content.append(message);
            if (!message.trim().isEmpty()) content.append("<br>");
        }
        content.append("</body></html>");
        textPane.setText(content.toString());
        // Хак для того, чтобы скроллбар оставался строго внизу
        if (!scrollPane.getVerticalScrollBar().getValueIsAdjusting()) SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    }

    @Override
    public void flush() {
        // Nothing
    }

    @Override
    public void close() throws SecurityException {
         clear();
    }

    public void clear() {
        messages.clear();
        render();
    }
}
