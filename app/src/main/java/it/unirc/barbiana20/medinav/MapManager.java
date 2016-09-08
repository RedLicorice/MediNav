package it.unirc.barbiana20.medinav;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giuse on 05/09/2016.
 */

public class MapManager {
    private List<University> uniList;
    public MapManager(List<University> l){
        uniList = l;
    }
    public MapManager(JSONArray jsonarray)
    {
        try {
            uniList = new ArrayList<University>();
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonUniversity = jsonarray.getJSONObject(i);
                uniList.add(new University(jsonUniversity));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public University getUniversity(int id){
        return uniList.get(id);
    }
    public Building getBuilding(Location t){
        try {
            University u = getUniversity(t.u);
            if(u == null)
            {
                Log.e("EROR","university is null!");
            }
            if(u.buildings == null)
            {
                Log.e("EROR","university buildings is null!");
            }
            return u.buildings.get(t.b);
        } catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            Log.e("GetBuilding","Error while retrieving building data!");
            return null;
        }
    }
    public Floor getFloor(Location t){
        try {
            University u = getUniversity(t.u);
            if(u == null)
            {
                Log.e("EROR","university is null!");
            }
            if(u.buildings == null)
            {
                Log.e("EROR","university buildings is null!");
            }
            Building b = u.buildings.get(t.b);
            Floor f = b.getFloors().get(t.f);
            return f;
        } catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            Log.e("GetFloor","Error while retrieving floor data!");
            return null;
        }
    }
}
