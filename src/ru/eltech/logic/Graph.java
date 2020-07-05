package ru.eltech.logic;

/**
 * * @author Samoilova Anna
 */

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Graph {
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

