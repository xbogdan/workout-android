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
import com.boamfa.workout.classes.AppTask;
import com.boamfa.workout.classes.Track;
import com.boamfa.workout.classes.TrackDay;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TrackActivity extends BaseActivity {

    private Track track;
    private final Activity self = this;
    private ListView trackDayList;
    private TrackSwipeAdapter trackDaysListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        track = (Track) getIntent().getSerializableExtra("track");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_track, null, false);

        drawerLayout.addView(contentView, 0);

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
                        (new CreateTrackDayTask(newDate)).execute();
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

        trackDayList = (ListView) findViewById(R.id.trackDayList);

        trackDaysListAdapter = new TrackSwipeAdapter(this, R.layout.tracks_item, R.id.swipe, track.id, track.days);
        trackDayList.setAdapter(trackDaysListAdapter);
        trackDayList.setClickable(true);
        trackDayList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TrackActivity.this, TrackDaysActivity.class);
                i.putExtra("day", track.days.get(position));
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public class CreateTrackDayTask extends AppTask {
        private String date;

        public CreateTrackDayTask(String date) {
            super(TrackActivity.this, userLocalStore);
            this.date = date;
        }

        @Override
        public void onSuccess(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                int trackDayId = jsonResponse.getInt("day_id");

                TrackDay newTrackDay = new TrackDay(trackDayId, date);
                newTrackDay.setItems();

                track.days.add(newTrackDay);
                trackDaysListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            response = service.createTrackDay(currentUser.auth_token, track.id, date);
            return true;
        }
    }
}