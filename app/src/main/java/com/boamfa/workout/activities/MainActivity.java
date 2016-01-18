package com.boamfa.workout.activities;

import android.accounts.Account;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boamfa.workout.R;
import com.boamfa.workout.utils.AppService;

import java.io.IOException;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        db.reinstall();
//        db.addExercise("Dumbell curl");
//        db.addExercise("Barbell curl");
//        db.addExercise("Incline press");
//        db.addExercise("Pullups");
//        db.addExercise("Chinups");
//        db.addExercise("Dumbell rows");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawerLayout.addView(contentView, 0);

        final String accountType = getString(R.string.accountType);
        final String authTokenType = getString(R.string.authTokenType);
        Account availableAccounts[] = accountManager.getAccountsByType(accountType);

        if (availableAccounts.length == 0) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra(LoginActivity.ARG_ACCOUNT_TYPE, accountType);
            intent.putExtra(LoginActivity.ARG_AUTH_TYPE, authTokenType);
            intent.putExtra(LoginActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
            startActivity(intent);
        } else {
            currentAccount = availableAccounts[0];
            final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(currentAccount, authTokenType, null, this, null, null);
            (new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String token = null;
                    try {
                        token = future.getResult().getString("authtoken");
                    } catch (OperationCanceledException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (AuthenticatorException e) {
                        e.printStackTrace();
                    }
                    return token;
                }
                @Override
                protected void onPostExecute(String token) {
                    authToken = token;
                    service = new AppService(authToken);
                    Intent i = new Intent(MainActivity.this, TracksActivity.class);
                    startActivity(i);
                }
            }).execute();
        }
    }
}