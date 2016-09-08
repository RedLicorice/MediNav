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
    public MapManager(List<University> uniList){
        this.uniList = uniList;
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

    public Building getBuilding(Location location){
        try {
            University university = getUniversity(location.getUniversityId());
            if(university == null)
            {
                Log.e("ERROR","university is null!");
            }
            if(university.getBuildings().isEmpty()) {
                Log.e("ERROR", "university buildings is empty!");
            }
            return university.getBuildings().get(location.getBuildingId());
        } catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            Log.e("GetBuilding","Error while retrieving building data!");
            return null;
        }
    }
    public Floor getFloor(Location location){
        try {
            University university = getUniversity(location.getUniversityId());
            if(university == null)
            {
                Log.e("ERROR","university is null!");
            }
            if(university.getBuildings().isEmpty())
            {
                Log.e("ERROR","university buildings is empty!");
            }
            Building building = university.getBuildings().get(location.getBuildingId());
            Floor floor = building.getFloors().get(location.getFloorId());
            return floor;
        } catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            Log.e("GetFloor","Error while retrieving floor data!");
            return null;
        }
    }
}
