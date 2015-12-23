package com.boamfa.workout.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdan on 10/12/15.
 */
public class TrackDayExercise implements Serializable {
    public long id;
    public String name;
    public long exerciseId;
    public long trackDayId;
    public ArrayList<TrackDayExerciseSet> sets;

    public TrackDayExercise(String name, long exerciseId, long trackDayId) {
        this.name = name;
        this.exerciseId = exerciseId;
        this.trackDayId = trackDayId;
        this.setItems();
    }

    public TrackDayExercise(long id, String name, long exerciseId, long trackDayId) {
        this.id = id;
        this.name = name;
        this.exerciseId = exerciseId;
        this.trackDayId = trackDayId;
        this.setItems();
    }

    public void setItems(ArrayList<TrackDayExerciseSet> sets) {
        this.sets = sets;
    }

    public void setItems() {
        this.sets = new ArrayList<TrackDayExerciseSet>();
    }
}
