package com.boamfa.workout.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boamfa.workout.R;
import com.boamfa.workout.utils.User;
import com.boamfa.workout.utils.UserLocalStore;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawerLayout.addView(contentView, 0);

        UserLocalStore userLocalStore = new UserLocalStore(this);
        User currentUser = userLocalStore.getLoggedInUser();
        if (currentUser.id == -1) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(MainActivity.this, TracksActivity.class);
            startActivity(i);
        }
    }
}