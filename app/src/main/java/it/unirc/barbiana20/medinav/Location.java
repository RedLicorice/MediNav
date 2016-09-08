package it.unirc.barbiana20.medinav;

/**
 * Created by giuse on 06/09/2016.
 * This class contains location information, in form of:
 *  u = University ID
 *  b = Building ID
 *  f = Floor ID
 *  m = Marker ID
 * These information are serialized and used in qrCodes (hence the single-letter variable names),
 * as well as used as location information throughout the application.
 */

public class Location {
    public int u;
    public int b;
    public int f;
    public int m;
    public Location(int university, int building, int floor, int mark)
    {
        u = university;
        b = building;
        f = floor;
        m = mark;
    }
}
