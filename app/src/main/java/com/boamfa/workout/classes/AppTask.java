package com.boamfa.workout.classes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Pair;

import com.boamfa.workout.R;
import com.boamfa.workout.activities.LoginActivity;

/**
 * Created by bogdan on 11/12/15.
 */
public abstract class AppTask extends AsyncTask<Void, Void, Boolean> {
    protected Pair<Integer, String> response;
    public abstract void onSuccess(String response);
    private Context context;

    public AppTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            if (response == null) {
                // TODO invalidate credentials
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
                        // TODO invalidate credentials
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, context.getString(R.string.accountType));
                        intent.putExtra(LoginActivity.ARG_AUTH_TYPE, context.getString(R.string.authTokenType));
                        intent.putExtra(LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
                        context.startActivity(intent);
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
