package com.boamfa.workout.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import com.boamfa.workout.R;
import com.boamfa.workout.adapters.TrackSwipeAdapter;
import com.boamfa.workout.classes.TrackDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TrackActivity extends BaseActivity {

    private final Activity self = this;
    private ListView trackDayList;
    private TrackSwipeAdapter trackDaysListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final long trackId = getIntent().getLongExtra("track_id", -1);

        final ArrayList<TrackDay> trackDays = db.getTrackDays(trackId);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_track, null, false);

        drawerLayout.addView(contentView, 0);

        trackDayList = (ListView) findViewById(R.id.trackDayList);

        trackDaysListAdapter = new TrackSwipeAdapter(this, R.layout.tracks_item, R.id.swipe, trackId, trackDays);
        trackDayList.setAdapter(trackDaysListAdapter);
        trackDayList.setClickable(true);
        trackDayList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TrackActivity.this, TrackDaysActivity.class);
                i.putExtra("track_day_id", trackDays.get(position).id);
                startActivity(i);
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) contentView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar myCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        String newDate = format.format(myCalendar.getTime());

                        TrackDay newTrackDay = new TrackDay(newDate, trackId);
                        newTrackDay.id = db.addTrackDay(newTrackDay);

                        trackDays.add(newTrackDay);
                        trackDaysListAdapter.notifyDataSetChanged();
                    }

                };

                new DatePickerDialog(
                        self,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}