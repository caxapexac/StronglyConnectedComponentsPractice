package ru.eltech.logic;

import ru.eltech.view.MainWindow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class KosarajuAlgorithm implements Algorithm {

    private ArrayList<Node> timeOutList;
    private FrameList frames;
    private int timer = 0;
    private boolean immediateReverse = true;
    private String currentStep = "";

    public boolean isImmediateReverse() {
        return immediateReverse;
    }

    public void setImmediateReverse(boolean immediateReverse) {
        this.immediateReverse = immediateReverse;
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
        //вершины по времени выхода в порядке возрастания
        timeOutList = new ArrayList<>();
        //возвращаемые фреймы
        frames = new FrameList();
        frames.add(context, "");

        timer = 0;
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

        clearAll(context);
        frames.add(context, "Снятие всех выделений, переход к следующему шагу.<br>");

        reverseGraph(context, immediateReverse);

        clearHighlightedEdges(context);
        frames.add(context, "Снятие всех выделений, переход к следующему шагу.<br>");

        int currentComponentId = 0;
        for (Node currentNode : timeOutList) {
            if (currentNode.strongComponentId == -1) {
                findComponent(currentComponentId, currentNode, context);
                ++currentComponentId;
            }
        }

        clearAll(context);
        frames.add(context, "Снятие всех выделений, переход к следующему шагу.<br>");

        reverseGraph(context, immediateReverse);

        clearHighlightedEdges(context);
        frames.add(context, "Снятие всех выделений, завершение алгоритма.<br>" +
                "Алгоритм закончен. " + frames.count() + " итераций");

        String components = "Найдены компоненты со следующими вершинами:<br>";
        for (int i = 0; context.componentExists(i);i++){
            components += context.getNodesInComponentById(i) + "<br>";
        }
        frames.add(context, components + "<br>");
        //MainWindow.log.info("Просчет алгоритма закончен. " + frames.count() + " итераций");
        return frames;
    }

    /**
     * @param startNode
     * @param graph
     * @implNote Первый обход dfs
     */
    private void timeOut(Node startNode, Graph graph) {
        startNode.visited = true;
        timer++;

        startNode.highlighted = true;
        currentStep += "<br>Подсчет времени выхода<br>Текущая вершина: " + startNode.getName() + "<br>";
        frames.add(graph, currentStep);//for animation

        Collection<Edge> edgeList = graph.getEdgesFromNode(startNode);
        Node nextNode = null;
        currentStep = "";
        for (Edge currentEdge : edgeList) {

            currentEdge.highlighted = true;
            nextNode = graph.getNode(currentEdge.getTarget());
            currentStep += "Переход в вершину " + nextNode.getName() + "<br>";
            frames.add(graph, currentStep);
            currentStep = "";

            if (nextNode.visited) {
                frames.add(graph, "Вершина " + nextNode.getName() + " уже посещена<br>");
            } else {
                currentStep += "Вершина " + nextNode.getName() + " не посещена,&#10;&#13; рекурсивно обходим ее<br>";
                timeOut(nextNode, graph);
            }
        }
        startNode.timeOut = timer;
        currentStep = "Выход из вызова функции обхода&#10;&#13; для вершины " + startNode.getName() + "<br>" +
                "Вершина добавлена в список &#10;&#13;для поиска компонент, ее время выхода " +
                timer + "<br>";
        frames.add(graph, currentStep);

        timeOutList.add(startNode);
        currentStep = "";
        timer++;
    }

    /**
     * @param graph
     * @implNote Разворачивает ребра графа
     */
    private void reverseGraph(Graph graph, boolean inOneStep) {

        if (inOneStep) {
            highlightAllEdges(graph);
            frames.add(graph, "Инвертирование всех ребер");
            for (Edge current : graph.getEdges()) {
                current.invert();
            }
            frames.add(graph);
            clearHighlightedEdges(graph);
            frames.add(graph);
            return;
        }
        for (Edge current : graph.getEdges()) {
            current.highlighted = true;
            String from = graph.getNode(current.getSource()).getName();
            String to = graph.getNode(current.getTarget()).getName();
            frames.add(graph, "Инвертирование ребра из " + from + " в " + to + "<br>");//for animation
            current.invert();
            frames.add(graph);
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
        frames.add(graph,"Поиск компоненты сильной связности с id " + componentId);

        Collection<Edge> edgeList = graph.getEdgesFromNode(node);
        for (Edge currentEdge : edgeList) {

            currentEdge.highlighted = true;
            Node nextNode = graph.getNode(currentEdge.getTarget());
            frames.add(graph, "Переход в вершину " +
                    graph.getNode(currentEdge.getTarget()).getName() + "<br>");

            if (nextNode.strongComponentId == -1) {
                frames.add(graph, "Вершина " + nextNode.getName() + " еще не в компоненте"+
                        ". Выполняется рекурсивный вызов функции поиска компоненты для вершины " +
                        nextNode.getName() + "<br>");
                findComponent(componentId, nextNode, graph);
//                frames.add(graph, "Найдена компонента сильной связности с id "+nextNode.strongComponentId+
//                        "<br>В нее входят вершины " + graph.getNodesInComponentById(nextNode.strongComponentId) + "<br>");
            } else if (nextNode.strongComponentId != node.strongComponentId) {
                currentEdge.highlighted = false;
                currentEdge.connectsStrongComponents = true;
                frames.add(graph,"Вершины " + node.getName() + " и " + nextNode.getName() +
                        " в разных компонентах, выделяем ребро между ними.<br>");
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

    /**
     * @param graph
     * @implNote Стирает все выделения, отмечает все вершины как непосещенные
     */
    private void clearAll(Graph graph) {
        clearVisitedNodes(graph);
        clearHighlightedNodes(graph);
        clearHighlightedEdges(graph);
    }
}