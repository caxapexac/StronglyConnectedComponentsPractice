package ru.eltech.view;

import ru.eltech.logic.Edge;
import ru.eltech.logic.Graph;
import ru.eltech.logic.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphEditor extends GraphVisualizer {
    private static final int EDGE_DRAG_DISTANCE = 20;

    private Point draggingLast = new Point();
    private Point connectingLast = new Point();

    private Node draggingNode;
    private Node selectedNode;
    private Edge draggingEdge;
    private Edge selectedEdge;
    private Node connectedNode;

    private static final BasicStroke SELECTED_STROKE = new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final BasicStroke CONNECTING_STROKE = new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, new float[]{5, 10}, 0);

    public GraphEditor() {
        addMouseListener(new GraphEditorMouseListener());
        addMouseMotionListener(new GraphEditorMouseMotionListener());
        addKeyListener(new GraphEditorKeyListener());
    }

    /**
     * Имеет ли граф выделенные ноды/дуги
     * @return
     */
    public boolean hasSelected() {
        return selectedNode != null || selectedEdge != null;
    }

    /**
     * Выделяет ноду или дугу, нода в приоритете
     * @param x
     * @param y
     * @return выделилось ли что-либо
     */
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
     * @param x
     * @param y
     */
    public void startDrag(int x, int y) {
        draggingLast.move(x, y);
        draggingNode = selectedNode;
        draggingEdge = selectedEdge;
    }

    /**
     * Перемещает выделенный объект, нода в приоритете
     * @param x
     * @param y
     */
    public void drag(int x, int y) {
        int mouseDX = x - draggingLast.x;
        int mouseDY = y - draggingLast.y;

        if (draggingNode != null) {
            draggingNode.position.translate(mouseDX, mouseDY);
            repaint();
        }
        else if (draggingEdge != null) {
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
     * @param x
     * @param y
     */
    public void endDrag(int x, int y) {
        drag(x, y);
        draggingNode = null;
        draggingEdge = null;
    }

    /**
     * Начало создания дуги. От ноды под координаитой x,y до координат мыши
     * @param x
     * @param y
     */
    public void startConnecting(int x, int y) {
        connectedNode = innerFindNode(x, y);
        connectingLast.move(x, y);
    }

    /**
     * Обновляет позицию создаваемой дуги, если она существует
     * @param x
     * @param y
     */
    public void connecting(int x, int y) {
        if (connectedNode == null) return;
        connectingLast.move(x, y);
        repaint();
    }

    /**
     * Завершает создание дуги в точке x,y
     * @param x
     * @param y
     * @param willCreateNode будет ли создаваться новая нода, если на координатах x,y нет ноды
     * @param willDestroyClone будет ли удаляться дуга, если происходит попытка создать совпадающую с ней дугу
     */
    public void endConnecting(int x, int y, boolean willCreateNode, boolean willDestroyClone) {
        if (connectedNode == null) return;
        Node node = innerFindNode(x, y);
        if (node != null) {
            Edge currentEdge = renderGraph.findEdge(connectedNode, node);
            if (currentEdge != null) {
                if (willDestroyClone) renderGraph.destroyEdge(currentEdge);
            } else
                renderGraph.createEdge(connectedNode, node);
        } else if (willCreateNode) {
            Node newNode = renderGraph.createNode().setXY(x, y);
            renderGraph.createEdge(connectedNode, newNode);
        }

        connectedNode = null;
        repaint();
    }

    /**
     * Создаёт ноду на координатах x,y
     * @param x
     * @param y
     */
    public void createNode(int x, int y) {
        renderGraph.createNode().setXY(x, y);
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
        if (connectedNode == selectedNode) connectedNode = null;
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

    protected Node innerFindNode(int x, int y) {
        Node founNode = null;
        int foundSqrDist = 0;
        for (Node node : graph.getNodes()) {
            int dx = node.x - x;
            int dy = node.y - y;
            int sqrDist = dx * dx + dy * dy;
            if (sqrDist <= node.radius * node.radius && (founNode == null || foundSqrDist > sqrDist)) {
                founNode = node;
                foundSqrDist = sqrDist;
            }
        }
        return founNode;
    }

    protected Edge innerFindEdge(int x, int y) {
        Edge founEdge = null;
        double foundDist = 0;

        for (Edge edge : graph.getEdges()) {
            Node fromNode = graph.getNode(edge.fromNode);
            Node toNode = graph.getNode(edge.toNode);
            int dx = toNode.x - fromNode.x;
            int dy = toNode.y - fromNode.y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            double vx = dx / dist;
            double vy = dy / dist;
            double lx = vx * (x - fromNode.x) + vy * (y - fromNode.y);
            if (lx < 0 || lx > dist)
                continue;
            double ly = vy * (x - fromNode.x) - vx * (y - fromNode.y);
            double distToLine = Math.abs(ly);
            if (distToLine > EDGE_DRAG_DISTANCE)
                continue;
            if (founEdge == null || distToLine < foundDist) {
                founEdge = edge;
                foundDist = distToLine;
            }
        }
        return founEdge;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (connectedNode != null) {
            g2d.setColor(Color.GREEN);
            g2d.setStroke(CONNECTING_STROKE);
            g2d.drawLine(connectedNode.x, connectedNode.y, connectingLastX, connectingLastY);
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
}

class GraphEditorOld extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ComponentListener {

    //private Grid grid;
    private boolean drawGrid;
    private Graph graph;

    private boolean mouseLeftButton = false;
    private boolean mouseRightButton = false;
    private boolean chooseNodeB = false;
    private int mouseX;
    private int mouseY;
    private Node nodeUnderCursor;
    private Edge edgeUnderCursor;

//    private Node newEdgeNodeA;
//    private Node newEdgeNodeB;

    public GraphEditor() {
        graph = new Graph();
        //grid = new Grid(getSize(), 50);
        drawGrid = false;

        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        addComponentListener(this);
        setFocusable(true);
        requestFocus();
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph == null ? new Graph() : graph;
    }

    public void createNewGraph() {
        setGraph(new Graph());
        repaint();
    }

    //region INTERFACES
    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) mouseLeftButton = true;
        if (e.getButton() == MouseEvent.BUTTON3) mouseRightButton = true;
        setMouseCursor(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseLeftButton = false;
            finalizeAddEdge();
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            mouseRightButton = false;
            chooseNodeB = false;
            if (nodeUnderCursor != null) {
                createNodePopupMenu(e, nodeUnderCursor);
            } else if (edgeUnderCursor != null) {
                createEdgePopupMenu(e, edgeUnderCursor);
            } else {
                createPlainPopupMenu(e);
            }
        }
        setMouseCursor(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseLeftButton) {
            moveGraphDrag(e.getX(), e.getY());
        } else {
            setMouseCursor(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        setMouseCursor(e);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //if (grid != null && drawGrid) grid.draw(g);
        if (graph != null) graph.draw(g);
    }
    //endregion

    //region POPUP

    private void createPlainPopupMenu(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem newNodeMenuItem = new JMenuItem("Создать узел");
        newNodeMenuItem.addActionListener((action) -> createNewNode(e.getX(), e.getY()));
        popupMenu.add(newNodeMenuItem);

        popupMenu.show(e.getComponent(), e.getX(), e.getY());

    }

    private void createNodePopupMenu(MouseEvent e, Node n) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem removeNodeMenuItem = new JMenuItem("Удалить узел");
        removeNodeMenuItem.addActionListener((action) -> removeNode(n));
        popupMenu.add(removeNodeMenuItem);
        popupMenu.addSeparator();
        JMenuItem addEdgeMenuItem = new JMenuItem("Создать дугу");
        addEdgeMenuItem.addActionListener((action) -> initializeAddEdge(n));
        popupMenu.add(addEdgeMenuItem);
        popupMenu.addSeparator();
        JMenuItem changeNodeRadiusMenuItem = new JMenuItem("Изменить радиус узла");
        changeNodeRadiusMenuItem.addActionListener((action) -> changeNodeRadius(n));
        popupMenu.add(changeNodeRadiusMenuItem);
        JMenuItem changeNodeColorMenuItem = new JMenuItem("Изменить цвет узла");
        changeNodeColorMenuItem.addActionListener((action) -> changeNodeColor(n));
        popupMenu.add(changeNodeColorMenuItem);
        JMenuItem changeTextMenuItem = new JMenuItem("Изменить имя узла");
        changeTextMenuItem.addActionListener((action) -> changeNodeText(n));
        popupMenu.add(changeTextMenuItem);

        popupMenu.show(e.getComponent(), e.getX(), e.getY());

    }


    private void createEdgePopupMenu(MouseEvent event, Edge e) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem removeEdgeMenuItem = new JMenuItem("Удалить дугу");
        removeEdgeMenuItem.addActionListener((action) -> removeEdge(e));
        popupMenu.add(removeEdgeMenuItem);
        popupMenu.addSeparator();
        JMenuItem changeEdgeStrokeMenuItem = new JMenuItem("Изменить толщину дуги");
        changeEdgeStrokeMenuItem.addActionListener((action) -> changeEdgeStroke(e));
        popupMenu.add(changeEdgeStrokeMenuItem);
        JMenuItem changeEdgeColorMenuItem = new JMenuItem("Изменить цвет дуги");
        changeEdgeColorMenuItem.addActionListener((action) -> changeEdgeColor(e));
        popupMenu.add(changeEdgeColorMenuItem);

        popupMenu.show(event.getComponent(), event.getX(), event.getY());
    }

    //endregion

    private void setMouseCursor(MouseEvent e) {
        if (e != null) {
            nodeUnderCursor = graph.findNodeUnderCursor(e.getX(), e.getY());
            if (nodeUnderCursor == null) {
                edgeUnderCursor = graph.findEdgeUnderCursor(e.getX(), e.getY());
            }
            mouseX = e.getX();
            mouseY = e.getY();
        }

        if (nodeUnderCursor != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        } else if (edgeUnderCursor != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        } else if (chooseNodeB) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        } else if (mouseLeftButton) {
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        }
    }

    private void moveGraphDrag(int mx, int my) {
        int dx = mx - mouseX;
        int dy = my - mouseY;

        if (nodeUnderCursor != null) {
            nodeUnderCursor.move(dx, dy);
        } else if (edgeUnderCursor != null) {
            edgeUnderCursor.move(dx, dy);
        } else {
            graph.moveGraph(dx, dy);
        }

        mouseX = mx;
        mouseY = my;
        repaint();
    }
}



