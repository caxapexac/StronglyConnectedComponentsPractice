package ru.eltech.logic;

/**
 * * @author Samoilova Anna
 */

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Graph {
    private int nextNodeId = 1;
    private int nextEdgeId = 1;
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();
    private final HashMap<Integer, Edge> edgeMap = new HashMap<>();

    public Node createNode() {
        while (nodeMap.containsKey(nextNodeId)) nextNodeId++;
        Node node = new Node(nextNodeId++);
        nodeMap.put(node.id, node);
        return node;
    }

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


    public Collection<Node> getNodes() {
        return nodeMap.values();
    }

    public Node getNode(Integer nodeId) {
        return nodeMap.get(nodeId);
    }


    public Edge findEdge(Node from, Node to) {
        if (!containsNode(from) || !containsNode(to))
            return null;
        return findEdge(from.id, to.id);
    }

    public Edge findEdge(Integer from, Integer to) {
        for (Edge edge : getEdges())
            if ((edge.fromNode == from && edge.toNode == to) || (edge.fromNode == to && edge.toNode == from)) {
                return edge;
            }
        return null;
    }


    public Edge getEdge(Integer edgeId) {
        return edgeMap.get(edgeId);
    }

    public Collection<Edge> getEdges() {
        return edgeMap.values();
    }

    public Graph set(Graph other) {
        clear();
        this.nextNodeId = other.nextNodeId;
        this.nextEdgeId = other.nextEdgeId;
        for (Node node : other.getNodes()) nodeMap.put(node.id, node.clone());
        for (Edge edge : other.getEdges()) edgeMap.put(edge.id, edge.clone());
        return this;
    }

    public Graph clear() {
        this.nextNodeId = 1;
        this.nextEdgeId = 1;
        nodeMap.clear();
        edgeMap.clear();
        return this;
    }

    //region SERVICE

    public void invertAllEdges() {
        for (Edge edge : getEdges()) edge.invert();
    }

    //endregion


}

public class GraphOld {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public Graph() {
        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();
    }

    private int getNodeIndex(String id) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getId().equals(id)) {
                return i;
            }
        }
        return 0;  // todo
    }

    public void load(InputStream stream) throws IOException, NumberFormatException {
        nodes.clear();
        edges.clear();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line = reader.readLine();
        int node_numb;
        try {
            node_numb = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            throw e;
        }
        for (int i = 0; i < node_numb; i++) {
            line = reader.readLine();
            String[] splitStrs = line.split("[ ]");
            try {
                createNode(splitStrs[0], new Point(Integer.parseInt(splitStrs[1]), Integer.parseInt(splitStrs[2])));
            } catch (NumberFormatException e) {
                throw e;
            }
        }
        line = reader.readLine();
        int edge_numb;
        try {
            edge_numb = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            throw e;
        }
        for (int i = 0; i < edge_numb; i++) {
            line = reader.readLine();
            String[] splitStrs = line.split("[ ]");
            createEdge(nodes.get(getNodeIndex(splitStrs[0])), nodes.get(getNodeIndex(splitStrs[1])));
        }
    }

    public void save(OutputStream stream) {

    }

    public int getNodesCount() {
        return nodes.size();
    }

    public Node getNode(int index) {
        return nodes.get(index);
    }

    public Node createNode(String name, Point position) {
        Node newNode = new Node(name);
        newNode.setPosition(position);
        nodes.add(newNode);
        return newNode;
    }

    public Edge createEdge(Node source, Node target) {
        Edge newEdge = new Edge(source, target);
        edges.add(newEdge);
        source.addEdge(newEdge);
        return newEdge;
    }

    public void removeNode(int index) {
        int i = 0;
        while (i < edges.size()) {
            if (edges.get(i).getSource().getId().equals(nodes.get(index).getId()) || edges.get(i).getTarget().getId().equals(nodes.get(index).getId())) {
                removeEdge(edges.get(i).getSource(), edges.get(i).getTarget());
            } else {
                i++;
            }
        }
        nodes.remove(index);
    }

    public void removeNode(String id) {
        int i = 0;
        while (i < edges.size()) {
            if (edges.get(i).getSource().getId().equals(id) || edges.get(i).getTarget().getId().equals(id)) {
                removeEdge(edges.get(i).getSource(), edges.get(i).getTarget());
            } else {
                i++;
            }
        }
        for (i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getId().equals(id)) {
                nodes.remove(nodes.get(i));
                break;
            }
        }
    }

    public void removeEdge(Node source, Node target) {
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getSource().getId().equals(source.getId()) && edges.get(i).getTarget().getId().equals(target.getId())) {
                edges.remove(edges.get(i));
                break;
            }
        }
        source.removeEdge(target);
    }
}

