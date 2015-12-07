package com.boamfa.workout.utils;

import android.util.Pair;

import java.util.HashMap;

/**
 * Created by bogdan on 25/11/15.
 */
public class AppService {

//    private static final String endPoint = "http://10.0.2.2:3000/api/v1";
    private static final String endPoint = "http://192.168.1.218:3000/api/v1";
//    private static final String endPoint = "http://192.168.0.10:3000/api/v1";
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

    public Pair<Integer, String> deleteTrack(String token, Integer trackId) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/deleteTrack?id=" + trackId, "DELETE", null, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> createTrack(String token, String trackName) {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("track[name]", trackName);

        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/createTrack", "POST", postParams, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> updateTrack(String token, int trackId, String trackName) {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("track[id]", trackId+"");
        postParams.put("track[name]", trackName);

        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/updateTrack?id=5", "PUT", postParams, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
