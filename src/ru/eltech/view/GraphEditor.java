package ru.eltech.view;

import ru.eltech.logic.Edge;
import ru.eltech.logic.Graph;
import ru.eltech.logic.Node;

import javax.swing.*;
import java.awt.*;

/**
 * Класс, отвечающий за логику редактирования графа
 *
 * @author caxap
 */
public final class GraphEditor extends GraphVisualizer {
    /**
     * Ширина коллайдера дуги
     */
    private static final int EDGE_DRAG_DISTANCE = 20;
    /**
     * Стиль выделенной дуги
     */
    private static final BasicStroke SELECTED_STROKE = new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    /**
     * Стиль создаваемой дуги
     */
    private static final BasicStroke CONNECTING_STROKE = new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, new float[]{5, 10}, 0);

    public boolean isReadOnly = false;
    public boolean isModified = false;
    private final Point draggingLast = new Point();
    private final Point connectingLast = new Point();
    private Node draggingNode;
    private Node selectedNode;
    private Edge draggingEdge;
    private Edge selectedEdge;
    private Node connectingSourceNode;

    public GraphEditor() {
        addMouseListener(new GraphEditorMouseListener());
        addMouseMotionListener(new GraphEditorMouseMotionListener());
        addKeyListener(new GraphEditorKeyListener());
    }

    @Override
    public void setGraphCopy(Graph renderGraph) {
        draggingNode = null;
        selectedNode = null;
        draggingEdge = null;
        selectedEdge = null;
        connectingSourceNode = null;
        super.setGraphCopy(renderGraph);
        isModified = false;
    }

    /**
     * @return Имеет ли граф выделенные ноды/дуги
     */
    public boolean hasSelected() {
        return selectedNode != null || selectedEdge != null;
    }

    /**
     * @return Имеет ли граф создаваемую дугу
     */
    public boolean isConnecting() {
        return connectingSourceNode != null;
    }

    /**
     * Выделяет ноду или дугу, нода в приоритете
     *
     * @return выделилось ли что-либо
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean select(int x, int y) {
        if (innerSelectNode(x, y)) {
            selectedEdge = null;
            repaint();
            return true;
        } else if (selectedNode == null && innerSelectEdge(x, y)) {
            repaint();
            return true;
        }
        return false;
    }

    private boolean innerSelectNode(int x, int y) {
        Node current = selectedNode;
        selectedNode = innerFindNode(x, y);
        return current != selectedNode;
    }

    private boolean innerSelectEdge(int x, int y) {
        Edge current = selectedEdge;
        selectedEdge = innerFindEdge(x, y);
        return current != selectedEdge;
    }

    /**
     * Инициирует перемещение выделенного объекта
     */
    public void startDrag(int x, int y) {
        draggingLast.move(x, y);
        draggingNode = selectedNode;
        draggingEdge = selectedEdge;
    }

    /**
     * Перемещает выделенный объект, нода в приоритете
     */
    public void drag(int x, int y) {
        int mouseDX = x - draggingLast.x;
        int mouseDY = y - draggingLast.y;

        if (draggingNode != null) {
            draggingNode.getPosition().translate(mouseDX, mouseDY);
            repaint();
        } else if (draggingEdge != null) {
            Node fromNode = graph.getNode(draggingEdge.getSource());
            Node toNode = graph.getNode(draggingEdge.getTarget());
            fromNode.getPosition().translate(mouseDX, mouseDY);
            toNode.getPosition().translate(mouseDX, mouseDY);
            repaint();
        } else {
            // Drag screen TODO (need additional fix)
        }

        draggingLast.move(x, y);
    }

    /**
     * Завершает процесс перемещения объекта
     */
    public void endDrag(int x, int y) {
        drag(x, y);
        draggingNode = null;
        draggingEdge = null;
    }

    /**
     * Начало создания дуги. От ноды под координаитой x,y до координат мыши
     */
    public void startConnecting(int x, int y) {
        connectingSourceNode = innerFindNode(x, y);
        connectingLast.move(x, y);
    }

    /**
     * Обновляет позицию создаваемой дуги, если она существует
     */
    public void connecting(int x, int y) {
        if (connectingSourceNode == null) return;
        connectingLast.move(x, y);
        repaint();
    }

    /**
     * Завершает создание дуги в точке x,y
     *
     * @param x                x
     * @param y                y
     * @param willCreateNode   будет ли создаваться новая нода, если на координатах x,y нет ноды
     * @param willDestroyClone будет ли удаляться дуга, если происходит попытка создать совпадающую с ней дугу
     */
    public void endConnecting(int x, int y, boolean willCreateNode, boolean willDestroyClone) {
        if (connectingSourceNode == null) return;
        Node node = innerFindNode(x, y);
        if (node != connectingSourceNode) {
            if (node != null) {
                Edge currentEdge = graph.getEdge(connectingSourceNode, node, true);
                if (currentEdge != null) {
                    if (willDestroyClone) graph.destroyEdge(currentEdge);
                } else
                    graph.createEdge(connectingSourceNode, node);
            } else if (willCreateNode) {
                Node newNode = graph.createNode(x, y);
                graph.createEdge(connectingSourceNode, newNode);
            }
        }
        connectingSourceNode = null;
        repaint();
    }

    /**
     * Создаёт ноду на координатах x,y
     */
    public void createNode(int x, int y) {
        graph.createNode(x, y);
        repaint();
    }

    /**
     * Удаляет выделенный объект
     */
    public void destroySelected() {
        if (innerDestroySelectedNode() | innerDestroySelectedEdge()) repaint();
    }

    private boolean innerDestroySelectedNode() {
        if (selectedNode == null) return false;
        graph.destroyNode(selectedNode);
        if (draggingNode == selectedNode) draggingNode = null;
        if (connectingSourceNode == selectedNode) connectingSourceNode = null;
        if (selectedEdge != null && !graph.containsEdge(selectedEdge)) selectedEdge = null;
        selectedNode = null;
        return true;
    }

    private boolean innerDestroySelectedEdge() {
        if (selectedEdge == null) return false;
        graph.destroyEdge(selectedEdge);
        if (draggingEdge == selectedEdge) draggingEdge = null;
        selectedEdge = null;
        return true;
    }

    /**
     * Поиск ноды по координатам x,y
     *
     * @return id найденной ноды, иначе null
     */
    public Integer findNode(int x, int y) {
        Node node = innerFindNode(x, y);
        return node != null ? node.id : null;
    }

    private Node innerFindNode(int x, int y) {
        Node foundNode = null;
        int foundSqrDist = 0;
        for (Node node : graph.getNodes()) {
            int sqrDist = (int) Point.distanceSq(node.getPosition().x, node.getPosition().y, x, y);
            if (sqrDist <= node.getRadius() * node.getRadius() && (foundNode == null || foundSqrDist > sqrDist)) {
                foundNode = node;
                foundSqrDist = sqrDist;
            }
        }
        return foundNode;
    }

    /**
     * Поиск дуги по координатам x,y
     *
     * @return id найденной дуги, иначе null
     */
    public Integer findEdge(int x, int y) {
        Edge edge = innerFindEdge(x, y);
        return edge != null ? edge.id : null;
    }

    private Edge innerFindEdge(int x, int y) {
        Edge foundEdge = null;
        double foundDist = 0;

        for (Edge edge : graph.getEdges()) {
            Node source = graph.getNode(edge.getSource());
            Node target = graph.getNode(edge.getTarget());
            int dx = target.getX() - source.getX();
            int dy = target.getY() - source.getY();
            double dist = Math.sqrt(dx * dx + dy * dy);
            double vx = dx / dist;
            double vy = dy / dist;
            double lx = vx * (x - source.getX()) + vy * (y - source.getY());
            if (lx < 0 || lx > dist) continue;
            double ly = vy * (x - source.getX()) - vx * (y - source.getY());
            double distToLine = Math.abs(ly);
            if (distToLine > EDGE_DRAG_DISTANCE) continue;
            if (foundEdge == null || distToLine < foundDist) {
                foundEdge = edge;
                foundDist = distToLine;
            }
        }
        return foundEdge;
    }

    @Override
    public void repaint() {
        isModified = true;
        super.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (connectingSourceNode != null) {
            g2d.setColor(Color.GREEN);
            g2d.setStroke(CONNECTING_STROKE);
            g2d.drawLine(connectingSourceNode.getPosition().x, connectingSourceNode.getPosition().y, connectingLast.x, connectingLast.y);
        }
        if (isModified) {
            g2d.drawString("*", 10, 10);
        }
    }

    @Override
    protected void decorateEdgeBody(Graphics2D g, Edge edge) {
        super.decorateEdgeBody(g, edge);
        if (draggingEdge == edge) {
            g.setStroke(SELECTED_STROKE);
        }
        if (selectedEdge == edge) {
            g.setColor(Color.GREEN);
        }
        if (isReadOnly) {
            if (edge.highlighted) {
                g.setColor(Color.ORANGE);
            }
        }
    }

    @Override
    protected void decorateEdgeArrow(Graphics2D g, Edge edge) {
        super.decorateEdgeArrow(g, edge);
        if (isReadOnly) {
            if (edge.highlighted) {
                g.setColor(Color.ORANGE);
            }
        }
    }

    @Override
    protected void decorateNodeInner(Graphics2D g, Node node) {
        super.decorateNodeInner(g, node);
        if (isReadOnly) {
            if (node.visited) {
                g.setColor(Color.LIGHT_GRAY);
            }
            if (node.strongComponentId != -1) {
                g.setColor(NodeColors.colors[node.strongComponentId % NodeColors.colors.length]);
            }
        }
    }

    @Override
    protected void decorateNodeOuter(Graphics2D g, Node node) {
        super.decorateNodeOuter(g, node);
        if (draggingNode == node) {
            g.setStroke(SELECTED_STROKE);
        }
        if (selectedNode == node) {
            g.setColor(Color.GREEN);
        }
        if (isReadOnly) {
            if (node.highlighted) {
                g.setColor(Color.ORANGE);
            }
        }
    }

    @Override
    protected void decorateNodeText(Graphics2D g, Node node) {
        super.decorateNodeText(g, node);
    }

    @Override
    protected void displayNode(Graphics2D g, Node node) {
        super.displayNode(g, node);
        if (isReadOnly) {
            g.setFont(fontAdditional);
            g.setColor(Color.BLACK);
            g.setStroke(DEFAULT_STROKE);
            FontMetrics fm = g.getFontMetrics();
            int tx = node.getPosition().x - fm.stringWidth(node.getName()) / 2;
            int ty = node.getPosition().y + fm.getHeight() / 2 + fm.getAscent();
            g.drawString(Integer.toString(node.timeOut), tx, ty);
        }
    }

    //region ACTIONS POPUP

    public void destroyEdge(Integer id) {
        Edge edge = graph.getEdge(id);
        if (edge != null) graph.destroyEdge(edge);
        repaint();
    }

    public void changeEdgeStroke(Integer id) {
        //Object[] radiusValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        //int radius = (Integer) JOptionPane.showInputDialog(this, "Введите толщину", "Модификация", JOptionPane.PLAIN_MESSAGE, null, radiusValues, radiusValues[radiusValues.length / 2]);
        // TODO
    }

    public void changeEdgeColor(Integer id) {
        // Color color = JColorChooser.showDialog(this, "Модификация", Color.BLACK);
        // TODO
    }

    public void createNewNode(int x, int y) {
        graph.createNode(x, y);
        repaint();
    }

    public void clearGraph() {
        graph.clear();
        repaint();
    }

    public void removeNode(Integer id) {
        Node node = graph.getNode(id);
        if (node != null) graph.destroyNode(node);
    }

    public void initializeAddEdge(Integer id) {
        // TODO
    }

    public void changeNodeRadius(Integer id) {
        //Object[] radiusValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        //int radius = (Integer) JOptionPane.showInputDialog(this, "Введите радиус", "Модификация", JOptionPane.PLAIN_MESSAGE, null, radiusValues, radiusValues[radiusValues.length / 2]);
        // TODO
    }

    public void changeNodeColor(Integer id) {
        // Color color = JColorChooser.showDialog(this, "Модификация", Color.BLACK);
        // TODO
    }

    public void changeNodeText(Integer id) {
        //String name = JOptionPane.showInputDialog(this, "Введите новое имя", "Модификация", JOptionPane.QUESTION_MESSAGE);
        // TODO
    }

    //endregion

    //region ACTIONS KEYSTROKE

    public void moveGraphStep(int x, int y) {
        drag(x, y);
        // TODO?
    }

    public void setColor(Color color) {
        // TODO
    }

    public void changeSize(int delta) {
        // TODO?
    }

    //endregion
}

