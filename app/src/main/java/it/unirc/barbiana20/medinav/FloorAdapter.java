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
    public int ClosestNodeToPoint(PointF m)    {
        double min = 0;
        int result = -1;
        int i=0;
        for (PointF p : nodes) {
            double res = Math.sqrt(Math.pow(p.x - m.x, 2) + Math.pow(p.y - m.y, 2));
            if (min == 0 || res < min) {
                min = res;
                result = i;
            }
            i++;
        }
        return result;
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
}
