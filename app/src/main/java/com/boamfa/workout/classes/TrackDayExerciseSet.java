package com.boamfa.workout.classes;

import java.io.Serializable;

/**
 * Created by bogdan on 10/12/15.
 */
public class TrackDayExerciseSet implements Serializable {
    public int id;
    public int reps;
    public double weight;

    public TrackDayExerciseSet(int id, int reps, double weight) {
        this.id = id;
        this.reps = reps;
        this.weight = weight;
    }

}
