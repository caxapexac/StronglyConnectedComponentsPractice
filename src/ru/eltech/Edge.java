package ru.eltech;

/**
 * * @author Samoilova Anna
 */

public class Edge {
    private Node source;
    private Node target;

    public Edge(Node source, Node target) {
        this.source = source;
        this.target = target;
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }
}
