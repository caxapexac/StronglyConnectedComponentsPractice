package ru.eltech.logic;

import ru.eltech.view.GraphPlayerListener;
import ru.eltech.view.MainWindow;

/**
 * TODO
 */
public class GraphPlayer {
    private final GraphPlayerListener listener;
    private FrameList frameList;
    private State state = State.Empty;
    private int currentFrame = 0;

    public GraphPlayer(GraphPlayerListener listener) {
        this.listener = listener;
    }

    public void setFrameList(FrameList frameList) {
        MainWindow.log.info("Frame list setup");
        this.frameList = frameList;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (this.state == state) return;
        this.state = state;
        switch (state) {
            case Empty:
                // TODO
                break;
            case Started:
                play();
                break;
            case Paused:
                pause();
                break;
            case Stoped:
                stop();
                break;
        }
    }

    private void play() {
        MainWindow.log.info("Старт таймера");
        // TODO
    }

    private void pause() {
        MainWindow.log.info("Пауза таймера");
        // TODO
    }

    private void stop() {
        MainWindow.log.warning("Стоп таймера");
        // TODO
    }

    public void stepForward() {
        if (frameList == null || state == State.Empty || state == State.Stoped) return;
        if (currentFrame >= frameList.count()) {
            MainWindow.log.warning("Фреймы кончились");
            setState(State.Stoped);
            return;
        }
        Graph graph = frameList.get(currentFrame++);
        listener.frameChanged(graph);
    }

    public void stepBackward() {
        // TODO
    }


    public enum State {
        Empty,
        Started,
        Paused,
        Stoped
    }
}
