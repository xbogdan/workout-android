package com.boamfa.workout.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.boamfa.workout.R;
import com.boamfa.workout.utils.AppService;
import com.boamfa.workout.utils.User;
import com.boamfa.workout.utils.UserLocalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrackActivity extends BaseActivity {

    private Integer trackId;
    private final Activity self = this;
    private ListView trackDayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        trackId = Integer.parseInt(i.getStringExtra("track_id"));

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_track, null, false);

        drawerLayout.addView(contentView, 0);

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
                        List<String> listItems = new ArrayList<String>();

                        for (int i = 0, size = days.length(); i < size; i++) {
                            JSONObject objectInArray = days.getJSONObject(i);
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            Date date = format.parse(objectInArray.get("date").toString());

                            DateFormat format2 = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
                            String newDate = format2.format(date);

                            listItems.add(newDate);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.track_item, R.id.tracker_item_text_view, listItems);
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
}
