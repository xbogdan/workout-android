package com.boamfa.workout.classes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Pair;

import com.boamfa.workout.activities.LoginActivity;

/**
 * Created by bogdan on 11/12/15.
 */
public abstract class AppTask extends AsyncTask<Void, Void, Boolean> {
    protected Pair<Integer, String> response;
    public abstract void onSuccess(String response);
    private Context context;
    private UserLocalStore userLocalStore;

    public AppTask(Context context, UserLocalStore userLocalStore) {
        this.context = context;
        this.userLocalStore = userLocalStore;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            if (response == null) {
                userLocalStore.clearUserData();
                // TODO: Network error
            } else {
                switch (response.first) {
                    case 200:
                        onSuccess(response.second);
                        break;
                    case 201:
                        onSuccess(response.second);
                        break;
                    case 401:
                        userLocalStore.clearUserData();
                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);
                        break;
                    case 400:
                        break;
                    case 500:
                        break;
                    default:
                        // TODO: Handle the rest of the errors
                }
            }
        } else {
            // TODO: task failed
        }
    }
}
