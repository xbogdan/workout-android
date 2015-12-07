package com.boamfa.workout.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boamfa.workout.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TrackActivity extends BaseActivity {

    private Integer trackId;
    private final Activity self = this;
    private ListView trackDayList;
    private List<String> listItems;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        trackId = Integer.parseInt(i.getStringExtra("track_id"));

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

        MainTask task = new MainTask();
        task.execute((Void) null);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                        final JSONObject track = jsonResponse.getJSONObject("track");
                        final JSONArray days = track.getJSONArray("track_days_attributes");
                        listItems = new ArrayList<String>();
                        int daysNr = days.length();
                        if (daysNr == 0) {
                            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View noDataView = inflater.inflate(R.layout.listview_no_data, null, false);

                            TextView noDataTextView = (TextView) noDataView.findViewById(R.id.listview_no_data);
                            noDataTextView.setText("No track days :(");
                            noDataTextView.setGravity(Gravity.CENTER);

                            RelativeLayout rl = (RelativeLayout) findViewById(R.id.track_layout);
                            noDataTextView.setWidth(rl.getWidth());
                            rl.addView(noDataTextView);
                        } else {
                            for (int i = 0, size = daysNr; i < size; i++) {
                                JSONObject objectInArray = days.getJSONObject(i);
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                Date date = format.parse(objectInArray.get("date").toString());

                                DateFormat format2 = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
                                String newDate = format2.format(date);

                                listItems.add(newDate);
                            }
                            arrayAdapter = new ArrayAdapter<String>(this, R.layout.track_item, R.id.tracker_item_text_view, listItems);
                            trackDayList.setAdapter(arrayAdapter);
                            trackDayList.setClickable(true);
                            trackDayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        Intent i = new Intent(TrackActivity.this, TrackDaysActivity.class);
                                        i.putExtra("day", days.getJSONObject(position).toString());
                                        startActivity(i);
                                    } catch (JSONException e) {
//                                    alertMessage("Error", "Invalid track id.");
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 401:
                    userLocalStore.clearUserData();
                    Intent i = new Intent(TrackActivity.this, LoginActivity.class);
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
            response = service.getTrack(currentUser.auth_token, trackId);
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

    public class CreateTrackDayTask extends AsyncTask<Void, Void, Boolean> {
        private Pair<Integer, String> response;
        private String date;

        public CreateTrackDayTask(String date) {
            this.date = date;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HashMap<String, String> postParams = new HashMap<String, String>();
            postParams.put("track[id]", trackId + "");
            postParams.put("track[track_days_attributes[][date]]", date);
            service.updateTrack(currentUser.auth_token, postParams);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                Date parsedDate = null;
                try {
                    parsedDate = format.parse(date);

                    DateFormat format2 = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
                    String newDate = format2.format(parsedDate);

                    listItems.add(newDate);
                    arrayAdapter.notifyDataSetChanged();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                // TODO: task failed
            }
        }
    }
}
