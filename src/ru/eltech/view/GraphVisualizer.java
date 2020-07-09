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
    /**
     * Шрифт для отображения имён нод
     */
    protected static final Font font = new Font(Font.DIALOG, Font.BOLD, 24);
    /**
     * Шрифт для отображения времени выхода из ноды
     */
    protected static final Font fontAdditional = new Font(Font.DIALOG, Font.ITALIC, 12);

    protected Graph graph = new Graph();

    /**
     * Заменяет текущий граф на копию переданного графа
     *
     * @param graph граф, который требуется визуализировать
     */
    public void setGraphCopy(Graph graph) {
        this.graph = new Graph(graph);
        repaint();
    }

    /**
     * @return копия текущего графа
     */
    public Graph getGraphCopy() {
        return new Graph(graph);
    }

    /**
     * Отрисовка графа через {@link Graphics2D}
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        for (Edge edge : graph.getEdges()) {
            displayEdge(g2d, edge);
        }
        for (Node node : graph.getNodes()) {
            displayNode(g2d, node);
        }
    }

    /**
     * Позволяет настроить стиль тела дуги в {@link GraphEditor}
     */
    protected void decorateEdgeBody(Graphics2D g, Edge edge) {
        g.setColor(Color.BLACK);
        g.setStroke(DEFAULT_STROKE);
    }

    /**
     * Позволяет настроить стиль крыльев дуги в {@link GraphEditor}
     */
    protected void decorateEdgeArrow(Graphics2D g, Edge edge) {
        g.setColor(Color.BLACK);
        g.setStroke(DEFAULT_STROKE);
    }

    private void displayEdge(Graphics2D g, Edge edge) {
        Node source = graph.getNode(edge.getSource());
        Node target = graph.getNode(edge.getTarget());
        int dx = target.getPosition().x - source.getPosition().x;
        int dy = target.getPosition().y - source.getPosition().y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double vx = dx / distance;
        double vy = dy / distance;
        if (distance > EPSILON) {
            double startOffset = ARROW_OFFSET + source.getRadius();
            double endOffset = ARROW_OFFSET + target.getRadius();
            if (distance > startOffset + endOffset) {
                int startX = (int) Math.round(source.getPosition().x + vx * startOffset);
                int startY = (int) Math.round(source.getPosition().y + vy * startOffset);
                int endX = (int) Math.round(target.getPosition().x - vx * endOffset);
                int endY = (int) Math.round(target.getPosition().y - vy * endOffset);
                int leftX = (int) Math.round(endX - vy * ARROW_WIDTH - vx * ARROW_LENGTH);
                int leftY = (int) Math.round(endY + vx * ARROW_WIDTH - vy * ARROW_LENGTH);
                int rightX = (int) Math.round(endX + vy * ARROW_WIDTH - vx * ARROW_LENGTH);
                int rightY = (int) Math.round(endY - vx * ARROW_WIDTH - vy * ARROW_LENGTH);
                decorateEdgeBody(g, edge);
                g.drawLine(startX, startY, endX, endY); // Тело дуги
                decorateEdgeArrow(g, edge);
                g.drawLine(leftX, leftY, endX, endY); // Левое крыло стрелки
                g.drawLine(endX, endY, rightX, rightY); // Правое крыло стрелки
            } else {
                int endX = (int) Math.round((target.getPosition().x + source.getPosition().x) * 0.5d);
                int endY = (int) Math.round((target.getPosition().y + source.getPosition().y) * 0.5d);
                int leftX = (int) Math.round(endX - vy * ARROW_WIDTH - vx * ARROW_LENGTH);
                int leftY = (int) Math.round(endY + vx * ARROW_WIDTH - vy * ARROW_LENGTH);
                int rightX = (int) Math.round(endX + vy * ARROW_WIDTH - vx * ARROW_LENGTH);
                int rightY = (int) Math.round(endY - vx * ARROW_WIDTH - vy * ARROW_LENGTH);
                decorateEdgeArrow(g, edge);
                g.drawLine(leftX, leftY, endX, endY); // Левое крыло стрелки
                g.drawLine(endX, endY, rightX, rightY); // Правое крыло стрелки
            }
        }
    }


    /**
     * Позволяет настроить стиль заливки ноды в {@link GraphEditor}
     */
    protected void decorateNodeInner(Graphics2D g, Node node) {
        g.setColor(Color.GRAY);
        g.setStroke(DEFAULT_STROKE);
    }

    /**
     * Позволяет настроить стиль обводки ноды в {@link GraphEditor}
     */
    protected void decorateNodeOuter(Graphics2D g, Node node) {
        g.setColor(Color.BLACK);
        g.setStroke(DEFAULT_STROKE);
    }

    /**
     * Позволяет настроить стиль текста внутри ноды в {@link GraphEditor}
     */
    protected void decorateNodeText(Graphics2D g, Node node) {
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.setStroke(DEFAULT_STROKE);
    }

    protected void displayNode(Graphics2D g, Node node) {
        int radius = node.getRadius();
        int diameter = radius * 2;
        Polygon hex = createHexagon(node.getPosition(), radius);

        decorateNodeInner(g, node);
        //g.fillOval(node.getPosition().x - radius, node.getPosition().y - radius, diameter, diameter);
        g.fillPolygon(hex);
        decorateNodeOuter(g, node);
        //g.drawOval(node.getPosition().x - radius, node.getPosition().y - radius, diameter, diameter);
        g.drawPolygon(hex);
        decorateNodeText(g, node);
        FontMetrics fm = g.getFontMetrics();
        int tx = node.getPosition().x - fm.stringWidth(node.getName()) / 2;
        int ty = node.getPosition().y - fm.getHeight() / 2 + fm.getAscent();
        g.drawString(node.getName(), tx, ty);
    }

    private Polygon createHexagon(Point position, int radius) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < 6; i++) {
            int x = (int) (position.x + radius * Math.cos(i * 2 * Math.PI / 6D));
            int y = (int) (position.y + radius * Math.sin(i * 2 * Math.PI / 6D));
            polygon.addPoint(x, y);
        }
        return polygon;
    }
}