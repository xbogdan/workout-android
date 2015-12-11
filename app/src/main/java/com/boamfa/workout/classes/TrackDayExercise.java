package com.boamfa.workout.classes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bogdan on 10/12/15.
 */
public class TrackDayExercise implements Serializable {
    public int id;
    public String name;
    public List<TrackDayExerciseSet> sets;

    public TrackDayExercise(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
