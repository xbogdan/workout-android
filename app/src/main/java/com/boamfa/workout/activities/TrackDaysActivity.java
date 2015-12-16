package com.boamfa.workout.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
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
import com.boamfa.workout.adapters.ExercisesAdapter;
import com.boamfa.workout.adapters.TrackDayExerciseAdapter;
import com.boamfa.workout.classes.AppTask;
import com.boamfa.workout.classes.Exercise;
import com.boamfa.workout.classes.TrackDay;
import com.boamfa.workout.classes.TrackDayExercise;
import com.boamfa.workout.classes.User;
import com.boamfa.workout.classes.UserLocalStore;
import com.boamfa.workout.utils.AppService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrackDaysActivity extends BaseActivity implements InfoActivity {

    private TrackDay day;

    private ExpandableListView ExpandListView;
    private TrackDayExerciseAdapter ExpandAdapter;

    // Exercise popup window
    private static boolean gotExercises = false;
    private static List<Exercise> exerciseList;
    private static ExercisesAdapter exercisesAdapter;
    private static ListView exercisesListView;
    private static PopupWindow exercisesPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_track_days, null, false);
        drawerLayout.addView(contentView, 0);

        int trackIndex = getIntent().getIntExtra("track_index", -1);
        int dayIndex = getIntent().getIntExtra("day_index", -1);

        day = trackList.get(trackIndex).days.get(dayIndex);

        ExpandListView = (ExpandableListView) findViewById(R.id.expandableList);
        ExpandAdapter = new TrackDayExerciseAdapter(TrackDaysActivity.this, day.exercises);
        ExpandListView.setAdapter(ExpandAdapter);

        /**
         * Setup exercises list
         */
        if (!gotExercises) {
            (new MainTask()).execute();

            // Exercise popup window
            exercisesPopup = new PopupWindow(drawerLayout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
            exercisesPopup.setFocusable(true);

            // Set popup window background
            final RelativeLayout exercisesPopupBg = (RelativeLayout) inflater.inflate(R.layout.exercises_popup, null, false);
            exercisesPopupBg.getBackground().setAlpha(220); // Dim the background color
            exercisesPopup.setContentView(exercisesPopupBg);

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

            // Exercise list
            exercisesListView = (ListView) exercisesPopupBg.findViewById(R.id.exercises_list);

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
                        (new CreateTask(selectedExercise)).execute();
                    }
                });
            }
        });
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
            exercisesAdapter = new ExercisesAdapter(TrackDaysActivity.this, exerciseList, exercisesPopup);
            exercisesListView.setAdapter(exercisesAdapter);
            exercisesAdapter.notifyDataSetChanged();
            gotExercises = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ExercisesAdapter getExerciseAdapter() {
        return exercisesAdapter;
    }

    @Override
    public AppService getService() {
        return service;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public UserLocalStore getUserLocalStore() {
        return userLocalStore;
    }

    @Override
    public DrawerLayout getLayout() {
        return drawerLayout;
    }

    @Override
    public PopupWindow getExercisePopup() {
        return exercisesPopup;
    }

    @Override
    public ListView getExerciseListView() {
        return exercisesListView;
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
        Exercise exercise;

        public CreateTask(Exercise exercise) {
            super(TrackDaysActivity.this, userLocalStore);
            this.exercise = exercise;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = service.createTrackDayExercise(currentUser.auth_token, day.id, exercise.id);
            return true;
        }

        @Override
        public void onSuccess(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                int trackDayExerciseId = jsonResponse.getInt("track_day_exercise_id");
                TrackDayExercise trackDayExercise = new TrackDayExercise(trackDayExerciseId, this.exercise.name);
                day.exercises.add(trackDayExercise);
                ExpandAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
