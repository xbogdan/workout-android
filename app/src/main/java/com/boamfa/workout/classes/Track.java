package com.boamfa.workout.classes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bogdan on 03/12/15.
 */
public class Track implements Serializable {
    public int id;
    public String name;
    public List<TrackDay> days;

    public Track(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
