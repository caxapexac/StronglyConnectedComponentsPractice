package ru.eltech.logic;

/**
 * @author Samoilova Anna
 */
public class Edge {
    public final Integer id;
    public Integer source;
    public Integer target;

    public Edge(Integer id) {
        this.id = id;
    }

    public Edge(Integer id, Node source, Node target) {
        this(id);
        this.source = source.id;
        this.target = target.id;
    }

    public Edge(Edge other) {
        this.id = other.id;
        this.source = other.source;
        this.target = other.target;
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
