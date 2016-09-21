package it.unirc.barbiana20.medinav;

import android.graphics.PointF;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giuse on 05/09/2016.
 */

public class Floor {
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private int level;
    private String name;
    private String map;
    private String data;
    public List<Node> nodes;
    public List<Edge> edges;
    public List<Mark> marks;

    public Floor(int l, String n, String m, String d){
        level = l;
        name = n;
        map = m;
        data = d;
    }

    public Mark getMark(int id){
        return marks.get(id);
    }
    public Floor(JSONObject object){
        try {
            level = object.getInt("level");
            name = object.getString("name");
            map = object.getString("map");
            JSONArray jsonNodes = object.getJSONArray("nodes");
            nodes = new ArrayList<Node>();
            for (int i=0; i < jsonNodes.length(); i++)
            {
                JSONObject jsonNode = jsonNodes.getJSONObject(i);
                int nodeID = jsonNode.getInt("id");
                String nodePointStr = jsonNode.getString("point");
                String[] strCoords = nodePointStr.split(",");
                PointF nodePoint = new PointF(Float.parseFloat(strCoords[0]),Float.parseFloat(strCoords[1]));
                nodes.add(nodeID,new Node(nodeID,nodePoint));
            }
            JSONArray jsonEdges = object.getJSONArray("edges");
            edges = new ArrayList<Edge>();
            for (int i=0; i < jsonEdges.length(); i++)
            {
                JSONObject jsonEdge = jsonEdges.getJSONObject(i);
                int edgeFrom = jsonEdge.getInt("from");
                int edgeTo = jsonEdge.getInt("to");
                if(nodes.size() < edgeFrom || nodes.size() < edgeTo )
                {
                    Log.e("Node","Node index out of bounds!");
                    continue;
                }
                edges.add(new Edge(edgeFrom,edgeTo));
            }
            JSONArray jsonMarks = object.getJSONArray("marks");
            marks = new ArrayList<Mark>();
            for (int i=0; i < jsonMarks.length(); i++)
            {
                JSONObject jsonMark = jsonMarks.getJSONObject(i);
                int markID = jsonMark.getInt("id");
                int markType = jsonMark.getInt("type");
                String markPointStr = jsonMark.getString("point");
                String[] strCoords = markPointStr.split(",");
                PointF markPoint = new PointF(Float.parseFloat(strCoords[0]),Float.parseFloat(strCoords[1]));
                String markName = jsonMark.getString("name");
                Mark m = new Mark(markID,markName,markPoint,markType);
                marks.add(m.id,m);
            }
        } catch (org.json.JSONException e){
            e.printStackTrace();
        }
    }

}
