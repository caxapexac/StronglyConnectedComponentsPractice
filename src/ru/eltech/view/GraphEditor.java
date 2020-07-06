package ru.eltech.view;

import ru.eltech.logic.Edge;
import ru.eltech.logic.Graph;
import ru.eltech.logic.Node;

import java.awt.*;

/**
 * Класс, отвечающий за логику редактирования графа
 * @author caxap
 */
public class GraphEditor extends GraphVisualizer {
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
    public void setRenderGraph(Graph renderGraph) {
        super.setRenderGraph(renderGraph);
        draggingNode = null;
        selectedNode = null;
        draggingEdge = null;
        selectedEdge = null;
        connectingSourceNode = null;
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
            draggingNode.position.translate(mouseDX, mouseDY);
            repaint();
        } else if (draggingEdge != null) {
            Node fromNode = renderGraph.getNode(draggingEdge.source);
            Node toNode = renderGraph.getNode(draggingEdge.target);
            fromNode.position.translate(mouseDX, mouseDY);
            toNode.position.translate(mouseDX, mouseDY);
            repaint();
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
                Edge currentEdge = renderGraph.getEdge(connectingSourceNode, node, true);
                if (currentEdge != null) {
                    if (willDestroyClone) renderGraph.destroyEdge(currentEdge);
                } else
                    renderGraph.createEdge(connectingSourceNode, node);
            } else if (willCreateNode) {
                Node newNode = renderGraph.createNode(x, y);
                renderGraph.createEdge(connectingSourceNode, newNode);
            }
        }
        connectingSourceNode = null;
        repaint();
    }

    /**
     * Создаёт ноду на координатах x,y
     */
    public void createNode(int x, int y) {
        renderGraph.createNode(x, y);
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
        renderGraph.destroyNode(selectedNode);
        if (draggingNode == selectedNode) draggingNode = null;
        if (connectingSourceNode == selectedNode) connectingSourceNode = null;
        if (selectedEdge != null && !renderGraph.containsEdge(selectedEdge)) selectedEdge = null;
        selectedNode = null;
        return true;
    }

    private boolean innerDestroySelectedEdge() {
        if (selectedEdge == null) return false;
        renderGraph.destroyEdge(selectedEdge);
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
        for (Node node : renderGraph.getNodes()) {
            int sqrDist = (int) Point.distanceSq(node.position.x, node.position.y, x, y);
            if (sqrDist <= node.radius * node.radius && (foundNode == null || foundSqrDist > sqrDist)) {
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

        for (Edge edge : renderGraph.getEdges()) {
            Node source = renderGraph.getNode(edge.source);
            Node target = renderGraph.getNode(edge.target);
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
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (connectingSourceNode != null) {
            g2d.setColor(Color.GREEN);
            g2d.setStroke(CONNECTING_STROKE);
            g2d.drawLine(connectingSourceNode.position.x, connectingSourceNode.position.y, connectingLast.x, connectingLast.y);
        }
    }

    @Override
    protected void decorateNode(Graphics2D g, Node node) {
        super.decorateNode(g, node);
        if (draggingNode == node) {
            g.setStroke(SELECTED_STROKE);
        }
        if (selectedNode == node) {
            g.setColor(Color.GREEN);
        }
    }

    @Override
    protected void decorateEdge(Graphics2D g, Edge edge) {
        super.decorateEdge(g, edge);
        if (draggingEdge == edge) {
            g.setStroke(SELECTED_STROKE);
        }
        if (selectedEdge == edge) {
            g.setColor(Color.GREEN);
        }
    }

    //region ACTIONS POPUP

    public void destroyEdge(Integer id) {
        // TODO
    }

    public void changeEdgeStroke(Integer id) {
        // TODO
    }

    public void changeEdgeColor(Integer id) {
        // TODO
    }

    public void createNewNode(int x, int y) {
        // TODO
    }

    public void clearGraph() {
        // TODO
    }

    public void removeNode(Integer id) {
        // TODO
    }

    public void initializeAddEdge(Integer id) {
        // TODO
    }

    public void changeNodeRadius(Integer id) {
        // TODO
    }

    public void changeNodeColor(Integer id) {
        // TODO
    }

    public void changeNodeText(Integer id) {
        // TODO
    }

    //endregion

    //region ACTIONS KEYSTROKE

    public void moveGraphStep(int x, int y) {
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



