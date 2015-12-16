package com.boamfa.workout.utils;

import android.util.Pair;

import java.util.HashMap;

/**
 * Created by bogdan on 25/11/15.
 */
public class AppService {

//    private static final String endPoint = "http://10.0.2.2:3000/api/v1";
//    private static final String endPoint = "http://192.168.1.218:3000/api/v1";
    private static final String endPoint = "http://192.168.0.11:3000/api/v1";
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

    public Pair<Integer, String> updateTrack(String token, HashMap<String, String> postParams) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/updateTrack", "PUT", postParams, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> createTrackDay(String token, int trackId, String date) {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("track_id", trackId + "");
        postParams.put("date", date);

        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/createTrackDay", "POST", postParams, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> deleteTrackDay(String token, int trackId, int dayId) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/deleteTrackDay?track_id=" + trackId + "&day_id=" + dayId, "DELETE", null, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> getExercises(String token) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/exercises", "GET", null, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> createTrackDayExercise(String token, int dayId, int exerciseId) {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("day_id", dayId + "");
        postParams.put("exercise_id", exerciseId + "");

        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/createTrackDayExercise", "POST", postParams, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> deleteTrackDayExercise(String token, int trackDayExerciseId) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/deleteTrackDayExercise?id=" + trackDayExerciseId, "DELETE", null, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> updateTrackDayExercise(String token, HashMap<String, String> postParams) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/updateTrackDayExercise", "PUT", postParams, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> createTrackDayExerciseSet(String token, HashMap<String, String> postParams) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/createTrackDayExerciseSet", "POST", postParams, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> deleteTrackDayExerciseSet(String token, int id) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/deleteTrackDayExerciseSet?id=" + id, "DELETE", null, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public Pair<Integer, String> updateTrackDayExerciseSet(String token, HashMap<String, String> postParams) {
        HashMap<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("withCredentials", "true");
        requestHeaders.put("Authorization", token);

        Pair<Integer, String> response = null;
        try {
            response = httpConn.sendRequest(endPoint + "/updateTrackDayExerciseSet", "PUT", postParams, requestHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
