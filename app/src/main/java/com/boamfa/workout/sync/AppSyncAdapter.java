package com.boamfa.workout.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Pair;

import com.boamfa.workout.R;
import com.boamfa.workout.classes.History;
import com.boamfa.workout.classes.Option;
import com.boamfa.workout.classes.Track;
import com.boamfa.workout.classes.TrackDay;
import com.boamfa.workout.classes.TrackDayExercise;
import com.boamfa.workout.classes.TrackDayExerciseSet;
import com.boamfa.workout.database.DatabaseContract;
import com.boamfa.workout.database.DatabaseHandler;
import com.boamfa.workout.utils.AppService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bogdan on 21/12/15.
 */
public class AppSyncAdapter extends AbstractThreadedSyncAdapter {
    private static AccountManager accountManager;
    private static AppService service;
    private static String accountType;
    private static String authTokenType;
    private static Context context;
    private static DatabaseHandler db;

    public AppSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        context = context;

        accountManager = AccountManager.get(context);
        accountType = context.getString(R.string.accountType);
        authTokenType = context.getString(R.string.authTokenType);

        db = new DatabaseHandler(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account, authTokenType, null, null, null, null);

        String authToken = null;
        try {
            authToken = future.getResult().getString("authtoken");
            service = new AppService(authToken);
            Pair<Integer, String> response;

            // TODO Get server history changes
            Option option = db.getOption("first_time_sync");

            if (option == null || option.value.equals("0")) {
                /**
                 * Syncing muscle groups
                 */
                response = service.getMuscleGroups();
                if (response == null) {
                    return;
                }
                if (response.first == 200) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.second);
                        JSONArray items = jsonResponse.getJSONArray("muscle_groups");

                        for (int i = 0, exNr = items.length(); i < exNr; i++) {
                            JSONObject ex = items.getJSONObject(i);
                            Long serverId = ex.getLong("id");
                            Long muscleGroupId;
                            try {
                                muscleGroupId = ex.getLong("muscle_group_id");
                            } catch (JSONException e) {
                                muscleGroupId = null;
                            }
                            if (!db.checkSync(serverId, DatabaseContract.MuscleGroupEntry.TABLE_NAME)) {
                                long id = db.addMuscleGroup(ex.getString("name"), muscleGroupId);
                                db.setSyncId(id, DatabaseContract.MuscleGroupEntry.TABLE_NAME, serverId);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (response.first/100 == 4) {
                    accountManager.invalidateAuthToken(accountType, authToken);
                }



                /**
                 * Syncing exercises
                 */
                response = service.getExercises();
                if (response.first == 200) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.second);
                        JSONArray items = jsonResponse.getJSONArray("exercises");

                        for (int i = 0, exNr = items.length(); i < exNr; i++) {
                            JSONObject ex = items.getJSONObject(i);
                            long serverId = ex.getInt("id");

                            if (!db.checkSync(serverId, DatabaseContract.ExerciseEntry.TABLE_NAME)) {
                                long id = db.addExercise(ex.getString("name"));
                                db.setSyncId(id, DatabaseContract.ExerciseEntry.TABLE_NAME, serverId);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (response.first/100 == 4) {
                    accountManager.invalidateAuthToken(accountType, authToken);
                }

                
                /**
                 * Syncing exercise muscle groups
                 */
                response = service.getExerciseMuscleGroups();
                if (response.first == 200) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.second);
                        JSONArray items = jsonResponse.getJSONArray("exercise_muscle_groups");

                        for (int i = 0, exNr = items.length(); i < exNr; i++) {
                            JSONObject ex = items.getJSONObject(i);
                            long serverId = ex.getInt("id");

                            if (!db.checkSync(serverId, DatabaseContract.ExerciseMuscleGroupEntry.TABLE_NAME)) {
                                long local_exercise_id = db.getSyncLocalId(ex.getLong("exercise_id"), DatabaseContract.ExerciseEntry.TABLE_NAME);
                                long local_muscle_group_id = db.getSyncLocalId(ex.getLong("muscle_group_id"), DatabaseContract.MuscleGroupEntry.TABLE_NAME);
                                long id = db.addExerciseMuscleGroup(local_exercise_id, local_muscle_group_id, ex.getBoolean("primary"));
                                db.setSyncId(id, DatabaseContract.ExerciseMuscleGroupEntry.TABLE_NAME, serverId);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (response.first/100 == 4) {
                    accountManager.invalidateAuthToken(accountType, authToken);
                }

                db.setOption("first_time_sync", "1");
            }


            ArrayList<History> list = db.getAllHistory();
            for (int i = 0, len = list.size(); i < len; i++) {
                History element = list.get(i);


                /**
                 * TRACK
                 */
                if (element.tableName.equals(DatabaseContract.TrackEntry.TABLE_NAME)) {
                    if (element.operation == DatabaseHandler.INSERT_OP || element.operation == DatabaseHandler.UPDATE_OP) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(element.content);
                        ObjectInputStream in = new ObjectInputStream(bis);
                        Track obj;
                        try {
                            obj = (Track) in.readObject();
                            in.close();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            continue;
                        }

                        // INSERT
                        if (element.operation == DatabaseHandler.INSERT_OP) {
                            Pair<Integer, String> res = service.createTrack(obj.name);
                            if (res.first == 201) {
                                try {
                                    JSONObject json = new JSONObject(res.second);
                                    int trackId = json.getInt("track_id");
                                    db.setSyncId(element.local_id, element.tableName, trackId);
                                    db.deleteHistory(element.id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (res.first/100 == 4) {
                                accountManager.invalidateAuthToken(accountType, authToken);
                            }
                        }

                        // UPDATE
                        if (element.operation == DatabaseHandler.UPDATE_OP) {
                            long serverId = db.getSyncServerId(element.local_id, element.tableName);

                            HashMap<String, String> postParams = new HashMap<String, String>();
                            postParams.put("track[id]", serverId+"");
                            postParams.put("track[name]", obj.name);

                            Pair<Integer, String> res = service.updateTrack(postParams);
                            if (res.first == 200) {
                                db.deleteHistory(element.id);
                            }
                            if (res.first/100 == 4) {
                                accountManager.invalidateAuthToken(accountType, authToken);
                            }
                        }
                    }

                    // DELETE
                    if (element.operation == DatabaseHandler.DELETE_OP) {
                        long serverId = db.getSyncServerId(element.local_id, DatabaseContract.TrackEntry.TABLE_NAME);
                        Pair<Integer, String> res = service.deleteTrack(serverId);
                        if (res.first == 200) {
                            db.deleteSyncEntry(element.local_id, element.tableName);
                            db.deleteHistory(element.id);
                            db.deleteTrack(element.local_id);
                        }
                        if (res.first/100 == 4) {
                            accountManager.invalidateAuthToken(accountType, authToken);
                        }
                    }
                }


                /**
                 * TRACK DAY
                 */
                if (element.tableName.equals(DatabaseContract.TrackDayEntry.TABLE_NAME)) {
                    if (element.operation == DatabaseHandler.INSERT_OP || element.operation == DatabaseHandler.UPDATE_OP) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(element.content);
                        ObjectInputStream in = new ObjectInputStream(bis);
                        TrackDay obj;
                        try {
                            obj = (TrackDay) in.readObject();
                            in.close();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            continue;
                        }

                        // INSERT
                        if (element.operation == DatabaseHandler.INSERT_OP) {
                            long trackServerId = db.getSyncServerId(obj.trackId, DatabaseContract.TrackEntry.TABLE_NAME);
                            Pair<Integer, String> res = service.createTrackDay(trackServerId, obj.date);
                            if (res.first == 201) {
                                try {
                                    JSONObject json = new JSONObject(res.second);
                                    int serverId = json.getInt("day_id");
                                    db.setSyncId(element.local_id, element.tableName, serverId);
                                    db.deleteHistory(element.id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (res.first/100 == 4) {
                                accountManager.invalidateAuthToken(accountType, authToken);
                            }
                        }

                        // UPDATE
                        if (element.operation == DatabaseHandler.UPDATE_OP) {
                            long serverId = db.getSyncServerId(element.local_id, element.tableName);

                            HashMap<String, String> postParams = new HashMap<String, String>();
                            postParams.put("id", serverId+"");
                            postParams.put("date", obj.date);

                            Pair<Integer, String> res = service.updateTrackDay(postParams);
                            if (res.first == 200) {
                                db.deleteHistory(element.id);
                            }
                            if (res.first/100 == 4) {
                                accountManager.invalidateAuthToken(accountType, authToken);
                            }
                        }
                    }

                    // DELETE
                    if (element.operation == DatabaseHandler.DELETE_OP) {
                        long serverId = db.getSyncServerId(element.local_id, element.tableName);
                        Pair<Integer, String> res = service.deleteTrackDay(serverId);
                        if (res.first == 200) {
                            db.deleteSyncEntry(element.local_id, element.tableName);
                            db.deleteHistory(element.id);
                            db.deleteTrackDay(element.local_id);
                        }
                        if (res.first/100 == 4) {
                            accountManager.invalidateAuthToken(accountType, authToken);
                        }
                    }
                }


                /**
                 * TRACK DAY EXERCISE
                 */
                if (element.tableName.equals(DatabaseContract.TrackDayExerciseEntry.TABLE_NAME)) {
                    if (element.operation == DatabaseHandler.INSERT_OP || element.operation == DatabaseHandler.UPDATE_OP) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(element.content);
                        ObjectInputStream in = new ObjectInputStream(bis);
                        TrackDayExercise obj;
                        try {
                            obj = (TrackDayExercise) in.readObject();
                            in.close();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            continue;
                        }

                        // INSERT
                        if (element.operation == DatabaseHandler.INSERT_OP) {
                            long serverExerciseId = db.getSyncServerId(obj.exerciseId, DatabaseContract.ExerciseEntry.TABLE_NAME);
                            long serverTrackDayId = db.getSyncServerId(obj.trackDayId, DatabaseContract.TrackDayEntry.TABLE_NAME);
                            Pair<Integer, String> res = service.createTrackDayExercise(serverTrackDayId, serverExerciseId);
                            if (res.first == 201) {
                                try {
                                    JSONObject json = new JSONObject(res.second);
                                    int serverId = json.getInt("track_day_exercise_id");
                                    db.setSyncId(element.local_id, element.tableName, serverId);
                                    db.deleteHistory(element.id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (res.first/100 == 4) {
                                accountManager.invalidateAuthToken(accountType, authToken);
                            }
                        }

                        // UPDATE
                        if (element.operation == DatabaseHandler.UPDATE_OP) {
                            long serverId = db.getSyncServerId(element.local_id, element.tableName);
                            long serverExerciseId = db.getSyncServerId(obj.exerciseId, DatabaseContract.ExerciseEntry.TABLE_NAME);

                            HashMap<String, String> postParams = new HashMap<String, String>();
                            postParams.put("id", serverId+"");
                            postParams.put("exercise_id", serverExerciseId+"");

                            Pair<Integer, String> res = service.updateTrackDayExercise(postParams);
                            if (res.first == 200) {
                                db.deleteHistory(element.id);
                            }
                            if (res.first/100 == 4) {
                                accountManager.invalidateAuthToken(accountType, authToken);
                            }
                        }
                    }

                    // DELETE
                    if (element.operation == DatabaseHandler.DELETE_OP) {
                        long serverId = db.getSyncServerId(element.local_id, element.tableName);
                        Pair<Integer, String> res = service.deleteTrackDayExercise(serverId);
                        if (res.first == 200) {
                            db.deleteSyncEntry(element.local_id, element.tableName);
                            db.deleteHistory(element.id);
                            db.deleteTrackDayExercise(element.local_id);
                        }
                        if (res.first/100 == 4) {
                            accountManager.invalidateAuthToken(accountType, authToken);
                        }
                    }
                }


                /**
                 * TRACK DAY EXERCISE SET
                 */
                if (element.tableName.equals(DatabaseContract.TrackDayExerciseSetEntry.TABLE_NAME)) {
                    if (element.operation == DatabaseHandler.INSERT_OP || element.operation == DatabaseHandler.UPDATE_OP) {
                        ByteArrayInputStream bis = new ByteArrayInputStream(element.content);
                        ObjectInputStream in = new ObjectInputStream(bis);
                        TrackDayExerciseSet obj;
                        try {
                            obj = (TrackDayExerciseSet) in.readObject();
                            in.close();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            continue;
                        }

                        // INSERT
                        if (element.operation == DatabaseHandler.INSERT_OP) {
                            long serverTrackDayExerciseId = db.getSyncServerId(obj.trackDayExerciseId, DatabaseContract.TrackDayExerciseEntry.TABLE_NAME);

                            HashMap<String, String> postParams = new HashMap<String, String>();
                            postParams.put("track_day_exercise_id", serverTrackDayExerciseId+"");
                            postParams.put("reps", obj.reps+"");
                            postParams.put("weight", obj.weight+"");

                            Pair<Integer, String> res = service.createTrackDayExerciseSet(postParams);
                            if (res.first == 201) {
                                try {
                                    JSONObject json = new JSONObject(res.second);
                                    int serverId = json.getInt("id");
                                    db.setSyncId(element.local_id, element.tableName, serverId);
                                    db.deleteHistory(element.id);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (res.first/100 == 4) {
                                accountManager.invalidateAuthToken(accountType, authToken);
                            }
                        }

                        // UPDATE
                        if (element.operation == DatabaseHandler.UPDATE_OP) {
                            long serverId = db.getSyncServerId(element.local_id, element.tableName);

                            HashMap<String, String> postParams = new HashMap<String, String>();
                            postParams.put("id", serverId+"");
                            postParams.put("reps", obj.reps+"");
                            postParams.put("weight", obj.weight+"");

                            Pair<Integer, String> res = service.updateTrackDayExerciseSet(postParams);
                            if (res.first == 200) {
                                db.deleteHistory(element.id);
                            }
                            if (res.first/100 == 4) {
                                accountManager.invalidateAuthToken(accountType, authToken);
                            }
                        }
                    }

                    // DELETE
                    if (element.operation == DatabaseHandler.DELETE_OP) {
                        long serverId = db.getSyncServerId(element.local_id, element.tableName);
                        Pair<Integer, String> res = service.deleteTrackDayExerciseSet(serverId);
                        if (res.first == 200) {
                            db.deleteSyncEntry(element.local_id, element.tableName);
                            db.deleteHistory(element.id);
                            db.deleteTrackDayExerciseSet(element.local_id);
                        }
                        if (res.first/100 == 4) {
                            accountManager.invalidateAuthToken(accountType, authToken);
                        }
                    }
                }
            }

            // TODO Handle exercise changes

        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        }
    }
}
