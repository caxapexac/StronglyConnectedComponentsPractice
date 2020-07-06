package ru.eltech.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

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

    public Node getNode(String nodeName) {
        for (Node node : getNodes()) {
            if (node.name.equals(nodeName)) return node;
        }
        return null;
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

    public Graph load(InputStream stream) throws IOException {
        clear();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            int nodesCount = Integer.parseInt(reader.readLine());
            for (int i = 0; i < nodesCount; i++) {
                StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
                String name = tokenizer.nextToken();
                int posX = Integer.parseInt(tokenizer.nextToken());
                int posY = Integer.parseInt(tokenizer.nextToken());
                Node node = createNode(posX, posY);
                node.name = name;
            }
            int edgesCount = Integer.parseInt(reader.readLine());
            for (int i = 0; i < edgesCount; i++) {
                StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
                Node source = getNode(tokenizer.nextToken());
                Node target = getNode(tokenizer.nextToken());
                if (source == null || target == null) {
                    System.out.println("Edge with missed nodes " + source + " " + target);
                    continue;
                }
                createEdge(source, target);
            }
        }
        catch (Exception e) {
            throw new IOException(e);
        }
        return this;
    }

    public void save(OutputStream stream) {
        PrintWriter p = new PrintWriter(stream);
        p.println(nodeMap.size());
        for (Node node : nodeMap.values()) {
            p.println(String.format("%s %d %d", node.name, node.getX(), node.getY()));
        }
        p.println(edgeMap.size());
        for (Edge edge : edgeMap.values()) {
            Node source = getNode(edge.source);
            Node target = getNode(edge.target);
            p.println(String.format("%s %s", source.name, target.name));
        }
        p.flush();
    }
}