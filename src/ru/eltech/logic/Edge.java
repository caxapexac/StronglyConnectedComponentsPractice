package ru.eltech.logic;

/**
 * @author Samoilova Anna
 */
public class Edge {
    public final Integer id;
    private Integer source;
    private Integer target;

    public boolean highlighted = false;

    public Edge(Integer id, Node source, Node target) {
        this.id = id;
        this.source = source.id;
        this.target = target.id;
    }

    public Edge(Edge other) {
        this.id = other.id;
        this.source = other.source;
        this.target = other.target;
    }

    public Integer getSource() {
        return source;
    }

    public Integer getTarget() {
        return target;
    }

    public void invert() {
        Integer temp = source;
        source = target;
        target = temp;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Edge clone() {
        return new Edge(this);
    }
}