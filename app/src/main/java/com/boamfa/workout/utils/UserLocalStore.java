package com.boamfa.workout.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bogdan on 25/11/15.
 */
public class UserLocalStore {
    public final static String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = this.userLocalDatabase.edit();
        spEditor.putInt("id", user.id);
        spEditor.putString("email", user.email);
        spEditor.putString("auth_token", user.auth_token);
        spEditor.putString("name", user.name);
        spEditor.commit();
    }

    public User getLoggedInUser() {
        int id = userLocalDatabase.getInt("id", -1);
        String name = userLocalDatabase.getString("name", "");
        String email = userLocalDatabase.getString("email", "");
        String auth_token = userLocalDatabase.getString("auth_token", "");

        User user = new User(id, email, auth_token, name);

        return user;
    }

    public void setLoggedInUser() {

    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = this.userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
