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

    public void setUniList(List<University> uniList) {
        this.uniList = uniList;
    }

    public List<University> getUniList() {
        return uniList;
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
            Building building = getBuilding(location);
            Floor floor = building.getFloors().get(location.getFloorId());
            return floor;
        } catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            Log.e("GetFloor","Error while retrieving floor data!");
            return null;
        }
    }

    public Mark getMark(Location location){
        try{
            Floor f = getFloor(location);
            return f.getMark(location.getMarkId());
        } catch (IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            Log.e("GetFloor","Error while retrieving floor data!");
            return null;
        }
    }
    //Reverse Access
    public List<Location> getLocations(int universityId){
        List<Location> res = new ArrayList<Location>();
        University cUniversity = getUniversity(universityId);
        for(int buildingId = 0; buildingId < cUniversity.getBuildings().size(); buildingId++)
        {
            Building cBuilding = cUniversity.getBuildings().get(buildingId);
            for(int floorId = 0; floorId < cBuilding.getFloors().size(); floorId++){
                Floor cFloor = cBuilding.getFloors().get(floorId);
                for(int markId = 0; markId < cFloor.marks.size(); markId++){
                    Mark cMark = cFloor.marks.get(markId);
                    if(!cMark.isWaypoint() && cMark.hasName()){
                        Location loc = new Location(universityId, buildingId, floorId, markId);
                        loc.setName(cMark.name);
                        res.add(loc);
                    }
                }
            }
        }
        return res;
    }
}
