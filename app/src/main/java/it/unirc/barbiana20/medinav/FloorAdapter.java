package it.unirc.barbiana20.medinav;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giuse on 06/09/2016.
 */

public class FloorAdapter {
    private List<PointF> nodes;
    private List<PointF> edges;
    private List<PointF> marks;
    private List<String> markNames;
    private List<PointF> stairMarks;
    public FloorAdapter(Floor f){
        nodes = new ArrayList<PointF>();
        for(Node n : f.nodes){
            nodes.add(n.id,n.pos);
        }
        edges = new ArrayList<PointF>();
        for(Edge e : f.edges)
        {
            edges.add(new PointF(e.from,e.to));
        }
        marks = new ArrayList<PointF>();
        stairMarks = new ArrayList<PointF>();
        markNames = new ArrayList<String>();
        for(Mark m : f.marks)
        {
            marks.add(m.id,m.pos);
            markNames.add(m.id,m.name);
            if(m.isStair())
                stairMarks.add(m.pos);
        }
    }
    public List<PointF> getNodes(){
        return nodes;
    }
    public int getNodeCount(){
        return nodes.size();
    }
    public List<PointF> getEdges(){
        return edges;
    }
    public int getEdgeCount(){
        return edges.size();
    }
    public List<PointF> getMarks(){
        return marks;
    }
    public List<PointF> getStairMarks(){
        return stairMarks;
    }
    public List<String> getMarkNames(){
        return markNames;
    }
    public PointF getNode(int id){
        return nodes.get(id);
    }
    public PointF getMark(int id){
        return marks.get(id);
    }
    public String getMarkName(int id){
        return markNames.get(id);
    }
}
