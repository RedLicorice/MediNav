package it.unirc.barbiana20.medinav;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giuse on 05/09/2016.
 */

public class University {
    public String getName() {
        return name;
    }

    private String name ;
    public List<Building> buildings;
    public double lat ;
    public double lng ;
    public University(String name, double lat, double lng)
    {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }
    public University(JSONObject object)
    {
        try {
            name = object.getString("name");
            this.lat = object.getDouble("lat");
            this.lng = object.getDouble("lng");
            JSONArray jsonNodes = object.getJSONArray("buildings");
            buildings = new ArrayList<Building>();
            for (int i=0; i < jsonNodes.length(); i++)
            {
                JSONObject jsonNode = jsonNodes.getJSONObject(i);
                buildings.add(new Building(jsonNode));
            }
        } catch (org.json.JSONException e){
            e.printStackTrace();
        }
    }
}
