package ru.eltech.logic;

import java.awt.*;

/**
 * @author Samoilova Anna
 */
public final class Node {
    public final Integer id;
    private String name;
    private int radius;
    private Point position;
    private Color color = null;

    public boolean visited = false;
    public boolean highlighted = false;
    public int strongComponentId = -1;
    public int timeOut = 0;

    public Node(Integer id, int x, int y) {
        this.id = id;
        this.setName(id.toString());
        this.setRadius(32);
        this.setPosition(new Point(x, y));
    }

    private Node(Node other) {
        this.id = other.id;
        this.setName(other.getName());
        this.setRadius(other.getRadius());
        this.setPosition((Point) other.position.clone());
        this.visited = other.visited;
        this.highlighted = other.highlighted;
        this.strongComponentId = other.strongComponentId;
        this.timeOut = other.timeOut;
        this.color = other.color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Point getPosition() {
        return position;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setX(int x) {
        this.position.x = x;
    }

    public void setY(int y) {
        this.position.y = y;
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Node clone() {
        return new Node(this);
    }

    public String getDescription() {
        String componentIdString = (strongComponentId != -1) ? Integer.toString(strongComponentId) : "еще на найден";
        return "Вершина " + id + " Имя: " + name +
                " ID компоненты: " + componentIdString +
                " Позиция на экране: x = " + getX() + " | y = " + getY();
    }
}