package ru.eltech.logic;

import ru.eltech.view.GraphPlayerListener;
import ru.eltech.view.MainWindow;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Проигрыватель данных из {@link FrameList}
 */
public final class GraphPlayer {
    private Timer timer = new Timer();
    private final GraphPlayerListener listener;
    private FrameList frameList;
    private State state = State.Empty;
    private int currentFrame = 0;
    private long delay = 1000;

    public GraphPlayer(GraphPlayerListener listener) {
        this.listener = listener;
    }

    public void setFrameList(FrameList frameList) {
        MainWindow.log.info("Frame list setup");
        this.frameList = frameList;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (this.state == state) return;
        switch (state) {
            case Empty:
                // TODO
                break;
            case Started:
                if (this.state == State.Stoped) currentFrame = 0;
                play();
                break;
            case Paused:
                pause();
                break;
            case Stoped:
                stop();
                break;
        }
        this.state = state;
        listener.stateChange(state);
    }

    private void play() {
        MainWindow.log.info("Старт таймера");
        timer.cancel();
        timer = new Timer();
        timer.schedule(new PlayerTask(), delay);
    }

    private void pause() {
        MainWindow.log.info("Пауза таймера");
        timer.cancel();
    }

    private void stop() {
        MainWindow.log.warning("Стоп таймера");
        timer.cancel();
    }

    public void stepForward() {
        if (frameList == null || state == State.Empty || state == State.Stoped) return;
        if (currentFrame >= frameList.count() - 1) {
            MainWindow.log.warning("Фреймы кончились");
            setState(State.Paused);
            return;
        }
        Graph graph = frameList.get(++currentFrame);
        listener.frameChanged(graph);
    }

    public void stepBackward() {
        if(frameList == null || state == State.Empty || state == State.Stoped) return;
        if(currentFrame <= 0){
            MainWindow.log.warning("Нет предыдущего фрейма");
            setState(State.Paused);
            return;
        }
        Graph graph = frameList.get(--currentFrame);
        listener.frameChanged(graph);
    }

    public class PlayerTask extends TimerTask {

        @Override
        public void run() {
            stepForward();
            timer.cancel();
            timer = new Timer();
            timer.schedule(new PlayerTask(), delay);
        }
    }


    public enum State {
        Empty,
        Started,
        Paused,
        Stoped
    }
}
