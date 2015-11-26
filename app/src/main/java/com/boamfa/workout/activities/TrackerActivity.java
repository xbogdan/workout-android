package com.boamfa.workout.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
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

public class TrackerActivity extends BaseActivity {

    private ListView trackerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_tracker, null, false);

        drawerLayout.addView(contentView, 0);

        trackerList = (ListView) findViewById(R.id.trackerList);

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetTrackerTask task = new GetTrackerTask(this);
        task.execute((Void) null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void fillListView(String context) {
        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(context);
            JSONArray tracks = jsonResponse.getJSONArray("tracks");
            List<String> trackNameList = new ArrayList<String>();

            for (int i = 0, size = tracks.length(); i < size; i++) {
                JSONObject objectInArray = tracks.getJSONObject(i);
                trackNameList.add((String) objectInArray.get("name"));
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.tracker_item, R.id.tracker_item_text_view, trackNameList);
            trackerList.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GetTrackerTask extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private String response;

        public GetTrackerTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            AppService service = new AppService();
            UserLocalStore userLocalStore = new UserLocalStore(context);
            User currentUser = userLocalStore.getLoggedInUser();
            Pair<Integer, String> response = service.getTracks(currentUser.auth_token);
            if (response != null) {
                this.response = response.second;
            } else {
                this.response = null;
                Intent i = new Intent(TrackerActivity.this, LoginActivity.class);
                startActivity(i);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success && this.response != null) {
                fillListView(this.response);
            } else {

            }
        }
    }
}

