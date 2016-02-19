package com.boamfa.workout.classes;

/**
 * Created by bogdan on 17/02/16.
 */
public class Option {
    public long id;
    public String name;
    public String value;

    public Option(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Option(long id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
}
