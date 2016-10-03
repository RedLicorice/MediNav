package it.unirc.barbiana20.medinav;

import java.util.List;

/**
 * Created by giuse on 05/09/2016.
 */

public class Floor {
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    private int level;
    private String name;
    private String map;
    public List<Node> nodes;
    public List<Edge> edges;
    public List<Mark> marks;

    public Floor(int l, String n, String m){
        level = l;
        name = n;
        map = m;
    }

    public Mark getMark(int id){
        return marks.get(id);
    }


}
