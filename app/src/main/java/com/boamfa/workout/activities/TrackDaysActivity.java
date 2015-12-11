package com.boamfa.workout.activities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.ExpandListAdapter;
import com.boamfa.workout.classes.ExpandListChild;
import com.boamfa.workout.classes.ExpandListGroup;
import com.boamfa.workout.classes.TrackDay;

import java.util.ArrayList;

public class TrackDaysActivity extends BaseActivity {

    private final Activity self = this;

    private ExpandableListView ExpandList;
    private ExpandListAdapter ExpAdapter;
    private ArrayList<ExpandListGroup> ExpListItems;
    private TrackDay day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_track_days, null, false);
        drawerLayout.addView(contentView, 0);


        day = (TrackDay) getIntent().getSerializableExtra("day");


        ExpListItems = new ArrayList<ExpandListGroup>();
        for (int i = 0, size = day.exercises.size(); i < size; i++) {
            ExpandListGroup exercise = new ExpandListGroup();
            exercise.setName(day.exercises.get(i).name);

            ArrayList<ExpandListChild> setList = new ArrayList<ExpandListChild>();
            for (int j = 0, size2 = day.exercises.get(i).sets.size(); j < size2; j++) {
                ExpandListChild set = new ExpandListChild();
                set.setReps(day.exercises.get(i).sets.get(j).reps);
                set.setWeight(day.exercises.get(i).sets.get(j).weight);
                setList.add(set);
            }
            exercise.setItems(setList);
            ExpListItems.add(exercise);
        }

        ExpAdapter = new ExpandListAdapter(self, ExpListItems);

        ExpandList = (ExpandableListView) findViewById(R.id.expandableList);
        ExpandList.setAdapter(ExpAdapter);


//                try {
//                    JSONObject jsonResponse = new JSONObject(day);
//                    JSONArray exercises = jsonResponse.getJSONArray("track_day_exercises_attributes");
//
//                    int exercisesNr = exercises.length();
//                    if (exercisesNr == 0) {
//                        LayoutInflater inflater = (LayoutInflater) self.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        View noDataView = inflater.inflate(R.layout.listview_no_data, null, false);
//
//                        TextView noDataTextView = (TextView) noDataView.findViewById(R.id.listview_no_data);
//                        noDataTextView.setText("No day exercises :(");
//                        noDataTextView.setGravity(Gravity.CENTER);
//
//                        LinearLayout rl = (LinearLayout) findViewById(R.id.track_day_layout);
//                        noDataTextView.setWidth(rl.getWidth());
//                        rl.addView(noDataTextView);
//                    } else {
//                        ExpListItems = new ArrayList<ExpandListGroup>();
//
//                        for (int i = 0; i < exercisesNr; i++) {
//                            JSONObject exerciseObj = exercises.getJSONObject(i);
//                            ExpandListGroup exercise = new ExpandListGroup();
//                            exercise.setName(exerciseObj.getString("name"));
//
//                            ArrayList<ExpandListChild> setList = new ArrayList<ExpandListChild>();
//
//                            JSONArray sets = exerciseObj.getJSONArray("track_day_exercise_sets_attributes");
//                            for (int j = 0; j < sets.length(); j++) {
//                                JSONObject setObj = sets.getJSONObject(j);
//                                ExpandListChild set = new ExpandListChild();
//                                set.setReps(setObj.getInt("reps"));
//                                set.setWeight(Float.parseFloat(setObj.getString("weight")));
//                                setList.add(set);
//                            }
//                            exercise.setItems(setList);
//                            ExpListItems.add(exercise);
//                        }
//                        ExpAdapter = new ExpandListAdapter(self, ExpListItems);
//                        ExpandList.setAdapter(ExpAdapter);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
    }

    public class MainTask extends AsyncTask<Void, Void, Boolean> {
        private Pair<Integer, String> response;

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
            } else {
                // TODO: task failed
            }
        }
    }
}
