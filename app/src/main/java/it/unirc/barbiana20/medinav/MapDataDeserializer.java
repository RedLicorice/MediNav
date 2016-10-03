package it.unirc.barbiana20.medinav;

import android.graphics.PointF;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by giuse on 30/09/2016.
 * Factory class for deserialization of database elements
 * the final implementation + static methods allows a behavior similar to that of a singleton
 */

public final class MapDataDeserializer {
    private MapDataDeserializer(){}

    public static List<University> getUniversityList(JSONArray jsonarray)
    {
        try {
            List<University> uniList = new ArrayList<University>();
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonUniversity = jsonarray.getJSONObject(i);
                uniList.add(getUniversity(jsonUniversity));
            }
            return uniList;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static University getUniversity(JSONObject object){
        try {
            String name = object.getString("name");
            Double latitude = object.getDouble("lat");
            Double longitude = object.getDouble("lng");
            JSONArray jsonNodes = object.getJSONArray("buildings");
            List<Building> buildings = new ArrayList<Building>();
            for (int i=0; i < jsonNodes.length(); i++)
            {
                JSONObject jsonNode = jsonNodes.getJSONObject(i);
                buildings.add(getBuilding(jsonNode));
            }
            University result =  new University(name, latitude, longitude);
            result.setBuildings(buildings);
            return result;
        } catch (org.json.JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Building getBuilding(JSONObject object){
        try {
            String name = object.getString("name");
            JSONArray jsonFloors = object.getJSONArray("floors");
            List<Floor> floors = new ArrayList<Floor>();
            for (int i=0; i < jsonFloors.length(); i++)
            {
                JSONObject jsonNode = jsonFloors.getJSONObject(i);
                if(jsonNode != null){
                    Floor newFloor = getFloor(jsonNode);
                    floors.add(newFloor);
                }
            }
            Building result = new Building(name);
            result.setFloors(floors);
            return result;
        } catch (org.json.JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Floor getFloor(JSONObject object){
        try {
            int level = object.getInt("level");
            String name = object.getString("name");
            String map = object.getString("map");
            Floor result = new Floor(level,name,map);
            //Base object is created, now load children
            //Nodes
            JSONArray jsonNodes = object.getJSONArray("nodes");
            result.nodes = new ArrayList<Node>();
            for (int i=0; i < jsonNodes.length(); i++)
            {
                JSONObject jsonNode = jsonNodes.getJSONObject(i);
                Node newNode = getNode(jsonNode);
                result.nodes.add(newNode.id,newNode);
            }
            //Edges
            JSONArray jsonEdges = object.getJSONArray("edges");
            result.edges = new ArrayList<Edge>();
            for (int i=0; i < jsonEdges.length(); i++)
            {
                JSONObject jsonEdge = jsonEdges.getJSONObject(i);
                Edge newEdge = getEdge(jsonEdge);
                if(result.nodes.size() < newEdge.from || result.nodes.size() < newEdge.to )
                {
                    Log.e("EDGE","Node index out of bounds!");
                    continue;
                }
                result.edges.add(newEdge);
            }
            //Marks
            JSONArray jsonMarks = object.getJSONArray("marks");
            result.marks = new ArrayList<Mark>();
            for (int i=0; i < jsonMarks.length(); i++)
            {
                JSONObject jsonMark = jsonMarks.getJSONObject(i);
                Mark newMark = getMark(jsonMark);
                result.marks.add(newMark.id,newMark);
            }
            return result;
        } catch (org.json.JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Node getNode(JSONObject object){
        try {
            int nodeID = object.getInt("id");
            String nodePointStr = object.getString("point");
            String[] strCoords = nodePointStr.split(",");
            PointF nodePoint = new PointF(Float.parseFloat(strCoords[0]),Float.parseFloat(strCoords[1]));
            return new Node(nodeID, nodePoint);
        } catch (org.json.JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    public static Edge getEdge(JSONObject object){
        try{
            int edgeFrom = object.getInt("from");
            int edgeTo = object.getInt("to");
            return new Edge(edgeFrom,edgeTo);
        } catch (org.json.JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    public static Mark getMark(JSONObject object){
        try{
            int markID = object.getInt("id");
            int markType = object.getInt("type");
            String markPointStr = object.getString("point");
            String[] strCoords = markPointStr.split(",");
            PointF markPoint = new PointF(Float.parseFloat(strCoords[0]),Float.parseFloat(strCoords[1]));
            String markName = object.getString("name");
            Mark m = new Mark(markID,markName,markPoint,markType);
            m.cardDesc = object.getString("cardDesc");
            m.cardImage = object.getString("cardImage");
            return m;
        } catch (org.json.JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
