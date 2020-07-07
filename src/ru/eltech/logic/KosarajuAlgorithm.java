package ru.eltech.logic;

import ru.eltech.logic.Algorithm;
import ru.eltech.logic.FrameList;
import ru.eltech.logic.Graph;

import java.util.ArrayList;
import java.util.Collection;

public class KosarajuAlgorithm implements Algorithm {

    private ArrayList<Node> timeOutList;
    private FrameList frames;

    public KosarajuAlgorithm() {
        //вершины по времени выхода в порядке возрастания
        timeOutList = new ArrayList<Node>();
        //возвращаемые фреймы
        frames = new FrameList();
    }

    /**
     * @param context Граф, над которым требуется выполнить алгоритм
     * @return
     * @implNote sort nodes by timeout
     * result goes to global "timeOutList"
     * frames are created by timeOut() and written down to global "frames
     */
    @Override
    public FrameList process(Graph context) {

        Collection<Node> nodes = context.getNodes();
        for (Node node : nodes) {
            if (!node.visited) {
                timeOut(node, context);
            }
        }

        clearVisitedNodes(context);
        clearHighlightedNodes(context);
        clearHighlightedEdges(context);

        reverseGraph(context);

        clearHighlightedEdges(context);

        int currentComponentId = 0;
        for (Node currentNode : timeOutList) {
            if (currentNode.strongComponentId == -1) {
                findComponent(currentComponentId, currentNode, context);
                ++currentComponentId;
            }
        }

        clearVisitedNodes(context);
        clearHighlightedNodes(context);
        clearHighlightedEdges(context);
        frames.add(context);

        return frames;
    }

    private void timeOut(Node startNode, Graph graph) {
        startNode.visited = true;

        startNode.highlighted = true;
        frames.add(graph);//for animation

        Collection<Edge> edgeList = graph.getEdgesFromNode(startNode);
        Node nextNode = null;
        for (Edge currentEdge : edgeList) {

            currentEdge.highlighted = true;
            frames.add(graph);

            nextNode = graph.getNode(currentEdge.getTarget());
            if (!nextNode.visited) {
                timeOut(nextNode, graph);
            }
        }
        timeOutList.add(nextNode);
    }

    /**
     * @param graph
     * @implNote Разворачивает ребра графа
     */
    private void reverseGraph(Graph graph) {
        Node from, to;
        for (Edge current : graph.getEdges()) {
            current.invert();
            current.highlighted = true;
            frames.add(graph);//for animation
        }
    }

    private void findComponent(int componentId, Node node, Graph graph) {

        node.strongComponentId = componentId;
        node.visited = true;

        node.highlighted = true;
        frames.add(graph);

        Collection<Edge> edgeList = graph.getEdgesFromNode(node);
        for (Edge currentEdge : edgeList) {
            currentEdge.highlighted = true;
            frames.add(graph);
            Node nextNode = graph.getNode(currentEdge.getTarget());
            if (nextNode.strongComponentId == -1) {
                findComponent(componentId, nextNode, graph);
            }
        }
    }

    private void clearVisitedNodes(Graph graph) {
        for (Node node : graph.getNodes()) {
            node.visited = false;
        }
    }

    private void clearHighlightedNodes(Graph graph) {
        for (Node node : graph.getNodes()) {
            node.highlighted = false;
        }
    }

    private void clearHighlightedEdges(Graph graph) {
        for (Edge edge : graph.getEdges()) {
            edge.highlighted = false;
        }
    }
}