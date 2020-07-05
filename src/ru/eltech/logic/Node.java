package ru.eltech.logic;

/**
 * * @author Samoilova Anna
 */

import java.awt.*;

public class Node {
    public final Integer id;
    public String name;
    public int radius = 10;
    public Point position = new Point(0, 0);

    public Node(Integer id) {
        this.id = id;
        this.name = id.toString();
    }

    private Node(Node other) {
        this.id = other.id;
        this.radius = other.radius;
        this.position = (Point) other.position.clone();
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public Node setXY(int x, int y) {
        this.position = new Point(x, y);
        return this;
    }

    @Override
    public Node clone() {
        return new Node(this);
    }
}
