package com.boamfa.workout.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.TracksSwipeAdapter;
import com.boamfa.workout.classes.AppTask;
import com.boamfa.workout.classes.Track;
import com.boamfa.workout.classes.TrackDay;
import com.boamfa.workout.classes.TrackDayExercise;
import com.boamfa.workout.classes.TrackDayExerciseSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TracksActivity extends BaseActivity {

    private ListView tracksListView;
//    private List<Track> trackList;
    private TracksSwipeAdapter tracksListAdapter;
    private final Activity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_tracks, null, false);

        drawerLayout.addView(contentView, 0);

        FloatingActionButton floatingActionButton = (FloatingActionButton) contentView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(self);
                builder.setTitle("Track name");

                // Set up the input
                final EditText input = new EditText(self);
                input.setTextColor(getResources().getColor(R.color.black));
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        (new CreateTask(m_Text)).execute();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        tracksListView = (ListView) findViewById(R.id.tracksList);
        (new MainTask()).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void createTrackList(String json) {
        JSONObject jsonResponse = null;
        trackList = new ArrayList<Track>();
        try {
            jsonResponse = new JSONObject(json);
            final JSONArray tracks = jsonResponse.getJSONArray("tracks");

            for (int i = 0, size1 = tracks.length(); i < size1; i++) {
                JSONObject trackObj = tracks.getJSONObject(i);
                Track track = new Track(trackObj.getInt("id"), trackObj.getString("name"));
//                track.days = new ArrayList<TrackDay>();
                trackList.add(track);

                // days
                JSONArray days;
                try {
                    days = trackObj.getJSONArray("track_days_attributes");
                } catch (JSONException e) {
                    continue;
                }
                for (int j = 0, size2 = days.length(); j < size2; j++) {
                    JSONObject trackDayObj = days.getJSONObject(j);
                    TrackDay trackDay = new TrackDay(trackDayObj.getInt("id"), trackDayObj.getString("date"));
//                    trackDay.exercises = new ArrayList<TrackDayExercise>();
                    track.days.add(trackDay);

                    // exercises
                    JSONArray exercises;
                    try {
                        exercises = trackDayObj.getJSONArray("track_day_exercises_attributes");
                    } catch (JSONException e) {
                        continue;
                    }
                    for (int k = 0, size3 = exercises.length(); k < size3; k++) {
                        JSONObject trackDayExerciseObj = exercises.getJSONObject(k);
                        TrackDayExercise trackDayExercise = new TrackDayExercise(trackDayExerciseObj.getInt("id"), trackDayExerciseObj.getString("name"));
//                        trackDayExercise.sets = new ArrayList<TrackDayExerciseSet>();
                        trackDay.exercises.add(trackDayExercise);

                        //sets
                        JSONArray sets;
                        try {
                            sets = trackDayExerciseObj.getJSONArray("track_day_exercise_sets_attributes");
                        } catch (JSONException e) {
                            continue;
                        }
                        for (int l = 0, size4 = sets.length(); l < size4; l++) {
                            JSONObject trackDayExerciseSetObj = sets.getJSONObject(l);
                            TrackDayExerciseSet trackDayExerciseSet = new TrackDayExerciseSet(trackDayExerciseSetObj.getInt("id"), trackDayExerciseSetObj.getInt("reps"), trackDayExerciseSetObj.getDouble("weight"));
                            trackDayExercise.sets.add(trackDayExerciseSet);
                        }
                    }
                }
            }

            tracksListAdapter = new TracksSwipeAdapter(this, R.layout.tracks_item, R.id.swipe, trackList);

            tracksListView.setAdapter(tracksListAdapter);
            tracksListView.setClickable(true);
            tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(TracksActivity.this, TrackActivity.class);
                    i.putExtra("track_index", position);
                    startActivity(i);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MainTask extends AppTask {
        public MainTask() {
            super(TracksActivity.this, userLocalStore);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = service.getTracks(currentUser.auth_token);
            return true;
        }

        @Override
        public void onSuccess(String response) {
            createTrackList(response);
        }
    }

    public class CreateTask extends AppTask {
        private String trackName;

        public CreateTask(String trackName) {
            super(TracksActivity.this, userLocalStore);
            this.trackName = trackName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = service.createTrack(currentUser.auth_token, trackName);
            return true;
        }

        @Override
        public void onSuccess(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                int trackId = jsonResponse.getInt("track_id");
                trackList.add(new Track(trackId, trackName));
                tracksListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

