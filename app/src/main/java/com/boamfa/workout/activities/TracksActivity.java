package com.boamfa.workout.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;

public class TracksActivity extends BaseActivity {

    private ListView tracksList;
    private final Activity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_tracks, null, false);

        drawerLayout.addView(contentView, 0);

        tracksList = (ListView) findViewById(R.id.tracksList);
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

    private void alertMessage(String title, String message) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void fillListView(Pair<Integer, String> response) {
        if (response == null) {
            UserLocalStore userLocalStore = new UserLocalStore(this);
            userLocalStore.clearUserData();
            // TODO: Network error
        } else {
            switch (response.first) {
                case 200:
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response.second);
                        final JSONArray tracks = jsonResponse.getJSONArray("tracks");
                        List<String> trackNameList = new ArrayList<String>();

                        for (int i = 0, size = tracks.length(); i < size; i++) {
                            JSONObject objectInArray = tracks.getJSONObject(i);
                            trackNameList.add((String) objectInArray.get("name"));
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.track_item, R.id.tracker_item_text_view, trackNameList);
                        tracksList.setAdapter(arrayAdapter);
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
                    UserLocalStore userLocalStore = new UserLocalStore(this);
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
            AppService service = new AppService();
            UserLocalStore userLocalStore = new UserLocalStore(self);
            User currentUser = userLocalStore.getLoggedInUser();
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
}

