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

public class TracksActivity extends BaseActivity {

    private ListView tracksListView;
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
                        m_Text = m_Text.trim().replaceAll("\t", "").replaceAll("\n", "");
                        Track newTrack = new Track(m_Text);
                        newTrack.id = db.addTrack(newTrack);

                        trackList.add(newTrack);
                        tracksListAdapter.notifyDataSetChanged();
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
        if (trackList == null) {
            trackList = db.getTracks();
            tracksListAdapter = new TracksSwipeAdapter(this, R.layout.tracks_item, R.id.swipe, trackList);

            tracksListView.setAdapter(tracksListAdapter);
            tracksListView.setClickable(true);
            tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(TracksActivity.this, TrackActivity.class);
                    i.putExtra("track_id", trackList.get(position).id);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}

