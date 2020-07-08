package ru.eltech.view;

import ru.eltech.logic.Graph;
import ru.eltech.logic.GraphPlayer;

public final class GraphPlayerListener {
    private MainWindow parent;

    public GraphPlayerListener(MainWindow parent) {
        this.parent = parent;
    }

    public void frameChanged(Graph graph) {
        //MainWindow.log.info("frameChanged");
        parent.stepVisualizing(graph);
    }

    public void stateChange(GraphPlayer.State state) {
        switch (state) {
            case Empty:
                break;
            case Started:
                break;
            case Paused:
                break;
            case Stoped:
                parent.stopVisualizing();
                break;
        }
        //MainWindow.log.info("stateChange");
    }
}
