package ru.eltech.logic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Samoilova Anna
 */
public class Graph {
    private int nextNodeId = 1;
    private int nextEdgeId = 1;
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();
    private final HashMap<Integer, Edge> edgeMap = new HashMap<>();

    public Node createNode(int x, int y) {
        return createNode().setXY(x, y);
    }

    private Node createNode() {
        while (nodeMap.containsKey(nextNodeId)) nextNodeId++;
        Node node = new Node(nextNodeId++);
        nodeMap.put(node.id, node);
        return node;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Edge createEdge(Node from, Node to) {
        Edge edge = createEdge();
        edge.source = from.id;
        edge.target = to.id;
        return edge;
    }

    private Edge createEdge() {
        while (nodeMap.containsKey(nextEdgeId)) nextEdgeId++;
        Edge edge = new Edge(nextEdgeId++);
        edgeMap.put(edge.id, edge);
        return edge;
    }

    public Collection<Node> getNodes() {
        return nodeMap.values();
    }

    public Collection<Edge> getEdges() {
        return edgeMap.values();
    }

    public int getNodesCount() {
        return nodeMap.size();
    }

    public int getEdgesCount() {
        return edgeMap.size();
    }

    public Node getNode(Integer nodeId) {
        return nodeMap.get(nodeId);
    }

    public Edge getEdge(Integer edgeId) {
        return edgeMap.get(edgeId);
    }

    public Edge getEdge(Node source, Node target, boolean ignoreDirections) {
        if (!containsNode(source) || !containsNode(target)) return null;
        return getEdge(source.id, target.id, ignoreDirections);
    }

    public Edge getEdge(Integer source, Integer target, boolean ignoreDirections) {
        for (Edge edge : getEdges()) {
            if ((edge.source.equals(source) && edge.target.equals(target)) || (!ignoreDirections && edge.source.equals(target) && edge.target.equals(source))) {
                return edge;
            }
        }
        return null;
    }

    public Collection<Edge> getEdgesFromNode(Node node) {
        return getEdgesFromNodeNoAlloc(node, new ArrayList<>());
    }

    public Collection<Edge> getEdgesFromNodeNoAlloc(Node node, Collection<Edge> result) {
        result.clear();
        for (Edge edge : getEdges()) {
            if (edge.source.equals(node.id)) result.add(edge);
        }
        return result;
    }

    public boolean containsNode(Node node) {
        return nodeMap.get(node.id) == node;
    }

    public boolean containsEdge(Edge edge) {
        return edgeMap.get(edge.id) == edge;
    }

    public boolean destroyNode(Node node) {
        Node removed = nodeMap.remove(node.id);
        if (removed == node) {
            edgeMap.values().removeIf(e -> e.source.equals(node.id) || e.target.equals(node.id));
            return true;
        }
        nodeMap.put(removed.id, removed);
        return false;
    }

    public boolean destroyEdge(Edge edge) {
        Edge removed = edgeMap.remove(edge.id);
        if (removed == edge) return true;
        edgeMap.put(removed.id, removed);
        return false;
    }

    public Graph set(Graph other) {
        clear();
        this.nextNodeId = other.nextNodeId;
        this.nextEdgeId = other.nextEdgeId;
        for (Node node : other.getNodes()) nodeMap.put(node.id, node.clone());
        for (Edge edge : other.getEdges()) edgeMap.put(edge.id, edge.clone());
        return this;
    }

    public void clear() {
        this.nextNodeId = 1;
        this.nextEdgeId = 1;
        nodeMap.clear();
        edgeMap.clear();
    }

    public void load(InputStream stream) throws IOException, NumberFormatException {
//        nodes.clear();
//        edges.clear();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//        String line = reader.readLine();
//        int node_numb;
//        try {
//            node_numb = Integer.parseInt(line);
//        } catch (NumberFormatException e) {
//            throw e;
//        }
//        for (int i = 0; i < node_numb; i++) {
//            line = reader.readLine();
//            String[] splitStrs = line.split("[ ]");
//            try {
//                createNode(splitStrs[0], new Point(Integer.parseInt(splitStrs[1]), Integer.parseInt(splitStrs[2])));
//            } catch (NumberFormatException e) {
//                throw e;
//            }
//        }
//        line = reader.readLine();
//        int edge_numb;
//        try {
//            edge_numb = Integer.parseInt(line);
//        } catch (NumberFormatException e) {
//            throw e;
//        }
//        for (int i = 0; i < edge_numb; i++) {
//            line = reader.readLine();
//            String[] splitStrs = line.split("[ ]");
//            createEdge(nodes.get(getNodeIndex(splitStrs[0])), nodes.get(getNodeIndex(splitStrs[1])));
//        }
    }

    public void save(OutputStream stream) {

    }
}