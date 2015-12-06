package com.boamfa.workout.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.TracksSwipeAdapter;
import com.boamfa.workout.classes.Track;
import com.boamfa.workout.utils.AppService;
import com.boamfa.workout.utils.User;
import com.boamfa.workout.utils.UserLocalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TracksActivity extends BaseActivity {

    private ListView tracksList;
    private List<Track> trackNameList;
    private TracksSwipeAdapter tracksAdapter;
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
                        (new CreateTrackTask(m_Text)).execute();
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

        tracksList = (ListView) findViewById(R.id.tracksList);
        MainTask task = new MainTask();
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void fillListView(Pair<Integer, String> response) {
        if (response == null) {
            userLocalStore.clearUserData();
            // TODO: Network error
        } else {
            switch (response.first) {
                case 200:
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response.second);
                        final JSONArray tracks = jsonResponse.getJSONArray("tracks");
                        trackNameList = new ArrayList<Track>();

                        for (int i = 0, size = tracks.length(); i < size; i++) {
                            JSONObject objectInArray = tracks.getJSONObject(i);
                            trackNameList.add(new Track((int) objectInArray.get("id"), (String) objectInArray.get("name")));
                        }

                        tracksAdapter = new TracksSwipeAdapter(this, R.layout.tracks_item, R.id.swipe, trackNameList);

                        tracksList.setAdapter(tracksAdapter);
                        tracksList.setClickable(true);
                        tracksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    JSONObject obj = tracks.getJSONObject(position);
                                    Intent i = new Intent(TracksActivity.this, TrackActivity.class);
                                    i.putExtra("track_id", obj.get("id").toString());
                                    startActivity(i);
                                } catch (JSONException e) {
                                    alertMessage("Error", "Invalid track id.");
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 401:
                    userLocalStore.clearUserData();
                    Intent i = new Intent(TracksActivity.this, LoginActivity.class);
                    startActivity(i);
                    break;
                default:
                    // TODO: Handle the rest of the errors
            }
        }
    }

    public class MainTask extends AsyncTask<Void, Void, Boolean> {
        private Pair<Integer, String> response;

        @Override
        protected Boolean doInBackground(Void... params) {
            response = service.getTracks(currentUser.auth_token);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                fillListView(this.response);
            } else {
                // TODO: task failed
            }
        }
    }

    public class CreateTrackTask extends AsyncTask<Void, Void, Boolean> {
        private Pair<Integer, String> response;
        private String trackName;

        public CreateTrackTask(String trackName) {
            this.trackName = trackName;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = service.createTrack(currentUser.auth_token, trackName);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                try {
                    JSONObject jsonResponse = new JSONObject(response.second);
                    int trackId = jsonResponse.getInt("track_id");
                    trackNameList.add(new Track(trackId, trackName));
                    tracksAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // TODO: task failed
            }
        }
    }
}

