package it.unirc.barbiana20.medinav;

import org.json.JSONObject;

/**
 * Created by giuse on 06/09/2016.
 * This class contains location information, in form of:
 *  u = University ID
 *  b = Building ID
 *  f = Floor ID
 *  m = Marker ID
 * These information are serialized and used in qrCodes ,
 * as well as used as location information throughout the application.
 */

public class Location {
    private int universityId;
    private int buildingId;
    private int floorId;
    private int markId;
    private String name;

    public Location(int universityId, int buildingId, int floorId, int markId)
    {
        this.universityId = universityId;
        this.buildingId = buildingId;
        this.floorId = floorId;
        this.markId = markId;
    }
    public Location(String data){
        try {
            JSONObject object = new JSONObject(data);
            ParseJson(object);
        } catch(org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    public Location(JSONObject object){
       ParseJson(object);
    }

    private void ParseJson(JSONObject object){
        try {
            this.universityId = object.getInt("u");
            this.buildingId = object.getInt("b");
            this.floorId = object.getInt("f");
            this.markId = object.getInt("m");
        } catch(org.json.JSONException e) {
            e.printStackTrace();
        }
    }

    public int getUniversityId() {
        return universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public int getMarkId() {
        return markId;
    }

    public void setMark(int markId) {
        this.markId = markId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String jsonSerialize() {return String.format("{\"u\":%d,\"b\":%d,\"f\":%d,\"m\":%d}",universityId,buildingId,floorId,markId); }

}
