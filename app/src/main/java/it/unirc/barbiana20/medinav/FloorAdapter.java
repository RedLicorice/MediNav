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
    private List<PointF> endMarks;
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

    public PointF getNode(int id){
        return nodes.get(id);
    }

    public String getMarkName(int id){
        return markNames.get(id);
    }
}
