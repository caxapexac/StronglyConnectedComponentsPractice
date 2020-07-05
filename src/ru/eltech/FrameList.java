package ru.eltech;

import java.util.ArrayList;

public class FrameList {
    private ArrayList<Graph> frames;
    public FrameList() {
        this.frames = new ArrayList<Graph>();
    }
    public int count() {
        return frames.size();
    }
    public void add(Graph frame) {
        frames.add(frame);
    }
    public Graph get(int index) {
        return frames.get(index);
    }
}
