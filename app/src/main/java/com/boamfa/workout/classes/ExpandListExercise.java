package com.boamfa.workout.classes;

import java.util.ArrayList;

/**
 * Created by bogdan on 30/11/15.
 */
public class ExpandListExercise {

    private String name;
    private ArrayList<ExpandListSet> sets;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<ExpandListSet> getItems() {
        return sets;
    }
    public void setItems(ArrayList<ExpandListSet> Items) {
        this.sets = Items;
    }
}

