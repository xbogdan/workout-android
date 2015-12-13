package com.boamfa.workout.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.ExpandListAdapter;
import com.boamfa.workout.classes.ExpandListExercise;
import com.boamfa.workout.classes.ExpandListSet;
import com.boamfa.workout.classes.TrackDay;

import java.util.ArrayList;

public class TrackDaysActivity extends BaseActivity {

    private ExpandableListView ExpandList;
    private ExpandListAdapter ExpAdapter;
    private ArrayList<ExpandListExercise> ExpListItems;
    private TrackDay day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_track_days, null, false);
        drawerLayout.addView(contentView, 0);

        int trackIndex = getIntent().getIntExtra("track_index", -1);
        int dayIndex = getIntent().getIntExtra("day_index", -1);

        day = trackList.get(trackIndex).days.get(dayIndex);

        ExpListItems = new ArrayList<ExpandListExercise>();
        for (int i = 0, size = day.exercises.size(); i < size; i++) {
            ExpandListExercise exercise = new ExpandListExercise();
            exercise.setName(day.exercises.get(i).name);

            ArrayList<ExpandListSet> setList = new ArrayList<ExpandListSet>();
            for (int j = 0, size2 = day.exercises.get(i).sets.size(); j < size2; j++) {
                ExpandListSet set = new ExpandListSet();
                set.setReps(day.exercises.get(i).sets.get(j).reps);
                set.setWeight(day.exercises.get(i).sets.get(j).weight);
                setList.add(set);
            }
            exercise.setItems(setList);
            ExpListItems.add(exercise);
        }

        ExpAdapter = new ExpandListAdapter(TrackDaysActivity.this, ExpListItems);

        ExpandList = (ExpandableListView) findViewById(R.id.expandableList);
        ExpandList.setAdapter(ExpAdapter);
    }
}
