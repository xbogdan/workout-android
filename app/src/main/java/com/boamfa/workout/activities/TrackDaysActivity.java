package com.boamfa.workout.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.ExpandListAdapter;
import com.boamfa.workout.classes.ExpandListChild;
import com.boamfa.workout.classes.ExpandListGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrackDaysActivity extends BaseActivity {

    private final Activity self = this;

    private ExpandListAdapter ExpAdapter;
    private ArrayList<ExpandListGroup> ExpListItems;
    private ExpandableListView ExpandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String day = intent.getStringExtra("day");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_track_days, null, false);
        drawerLayout.addView(contentView, 0);

        ExpandList = (ExpandableListView) findViewById(R.id.expandableList);

        try {
            JSONObject jsonResponse = new JSONObject(day);
            JSONArray exercises = jsonResponse.getJSONArray("track_day_exercises_attributes");

            ExpListItems = new ArrayList<ExpandListGroup>();

            for (int i = 0; i < exercises.length(); i++) {
                JSONObject exerciseObj = exercises.getJSONObject(i);
                ExpandListGroup exercise = new ExpandListGroup();
                exercise.setName(exerciseObj.getString("name"));

                ArrayList<ExpandListChild> setList = new ArrayList<ExpandListChild>();

                JSONArray sets = exerciseObj.getJSONArray("track_day_exercise_sets_attributes");
                for (int j = 0; j < sets.length(); j++) {
                    JSONObject setObj = sets.getJSONObject(j);
                    ExpandListChild set = new ExpandListChild();
                    set.setReps(setObj.getInt("reps"));
                    set.setWeight(Float.parseFloat(setObj.getString("weight")));
                    setList.add(set);
                }
                exercise.setItems(setList);
                ExpListItems.add(exercise);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ExpAdapter = new ExpandListAdapter(this, ExpListItems);
        ExpandList.setAdapter(ExpAdapter);
    }
}
