package ru.eltech.logic;

import java.awt.*;

/**
 * @author Samoilova Anna
 */
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
        this.name = other.name;
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
        this.position.move(x, y);
        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Node clone() {
        return new Node(this);
    }
}
