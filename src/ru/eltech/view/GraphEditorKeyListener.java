package ru.eltech.view;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GraphEditorKeyListener implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {
//        GraphEditor graphEditor = (GraphEditor) e.getSource();
//        char key = e.getKeyChar();
//
//        int quickChangeStep = 1;
//
//        switch (key) {
//            case 'r':
//                quickSetColor(Color.RED);
//                break;
//            case 'g':
//                quickSetColor(Color.GREEN);
//                break;
//            case 'b':
//                quickSetColor(Color.BLUE);
//                break;
//            case 'q':
//                enableGrid(!drawGrid);
//                break;
//            case 'z':
//                createNewNode(mouseX, mouseY);
//                break;
//            case 'x':
//                initializeAddEdge(nodeUnderCursor);
//                break;
//            case '=':
//                quickChangeSize(quickChangeStep);
//                break;
//            case '-':
//                quickChangeSize(-quickChangeStep);
//                break;
//        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
//        GraphEditor graphEditor = (GraphEditor) e.getSource();
//        int moveDistance = e.isShiftDown() ? 10 : 1;
//
//        switch(e.getKeyCode()) {
//            case KeyEvent.VK_LEFT:
//                graphEditor.moveGraphStep(-moveDistance, 0);
//                break;
//            case KeyEvent.VK_RIGHT:
//                graphEditor.moveGraphStep(moveDistance, 0);
//                break;
//            case KeyEvent.VK_UP:
//                graphEditor.moveGraphStep(0, -moveDistance);
//                break;
//            case KeyEvent.VK_DOWN:
//                //moveGraphStep(0, moveDistance);
//                break;
//            case KeyEvent.VK_DELETE:
//                if(nodeUnderCursor != null) {
//                    graph.removeNode(nodeUnderCursor);
//                }else if(edgeUnderCursor != null) {
//                    graph.removeEdge(edgeUnderCursor);
//                }
//                break;
//        }
//
//        if(e.isControlDown()) {
//            switch(e.getKeyCode()) {
//                case KeyEvent.VK_S:
//                    serializeGraph();
//                    break;
//                case KeyEvent.VK_O:
//                    deserializeGraph();
//                    break;
//            }
//        }
//
//        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //
    }
}
