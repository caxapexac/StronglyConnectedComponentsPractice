package ru.eltech.logic;

import java.util.ArrayList;

/**
 * TODO
 */
public class FrameList {
    private ArrayList<Graph> frames;

    public FrameList() {
        this.frames = new ArrayList<Graph>();
    }

    public int count() {
        return frames.size();
    }

    public void add(Graph frame) {
        frames.add(new Graph(frame));
    }

    public Graph get(int index) {
        return frames.get(index);
    }
}