package com.boamfa.workout.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdan on 10/12/15.
 */
public class TrackDay implements Serializable {
    public long id;
    public String name;
    public String date;
    public long trackId;
    public ArrayList<TrackDayExercise> exercises;

    public TrackDay(String date, long trackId) {
        this.trackId = trackId;
        this.date = date;
        this.setItems();
    }

    public TrackDay(long id, String date) {
        this.id = id;
        this.date = date;
        this.setItems();
    }

    public void setItems(ArrayList<TrackDayExercise> exercises) {
        this.exercises = exercises;
    }

    public void setItems() {
        this.exercises = new ArrayList<TrackDayExercise>();
    }
}
