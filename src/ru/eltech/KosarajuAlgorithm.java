package ru.eltech;

import java.util.ArrayList;
import java.util.Collection;

public class KosarajuAlgorithm implements Algorithm {

    private ArrayList<Node> timeOutList;
    private ArrayList<ArrayList<Node>> components;
    private FrameList frames;

    public KosarajuAlgorithm() {
        //вершины по времени выхода в порядке возрастания
        timeOutList = new ArrayList<Node>();
        //список найденных компонент
        components = new ArrayList<>();
        //возвращаемые фреймы
        frames = new FrameList();
    }

    @Override
    public FrameList process(Graph context) {
        //copy graph (not to invert context)
        //TODO

        //sort nodes by timeout
        //result goes to global "timeOutList"
        //frames are created by timeOut() and written down to global "frames"
        Collection<Node> nodes = context.getNodes();
        for(Node node : nodes) {
            if(!node.isVisited()) {
                timeOut(node);
            }
        }

        //TODO
        //сброс выделения всех нод и ребер
        //Возможно это должен быть метод самого графа

        //this func also creates frames and writes them to global "frames"
        reverseGraph(context);

        //TODO
        //сброс выделения всех нод и ребер
        //Возможно это должен быть метод самого графа

        //found components are stored in global "components"
        //findComponent() also creates frames and store them in "frames"
        for(Node currentNode : timeOutList) {
            if(!currentNode.isInComponent()) {
                ArrayList<Node> newComponent = new ArrayList<>();
                findComponent(newComponent, currentNode, context);
                components.add(newComponent);
            }
        }
        //returns global "frames"
        return frames;
    }


    //первый обход находит время выхода
    private void timeOut(Node startNode, Graph graph) {
        startNode.setVisited(true);
        frames.add(graph);//for animation
        ArrayList<Edge> edgeList = graph.findAllEdgesWithSourceNode(startNode.getId());
        Node nextNode = null;
        for(Edge currentEdge : edgeList) {
            //TODO
            //currentEdge.setHighlight(true);
            //frames.add(graph); for animation
            nextNode = currentEdge.getDestination();
            if (!nextNode.isVisited()) {
                timeOut(nextNode, graph);
            }
        }
        timeOutList.add(nextNode);
    }

    private void reverseGraph(Graph graph) {
        Node from, to;
        for(Edge current : graph.getEdges()) {
            from = current.getSource();
            to = current.getTarget();
            current.setSource(to);
            current.setTarget(from);
            frames.add(graph);//for animation
        }
    }

    //places all nodes in component containing given node in newComponent
    private void findComponent(ArrayList<Node> newComponent, Node node, Graph graph) {
        newComponent.add(node);
        //возможно, стоит раскрашивать компоненты в разные цвета, тогда нода
        //должна хранить свою принадлежность компоненте
        node.setVisited();//for animation
        frames.add(graph);//for animation
        ArrayList<Edge> edgeList = graph.findAllEdgesWithSourceNode(node.getId());
        for(Edge currentEdge : edgeList) {
            //TODO
            //currentEdge.setHighlight(true);
            //frames.add(graph); for animation
            Node nextNode = currentEdge.getDestination();
            if (!nextNode.isInComponent()) {
                findComponent(newComponent, nextNode, graph);
            }
        }
    }


}