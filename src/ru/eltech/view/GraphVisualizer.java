package ru.eltech.view;

import ru.eltech.logic.Edge;
import ru.eltech.logic.Graph;
import ru.eltech.logic.Node;

import javax.swing.*;
import java.awt.*;

/**
 * Класс, отвечающий за логику отображения графа
 */
public class GraphVisualizer extends JPanel {
    /**
     * Стандартный стиль линии
     */
    protected static final BasicStroke DEFAULT_STROKE = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    /**
     * Отступ между дугой и нодой
     */
    protected static final double ARROW_OFFSET = 10f;
    /**
     * Ширина крыльев дуги
     */
    protected static final double ARROW_WIDTH = 5f;
    /**
     * Длина тела дуги
     */
    protected static final double ARROW_LENGTH = 15f;
    /**
     * Расстояние, после которого у дуги перестаёт отображаться тело
     */
    protected static final double EPSILON = 0.0000000001d;

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
     * Отрисовка графа через {@link Graphics2D}
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
     * Позволяет настроить стиль дуги в {@link GraphEditor}
     */
    protected void decorateEdge(Graphics2D g, Edge edge) {
        g.setColor(Color.BLACK);
        g.setStroke(DEFAULT_STROKE);
    }

    private void displayEdge(Graphics2D g, Edge edge) {
        Node source = renderGraph.getNode(edge.source);
        Node target = renderGraph.getNode(edge.target);
        int dx = target.position.x - source.position.x;
        int dy = target.position.y - source.position.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double vx = dx / distance;
        double vy = dy / distance;
        if (distance > EPSILON) {
            double startOffset = ARROW_OFFSET + source.radius;
            double endOffset = ARROW_OFFSET + target.radius;
            if (distance > startOffset + endOffset) {
                int startX = (int) Math.round(source.position.x + vx * startOffset);
                int startY = (int) Math.round(source.position.y + vy * startOffset);
                int endX = (int) Math.round(target.position.x - vx * endOffset);
                int endY = (int) Math.round(target.position.y - vy * endOffset);
                int leftX = (int) Math.round(endX - vy * ARROW_WIDTH - vx * ARROW_LENGTH);
                int leftY = (int) Math.round(endY + vx * ARROW_WIDTH - vy * ARROW_LENGTH);
                int rightX = (int) Math.round(endX + vy * ARROW_WIDTH - vx * ARROW_LENGTH);
                int rightY = (int) Math.round(endY - vx * ARROW_WIDTH - vy * ARROW_LENGTH);
                g.drawLine(startX, startY, endX, endY); // Тело дуги
                g.drawLine(leftX, leftY, endX, endY); // Левое крыло стрелки
                g.drawLine(endX, endY, rightX, rightY); // Правое крыло стрелки
            } else {
                int endX = (int) Math.round((target.position.x + source.position.x) * 0.5d);
                int endY = (int) Math.round((target.position.y + source.position.y) * 0.5d);
                int leftX = (int) Math.round(endX - vy * ARROW_WIDTH - vx * ARROW_LENGTH);
                int leftY = (int) Math.round(endY + vx * ARROW_WIDTH - vy * ARROW_LENGTH);
                int rightX = (int) Math.round(endX + vy * ARROW_WIDTH - vx * ARROW_LENGTH);
                int rightY = (int) Math.round(endY - vx * ARROW_WIDTH - vy * ARROW_LENGTH);
                g.drawLine(leftX, leftY, endX, endY); // Левое крыло стрелки
                g.drawLine(endX, endY, rightX, rightY); // Правое крыло стрелки
            }
        }
    }


    /**
     * Позволяет настроить стиль ноды в {@link GraphEditor}
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
