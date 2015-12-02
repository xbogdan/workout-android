package com.boamfa.workout.utils;

import android.util.Pair;

import java.util.HashMap;

/**
 * Created by bogdan on 25/11/15.
 */
public class AppService {

    private static final String endPoint = "http://192.168.0.102:3000/api/v1";
//    private static final String endPoint = "http://192.168.2.1:3000/api/v1";
    private static final HttpCaller httpConn = new HttpCaller(false);

    public AppService() {
    }

    public Pair<Integer, String> login(String email, String password) {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("email", email);
        postParams.put("password", password);

        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/signin", "POST", postParams, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> getTracks(String token) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/tracks", "GET", null, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> getTrack(String token, Integer trackId) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/track?id=" + trackId, "GET", null, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
