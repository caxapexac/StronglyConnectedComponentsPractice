package ru.eltech.logic;

import java.awt.*;

/**
 * @author Samoilova Anna
 */
public final class Edge {
    public final Integer id;
    private Integer source;
    private Integer target;
    private Color color = null;
    private int stroke = 0;

    public boolean highlighted = false;
    public boolean connectsStrongComponents = false;

    public Edge(Integer id, Node source, Node target) {
        this.id = id;
        this.source = source.id;
        this.target = target.id;
    }

    public Edge(Edge other) {
        this.id = other.id;
        this.source = other.source;
        this.target = other.target;
        this.highlighted = other.highlighted;
        this.connectsStrongComponents = other.connectsStrongComponents;
        this.color = other.color;
        this.stroke = other.stroke;
    }

    public Integer getSource() {
        return source;
    }

    public Integer getTarget() {
        return target;
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public int getStroke(){
        return  stroke;
    }

    public void setStroke(int stroke){
        this.stroke = stroke;
    }

    public void invert() {
        Integer temp = source;
        source = target;
        target = temp;
    }

    public String getDescription() {
        String highlight = highlighted ? "подсвечено" : "не подсвечено";
        return "Ребро с id " + id + ". Из вершины " + source +
                " в вершину " + target + ". Сейчас " + highlight;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Edge clone() {
        return new Edge(this);
    }
}