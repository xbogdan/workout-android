package com.boamfa.workout.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.ExercisesListAdapter;
import com.boamfa.workout.adapters.ExpandListAdapter;
import com.boamfa.workout.classes.AppTask;
import com.boamfa.workout.classes.Exercise;
import com.boamfa.workout.classes.ExpandListExercise;
import com.boamfa.workout.classes.ExpandListSet;
import com.boamfa.workout.classes.TrackDay;
import com.boamfa.workout.classes.TrackDayExercise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrackDaysActivity extends BaseActivity {

    private ExpandableListView ExpandList;
    private ExpandListAdapter ExpAdapter;
    private ArrayList<ExpandListExercise> ExpListItems;
    private TrackDay day;
    private static List<Exercise> exerciseList;
    private static boolean gotExercises = false;
    private ExercisesListAdapter exercisesListAdapter;
    private static ListView exercisesListView;
    private PopupWindow exercisesPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_track_days, null, false);
        drawerLayout.addView(contentView, 0);

        int trackIndex = getIntent().getIntExtra("track_index", -1);
        int dayIndex = getIntent().getIntExtra("day_index", -1);

        day = trackList.get(trackIndex).days.get(dayIndex);

        // Exercise popup list
        exercisesPopup = new PopupWindow(drawerLayout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
        final RelativeLayout exercisesPopupBg = (RelativeLayout) inflater.inflate(R.layout.exercises_popup, null, false);
        exercisesPopupBg.getBackground().setAlpha(220);
        exercisesPopup.setFocusable(true);
        exercisesPopup.setContentView(exercisesPopupBg);

        EditText exerciseSearch = (EditText) exercisesPopupBg.findViewById(R.id.exercises_search);
        exerciseSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                exercisesListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        exercisesListView = (ListView) exercisesPopupBg.findViewById(R.id.exercises_list);
        exercisesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exercisesPopup.dismiss();
                Exercise selectedExercise = exercisesListAdapter.getItem(position);
                (new CreateTask(day.id, selectedExercise)).execute();
            }
        });

        Button closeButton = (Button) exercisesPopupBg.findViewById(R.id.exercises_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercisesPopup.dismiss();
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) contentView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercisesPopup.showAtLocation(drawerLayout, Gravity.CENTER, 0, 0);
            }
        });

        // Fill day items
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

        if (!gotExercises) {
            (new MainTask()).execute();
        }
    }

    private void createExerciseList(String json) {
        exerciseList = new ArrayList<Exercise>();
        try {
            JSONObject jsonResponse = new JSONObject(json);
            final JSONArray exercises = jsonResponse.getJSONArray("exercises");
            for (int i = 0, size1 = exercises.length(); i < size1; i++) {
                JSONObject exerciseObj = exercises.getJSONObject(i);
                Exercise e = new Exercise(exerciseObj.getInt("id"), exerciseObj.getString("name"), exerciseObj.getInt("muscle_group_id"), exerciseObj.getString("muscle_group_name"));
                exerciseList.add(e);
            }
            exercisesListAdapter = new ExercisesListAdapter(TrackDaysActivity.this, exerciseList, exercisesPopup);
            exercisesListView.setAdapter(exercisesListAdapter);
            exercisesListAdapter.notifyDataSetChanged();
            gotExercises = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MainTask extends AppTask {
        public MainTask() {
            super(TrackDaysActivity.this, userLocalStore);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = service.getExercises(currentUser.auth_token);
            return true;
        }

        @Override
        public void onSuccess(String response) {
            createExerciseList(response);
        }
    }

    public class CreateTask extends AppTask {
        int dayId;
        Exercise exercise;

        public CreateTask(int dayId, Exercise exercise) {
            super(TrackDaysActivity.this, userLocalStore);
            this.dayId = dayId;
            this.exercise = exercise;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = service.createTrackDayExercise(currentUser.auth_token, dayId, exercise.id);
            return true;
        }

        @Override
        public void onSuccess(String response) {
            ExpandListExercise expandListExercise = new ExpandListExercise(this.exercise.name, new ArrayList<ExpandListSet>());
            ExpListItems.add(expandListExercise);
            ExpAdapter.notifyDataSetChanged();
            try {
                JSONObject jsonResponse = new JSONObject(response);
                int trackDayExerciseId = jsonResponse.getInt("track_day_exercise_id");
                TrackDayExercise trackDayExercise = new TrackDayExercise(trackDayExerciseId, this.exercise.name);
                day.exercises.add(trackDayExercise);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
