package it.unirc.barbiana20.medinav;

import android.graphics.PointF;

/**
 * Created by giuse on 06/09/2016.
 */

public class Mark {
    public PointF pos;
    public int id;
    public String name;
    public int type;
    private boolean stair;
    private boolean exit;
    public Mark(int i, String n, PointF p, int t){
        pos = p;
        id = i;
        type = t;
        name = n;
    }


    public boolean isStair() {
        return stair;
    }

    public void setStair(boolean stair) {
        this.stair = stair;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }
}
