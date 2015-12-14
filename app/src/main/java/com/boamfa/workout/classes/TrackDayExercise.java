package com.boamfa.workout.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdan on 10/12/15.
 */
public class TrackDayExercise implements Serializable {
    public int id;
    public String name;
    public ArrayList<TrackDayExerciseSet> sets;

    public TrackDayExercise(int id, String name) {
        this.id = id;
        this.name = name;
        this.setItems();
    }

    public void setItems(ArrayList<TrackDayExerciseSet> sets) {
        this.sets = sets;
    }

    public void setItems() {
        this.sets = new ArrayList<TrackDayExerciseSet>();
    }
}
