package ru.eltech.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * @author Samoilova Anna
 */
public final class Graph {
    private int nextNodeId;
    private int nextEdgeId;
    private final HashMap<Integer, Node> nodeMap;
    private final HashMap<Integer, Edge> edgeMap;
    public String state = "";

    public Graph() {
        nextNodeId = 1;
        nextEdgeId = 1;
        nodeMap = new HashMap<>();
        edgeMap = new HashMap<>();
    }

    public Graph(Graph other) {
        this.nextNodeId = other.nextNodeId;
        this.nextEdgeId = other.nextEdgeId;
        this.state = other.state;
        this.nodeMap = new HashMap<>();
        for (Node node : other.getNodes()) nodeMap.put(node.id, node.clone());
        this.edgeMap = new HashMap<>();
        for (Edge edge : other.getEdges()) edgeMap.put(edge.id, edge.clone());
    }


    public Node createNode(int x, int y) {
        while (nodeMap.containsKey(nextNodeId)) nextNodeId++;
        Node node = new Node(nextNodeId++, x, y);
        nodeMap.put(node.id, node);
        return node;
    }

    @SuppressWarnings("UnusedReturnValue")
    public Edge createEdge(Node from, Node to) {
        while (nodeMap.containsKey(nextEdgeId)) nextEdgeId++;
        Edge edge = new Edge(nextEdgeId++, from, to);
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

    public int getVisitedNodesCount() {
        int count = 0;
        for(Node node : getNodes()) {
            if(node.visited) ++count;
        }
        return count;
    }

    public Node getNode(Integer nodeId) {
        return nodeMap.get(nodeId);
    }

    public Node getNode(String nodeName) {
        for (Node node : getNodes()) {
            if (node.getName().equals(nodeName)) return node;
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
            if ((edge.getSource().equals(source) && edge.getTarget().equals(target)) || (!ignoreDirections && edge.getSource().equals(target) && edge.getTarget().equals(source))) {
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
            if (edge.getSource().equals(node.id)) result.add(edge);
        }
        return result;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean containsNode(Node node) {
        return nodeMap.get(node.id) == node;
    }

    public boolean containsEdge(Edge edge) {
        return edgeMap.get(edge.id) == edge;
    }

    public boolean destroyNode(Node node) {
        Node removed = nodeMap.remove(node.id);
        if (removed == node) {
            edgeMap.values().removeIf(e -> e.getSource().equals(node.id) || e.getTarget().equals(node.id));
            return true;
        }
        nodeMap.put(removed.id, removed);
        return false;
    }

    public boolean destroyEdge(Edge edge) {
        if (edge == null) return false;
        Edge removed = edgeMap.remove(edge.id);
        if (removed == edge) return true;
        edgeMap.put(removed.id, removed);
        return false;
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
                node.setName(name);
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
        } catch (Exception e) {
            throw new IOException(e);
        }
        return this;
    }

    public void save(OutputStream stream) {
        PrintWriter p = new PrintWriter(stream);
        p.println(nodeMap.size());
        for (Node node : nodeMap.values()) {
            p.println(String.format("%s %d %d", node.getName(), node.getX(), node.getY()));
        }
        p.println(edgeMap.size());
        for (Edge edge : edgeMap.values()) {
            Node source = getNode(edge.getSource());
            Node target = getNode(edge.getTarget());
            p.println(String.format("%s %s", source.getName(), target.getName()));
        }
        p.flush();
    }

    public String getNodesInComponentById(int strongComponentId) {
        String nodesStr = "| ";
        for (Node node : getNodes()) {
            if (node.strongComponentId == strongComponentId) {
                nodesStr += node.getName() + " | ";
            }
        }
        return nodesStr;
    }

    public boolean componentExists(int id) {
        boolean res = false;
        for (Node node : getNodes()) {
            if (node.strongComponentId == id) {
                res = true;
            }
        }
        return res;
    }
}