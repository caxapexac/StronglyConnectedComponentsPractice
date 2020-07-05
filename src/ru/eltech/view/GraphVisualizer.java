package ru.eltech.view;

import ru.eltech.logic.Edge;
import ru.eltech.logic.Graph;
import ru.eltech.logic.Node;

import javax.swing.*;
import java.awt.*;

public class GraphVisualizer extends JPanel {
    /**
     * Стандартый стиль линии
     */
    protected static final BasicStroke DEFAULT_STROKE = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    protected Graph renderGraph = new Graph();

    /**
     * Заменяет текущий граф на содержимое переданного графа
     *
     * @param renderGraph граф, который требуется визуализировать
     */
    public void setRenderGraph(Graph renderGraph) {
        this.renderGraph.set(renderGraph);
        repaint();
    }

    /**
     * Заменяет содержимое переданного графа на текущий граф
     *
     * @param store контейнер для записи
     * @return ссылка на store
     */
    public Graph getGraph(Graph store) {
        return store.set(this.renderGraph);
    }

    /**
     * Отрисовка графа
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        for (Edge edge : renderGraph.getEdges()) {
            decorateEdge(g2d, edge);
            displayEdge(g2d, edge);
        }
        for (Node node : renderGraph.getNodes()) {
            decorateNode(g2d, node);
            displayNode(g2d, node);
        }
    }

    /**
     * Позволяет настроить стиль дуги в GraphEditor
     *
     * @param g
     * @param edge
     */
    protected void decorateEdge(Graphics2D g, Edge edge) {
        g.setColor(Color.BLACK);
        g.setStroke(DEFAULT_STROKE);
    }

    private void displayEdge(Graphics2D g, Edge edge) {
        Node source = renderGraph.getNode(edge.source);
        Node target = renderGraph.getNode(edge.target);
        g.drawLine(source.position.x, source.position.y, target.position.x, target.position.y);
    }

    /**
     * Позволяет настроить стиль ноды в GraphEditor
     *
     * @param g
     * @param node
     */
    protected void decorateNode(Graphics2D g, Node node) {
        g.setColor(Color.BLACK);
        g.setStroke(DEFAULT_STROKE);
    }

    private void displayNode(Graphics2D g, Node node) {
        int radius = node.radius;
        int diametr = radius * 2;
        g.drawArc(node.position.x - radius, node.position.y - radius, diametr, diametr, 0, 360);
    }
}
