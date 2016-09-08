package it.unirc.barbiana20.medinav;

import android.graphics.PointF;

/**
 * Created by giuse on 06/09/2016.
 */

public class Node {
    public PointF pos;
    public int id;
    public Node(int i, PointF p){
        pos = p;
        id = i;
    }
}
