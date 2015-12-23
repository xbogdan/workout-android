package com.boamfa.workout.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdan on 03/12/15.
 */
public class Track implements Serializable {
    public long id;
    public String name;
    public List<TrackDay> days;

    public Track(String name) {
        this.name = name;
        this.setItems();
    }

    public Track(long id, String name) {
        this.id = id;
        this.name = name;
        this.setItems();
    }

    public void setItems(List<TrackDay> days) {
        this.days = days;
    }

    public void setItems() {
        this.days = new ArrayList<TrackDay>();
    }
}
