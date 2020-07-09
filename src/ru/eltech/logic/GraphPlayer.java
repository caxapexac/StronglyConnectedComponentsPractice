package ru.eltech.logic;

import ru.eltech.view.GraphPlayerToolBar;
import ru.eltech.view.MainWindow;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Проигрыватель данных из {@link FrameList}
 */
public final class GraphPlayer {
    private Timer timer = new Timer();
    private FrameList frameList;
    private State state = State.Stop;
    private volatile int currentFrame = 0;
    private int delay = 300;

    private final GraphPlayerToolBar toolBar;

    public GraphPlayer(GraphPlayerToolBar toolBar) {
        this.toolBar = toolBar;
    }

    public FrameList getFrameList() {
        return frameList;
    }

    public void setFrameList(FrameList frameList) {
        //MainWindow.log.info("Frame list setup");
        this.frameList = frameList;
        toolBar.playerChanged(this);
    }

    public State getState() {
        return state;
    }

    public synchronized void setState(State state) {
        if (this.state == state) return;
        switch (state) {
            case Play:
                MainWindow.log.info("Старт анимации");
                if (frameList != null && currentFrame == frameList.count() - 1) currentFrame = 0;
                timer.cancel();
                timer = new Timer();
                timer.schedule(new PlayerTask(), delay);
                break;
            case Pause:
                MainWindow.log.info("Пауза анимации");
                timer.cancel();
                break;
            case Stop:
                MainWindow.log.warning("Стоп анимации");
                currentFrame = 0;
                timer.cancel();
                break;
        }
        this.state = state;
        toolBar.playerChanged(this);
    }

    public int getCurrentFrame(){
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
        toolBar.playerChanged(this);
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
        toolBar.playerChanged(this);
    }

    /**
     *
     * @return Переключился ли фрейм
     */
    public synchronized boolean stepForward() {
        if (frameList == null || state == State.Stop) return false;
        if (currentFrame >= frameList.count() - 1) {
            MainWindow.log.warning("Конец анимации");
            currentFrame = frameList.count() - 1;
            setState(State.Pause);
            return false;
        }
        currentFrame++;
        toolBar.playerChanged(this);
        //MainWindow.log.info(String.valueOf(frameList.get(getCurrentFrame()).state));
        return true;
    }

    /**
     *
     * @return Переключился ли фрейм
     */
    public synchronized boolean stepBackward() {
        if (frameList == null || state == State.Stop) return false;
        if (currentFrame <= 0) {
            //MainWindow.log.warning("Нет предыдущего фрейма");
            currentFrame = 0;
            setState(State.Pause);
            return false;
        }
        currentFrame--;
        toolBar.playerChanged(this);
        //MainWindow.log.info(String.valueOf(frameList.get(getCurrentFrame()).state));
        return true;
    }

    public class PlayerTask extends TimerTask {

        @Override
        public void run() {
            if (stepForward()) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(new PlayerTask(), delay);
            }
        }
    }


    public enum State {
        Play,
        Pause,
        Stop
    }
}
