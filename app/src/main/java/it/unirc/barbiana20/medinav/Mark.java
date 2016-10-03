package it.unirc.barbiana20.medinav;

import android.graphics.PointF;
import android.util.Log;

/**
 * Created by giuse on 06/09/2016.
 */

public class Mark {
    public enum Types {Endpoint, Waypoint, Entrance, Stair, Elevator, Toilet, FirstAid, FireEstinguisher, Ramp, EmergencyExit }

    public PointF pos;
    public int id;
    public String name;
    public Types type;
    public String cardImage;
    public String cardDesc;
    
    public Mark(int i, String n, PointF p, int t){
        pos = p;
        id = i;
        type = Types.values()[t];
        name = n;
    }

    public boolean isStair() {
        return type == Types.Stair || type == Types.Elevator;
    }
    public boolean isWaypoint() {
        return type == Types.Stair || type == Types.Waypoint || type == Types.Entrance;
    }
    public boolean hasName(){
        if(name == null) return false;
        //Log.d("HasName",String.format("Mark name: \"%s\"",name));
        return  !name.equals("");
    }
    public boolean isEntrance() {
        return type == Types.Entrance;
    }
}
