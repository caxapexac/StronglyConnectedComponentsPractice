package ru.eltech;

/**
 ** @author Samoilova Anna
 */

import java.awt.*;
import java.util.*;

public class Node {
    private String id;
    private Point position;
    private ArrayList<Edge> edges;

    public Node(String id){
        this.id = id;
        edges = new ArrayList<Edge>();
    }

    public String getId(){
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public ArrayList<Edge> getEdges(){
        return edges;
    }

    public void addEdge(Edge edge){
        if(edge!=null){
            edges.add(edge);
        }
    }

    public void removeEdge(Node target){
        int i = 0;
        while (i<edges.size()){
            if (edges.get(i).getTarget().getId().equals(target.getId())){
                edges.remove(edges.get(i));
            }else{
                i++;
            }
        }
    }
}
