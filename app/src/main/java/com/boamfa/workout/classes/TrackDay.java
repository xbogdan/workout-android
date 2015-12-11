package com.boamfa.workout.classes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bogdan on 10/12/15.
 */
public class TrackDay implements Serializable {
    public int id;
    public String name;
    public String date;
    public List<TrackDayExercise> exercises;

    public TrackDay(int id, String date) {
        this.id = id;
        this.date = date;
    }
}
