package ru.eltech.logic;

import java.util.ArrayList;

/**
 * TODO
 */
public final class FrameList {
    private ArrayList<Graph> frames;

    public FrameList() {
        this.frames = new ArrayList<Graph>();
    }

    public int count() {
        return frames.size();
    }

    public void add(Graph frame) {
        frame.state = "";
        frames.add(new Graph(frame));
    }

    public void add(Graph frame, String state) {
        frame.state = state;
        frames.add(new Graph(frame));
    }

    public Graph get(int index) {
        return frames.get(index);
    }

//    public void generateLastStateInfo(String whatStep, int timeOutListSize, String edgeInvertion) {
//
//    }
}