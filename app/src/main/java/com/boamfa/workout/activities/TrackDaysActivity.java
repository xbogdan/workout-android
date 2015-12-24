package com.boamfa.workout.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.ExercisesAdapter;
import com.boamfa.workout.adapters.TrackDayExerciseAdapter;
import com.boamfa.workout.classes.Exercise;
import com.boamfa.workout.classes.TrackDay;
import com.boamfa.workout.classes.TrackDayExercise;
import com.boamfa.workout.classes.TrackDayExerciseSet;
import com.boamfa.workout.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class TrackDaysActivity extends BaseActivity implements TrackDayExerciseAdapter.ExpandableListActions {

    private ExpandableListView expandListView;
    private TrackDayExerciseAdapter expandAdapter;

    // Exercise popup window
    private static boolean gotExercises = false;
    private static List<Exercise> exerciseList;
    private static ExercisesAdapter exercisesAdapter;
    private static ListView exercisesListView;

    private static PopupWindow exercisesPopup;

    private static PopupWindow setPopup;
    View contentView;

    Button okButton;

    private ArrayList<TrackDayExercise> trackDayExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.activity_track_days, null, false);
        drawerLayout.addView(contentView, 0);

        final long trackDayId = getIntent().getLongExtra("track_day_id", -1);
        trackDayExercises = db.getTrackDayExercises(trackDayId);

        expandListView = (ExpandableListView) findViewById(R.id.expandableList);
        expandAdapter = new TrackDayExerciseAdapter(TrackDaysActivity.this, trackDayExercises);
        expandAdapter.setActions(this);
        expandListView.setAdapter(expandAdapter);

        /**
         * Setup exercises list
         */
        if (!gotExercises) {
            exerciseList = db.getExercises();
            gotExercises = true;

            // Exercise popup window
            exercisesPopup = new PopupWindow(drawerLayout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
            exercisesPopup.setFocusable(true);
            exercisesPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            // Set popup window background
            final RelativeLayout exercisesPopupBg = (RelativeLayout) inflater.inflate(R.layout.exercises_popup, null, false);
            exercisesPopupBg.getBackground().setAlpha(220); // Dim the background color
            exercisesPopup.setContentView(exercisesPopupBg);

            // Exercise list
            exercisesListView = (ListView) exercisesPopupBg.findViewById(R.id.exercises_list);

            exercisesAdapter = new ExercisesAdapter(TrackDaysActivity.this, exerciseList, exercisesPopup);
            exercisesListView.setAdapter(exercisesAdapter);
            exercisesAdapter.notifyDataSetChanged();

            // Setup search input
            EditText exerciseSearch = (EditText) exercisesPopupBg.findViewById(R.id.exercises_search);
            exerciseSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    exercisesAdapter.getFilter().filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            // Close popup button
            Button closeButton = (Button) exercisesPopupBg.findViewById(R.id.exercises_close);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exercisesPopup.dismiss();
                }
            });
        }

        // Open pupup button
        FloatingActionButton floatingActionButton = (FloatingActionButton) contentView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercisesPopup.showAtLocation(drawerLayout, Gravity.CENTER, 0, 0);
                exercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        exercisesPopup.dismiss();
                        Exercise selectedExercise = exercisesAdapter.getItem(position);
                        TrackDayExercise trackDayExercise = new TrackDayExercise(selectedExercise.name, selectedExercise.id, trackDayId);
                        trackDayExercise.id = db.addTrackDayExercise(trackDayExercise);
                        trackDayExercises.add(trackDayExercise);
                        expandAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        createSetPoupup();
    }

    private void createSetPoupup() {
        // Set popup window
        setPopup = new PopupWindow(contentView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
        setPopup.setFocusable(true);
        setPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Set popup window background
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout setPopupBg = (RelativeLayout) inflater.inflate(R.layout.set_popup, null, false);
        setPopupBg.getBackground().setAlpha(400); // Dim the background color
        setPopup.setContentView(setPopupBg);

        // Close popup button
        Button closeButton = (Button) setPopupBg.findViewById(R.id.set_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopup.dismiss();
            }
        });

        // Close popup button
        okButton = (Button) setPopupBg.findViewById(R.id.set_ok);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPopup.dismiss();
            }
        });
    }

    @Override
    public void showSetPopup() {
        setPopup.showAtLocation(drawerLayout, Gravity.CENTER, 0, 0);
    }

    public double getWeight() {
        EditText weightField = (EditText) setPopup.getContentView().findViewById(R.id.set_weight);
        double value = Double.parseDouble(weightField.getText().toString());
        return value;
    }

    public int getReps() {
        EditText weightField = (EditText) setPopup.getContentView().findViewById(R.id.set_reps);
        int value = Integer.parseInt(weightField.getText().toString());
        return value;
    }

    @Override
    public void closeSetPopup() {
        setPopup.dismiss();
    }

    @Override
    public void deleteGroup(int groupPosition) {
        db.deleteTrackDayExercise(trackDayExercises.get(groupPosition).id);
        trackDayExercises.remove(groupPosition);
        expandAdapter.notifyDataSetChanged();
    }

    @Override
    public void editGroup(final int groupPosition) {
        exercisesPopup.showAtLocation(drawerLayout, Gravity.CENTER, 0, 0);
        exercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exercisesPopup.dismiss();
                Exercise selectedExercise = exercisesAdapter.getItem(position);
                trackDayExercises.get(groupPosition).name = selectedExercise.name;
                trackDayExercises.get(groupPosition).exerciseId = selectedExercise.id;
                db.updateTrackDayExercise(trackDayExercises.get(groupPosition));
                expandAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void addChild(final int groupPosition) {
        showSetPopup();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSetPopup();

                EditText weightField = (EditText) setPopup.getContentView().findViewById(R.id.set_weight);
                double weight = Double.parseDouble(weightField.getText().toString());

                EditText repsField = (EditText) setPopup.getContentView().findViewById(R.id.set_reps);
                int reps = Integer.parseInt(repsField.getText().toString());

                TrackDayExerciseSet trackDayExerciseSet = new TrackDayExerciseSet(reps, weight, trackDayExercises.get(groupPosition).id);
                trackDayExerciseSet.id = db.addTrackDayExerciseSet(trackDayExerciseSet);
                trackDayExercises.get(groupPosition).sets.add(trackDayExerciseSet);
                expandAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void deleteChild(int groupPosition, int childPosition) {
        db.deleteTrackDayExerciseSet(trackDayExercises.get(groupPosition).sets.get(childPosition).id);
        trackDayExercises.get(groupPosition).sets.remove(childPosition);
        expandAdapter.notifyDataSetChanged();
    }

    @Override
    public void editChild() {

    }
}
