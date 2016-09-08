package it.unirc.barbiana20.medinav;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giuse on 05/09/2016.
 */

public class Building {
    private String name;
    private List<Floor> floors;

    public Building(String name)
    {
        this.name = name;
    }
    public Building(JSONObject object)
    {
        try {
            name = object.getString("name");
            JSONArray jsonNodes = object.getJSONArray("floors");
            floors = new ArrayList<Floor>();
            for (int i=0; i < jsonNodes.length(); i++)
            {
                JSONObject jsonNode = jsonNodes.getJSONObject(i);
                if(jsonNode != null)
                    floors.add(new Floor(jsonNode));
            }
        } catch (org.json.JSONException e){
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public List<Floor> getFloors(){
        return floors;
    }
    public void setFloors(List<Floor> f ){
        floors = f;
    }
}
