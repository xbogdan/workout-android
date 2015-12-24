package com.boamfa.workout.classes;

import java.io.Serializable;

/**
 * Created by bogdan on 10/12/15.
 */
public class TrackDayExerciseSet implements Serializable {
    public long id;
    public int reps;
    public double weight;
    public long trackDayExerciseId;

    public TrackDayExerciseSet(long id, int reps, double weight, long trackDayExerciseId) {
        this.id = id;
        this.reps = reps;
        this.weight = weight;
        this.trackDayExerciseId = trackDayExerciseId;
    }

    public TrackDayExerciseSet(int reps, double weight, long trackDayExerciseId) {
        this.reps = reps;
        this.weight = weight;
        this.trackDayExerciseId = trackDayExerciseId;
    }

}
