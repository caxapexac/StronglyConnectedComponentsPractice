package ru.eltech.logic;

/**
 * * @author Samoilova Anna
 */

public class Edge {
    public final Integer id;
    public Integer source;
    public Integer target;

    public Edge(Integer id) {
        this.id = id;
    }

    public Edge(Edge other) {
        this.id = other.id;
        this.source = other.source;
        this.target = other.target;
    }

    public Edge fromTo(Node fromNode, Node toNode) {
        this.source = fromNode.id;
        this.target = toNode.id;
        return this;
    }

    @Override
    public Edge clone() {
        return new Edge(this);
    }
}
