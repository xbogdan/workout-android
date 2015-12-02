package com.boamfa.workout.classes;

import java.util.ArrayList;

/**
 * Created by bogdan on 30/11/15.
 */
public class ExpandListGroup {

    private String name;
    private ArrayList<ExpandListChild> sets;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<ExpandListChild> getItems() {
        return sets;
    }
    public void setItems(ArrayList<ExpandListChild> Items) {
        this.sets = Items;
    }
}

