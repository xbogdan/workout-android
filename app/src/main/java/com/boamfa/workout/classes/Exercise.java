package com.boamfa.workout.classes;

/**
 * Created by bogdan on 13/12/15.
 */
public class Exercise {
    public String name;
    public long id;
    public long muscleGroupId;
    public String muscleGroupName;

    public Exercise(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Exercise(long id, String name, long muscleGroupId, String muscleGroupName) {
        this.id = id;
        this.name = name;
        this.muscleGroupId = muscleGroupId;
        this.muscleGroupName = muscleGroupName;
    }

    public Exercise(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
