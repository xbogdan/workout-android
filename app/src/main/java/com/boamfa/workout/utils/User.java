package com.boamfa.workout.utils;

/**
 * Created by bogdan on 25/11/15.
 */
public class User {
    public int id;
    public String email, auth_token, name;

    public User(int id, String email, String auth_token) {
        this.id = id;
        this.email = email;
        this.auth_token = auth_token;
    }

    public User(int id, String email, String auth_token, String name) {
        this.id = id;
        this.email = email;
        this.auth_token = auth_token;
        this.name = name;
    }
}
