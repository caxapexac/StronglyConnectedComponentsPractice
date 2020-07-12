package ru.eltech.view;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Логика нажатия клавиш клавиатуры в {@link GraphEditor}
 */
public final class GraphEditorKeyListener implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
        GraphEditor graphEditor = (GraphEditor) e.getSource();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                graphEditor.moveGraphStep(e.isShiftDown() ? -10 : -1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                graphEditor.moveGraphStep(e.isShiftDown() ? 10 : 1, 0);
                break;
            case KeyEvent.VK_UP:
                graphEditor.moveGraphStep(0, e.isShiftDown() ? -10 : -1);
                break;
            case KeyEvent.VK_DOWN:
                graphEditor.moveGraphStep(0, e.isShiftDown() ? 10 : 1);
                break;
            case KeyEvent.VK_DELETE:
                graphEditor.destroySelected();
                break;
            case KeyEvent.VK_R:
                graphEditor.setColor(Color.RED);
                break;
            case KeyEvent.VK_G:
                graphEditor.setColor(Color.GREEN);
                break;
            case KeyEvent.VK_B:
                graphEditor.setColor(Color.BLUE);
                break;
            case KeyEvent.VK_EQUALS:
                graphEditor.changeSize(1);
                break;
            case KeyEvent.VK_MINUS:
                graphEditor.changeSize(-1);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //
    }
}