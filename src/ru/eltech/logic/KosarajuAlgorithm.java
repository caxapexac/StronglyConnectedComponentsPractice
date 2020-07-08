package ru.eltech.logic;

import ru.eltech.view.MainWindow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class KosarajuAlgorithm implements Algorithm {

    private ArrayList<Node> timeOutList;
    private FrameList frames;
    //private int timer = 0;

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
        timeOutList = new ArrayList<>();
        frames = new FrameList();
        frames.add(context);

        Collection<Node> nodes = context.getNodes();
        for (Node node : nodes) {
            if (!node.visited) {
                timeOut(node, context);
            }
        }
        Collections.reverse(timeOutList);

        //just in case
        if (timeOutList.size() != context.getNodesCount()) {
            MainWindow.log.severe("timeoutsize " + timeOutList.size() + " nodes count " + context.getNodesCount());
        }

        clearVisitedNodes(context);
        clearHighlightedNodes(context);
        clearHighlightedEdges(context);

        reverseGraph(context, true);

        clearHighlightedEdges(context);

        int currentComponentId = 0;
        for (Node currentNode : timeOutList) {
            if (currentNode.strongComponentId == -1) {
                findComponent(currentComponentId, currentNode, context);
                ++currentComponentId;
            }
        }

        //сбрасываем все выделения в конце алгоритма
        clearVisitedNodes(context);
        clearHighlightedNodes(context);
        clearHighlightedEdges(context);
        frames.add(context);

        //возвращаем граф к исходному состоянию за три кадра
        highlightAllEdges(context);
        frames.add(context);
        reverseGraph(context, false);
        frames.add(context);
        clearHighlightedEdges(context);
        frames.add(context);

        MainWindow.log.info("Алгоритм закончен. " + frames.count() + " итераций");
        return frames;
    }

    /**
     * @param startNode
     * @param graph
     * @implNote Первый обход dfs
     */
    private void timeOut(Node startNode, Graph graph) {
        startNode.visited = true;
        //timer++;

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
                //MainWindow.log.info("added " + Integer.toString(timer));
            }
        }
        timeOutList.add(startNode);
        //timer++;
    }

    /**
     * @param graph
     * @implNote Разворачивает ребра графа
     */
    private void reverseGraph(Graph graph, boolean animate) {
        for (Edge current : graph.getEdges()) {
            if (animate) {
                current.highlighted = true;
                frames.add(graph);//for animation
            }

            current.invert();

            if (animate) {
                frames.add(graph);
            }
        }
    }

    /**
     * @param componentId
     * @param node
     * @param graph
     * @implNote Второй обход dfs
     */
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
            } else if (nextNode.strongComponentId != node.strongComponentId) {
                currentEdge.highlighted = false;
                frames.add(graph);
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

    private void highlightAllEdges(Graph graph) {
        for (Edge edge :
                graph.getEdges()) {
            edge.highlighted = true;
        }
    }
}