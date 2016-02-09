package com.boamfa.workout.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.boamfa.workout.classes.Exercise;
import com.boamfa.workout.classes.History;
import com.boamfa.workout.classes.Track;
import com.boamfa.workout.classes.TrackDay;
import com.boamfa.workout.classes.TrackDayExercise;
import com.boamfa.workout.classes.TrackDayExerciseSet;
import com.boamfa.workout.fragments.FavoriteExercisesFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import static com.boamfa.workout.database.DatabaseContract.*;

/**
 * Database handler
 * Created by bogdan on 20/12/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "workout";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TrackEntry.SQL_CREATE_ENTRIES);
        db.execSQL(TrackDayEntry.SQL_CREATE_ENTRIES);
        db.execSQL(TrackDayExerciseEntry.SQL_CREATE_ENTRIES);
        db.execSQL(TrackDayExerciseSetEntry.SQL_CREATE_ENTRIES);
        db.execSQL(ExerciseEntry.SQL_CREATE_ENTRIES);
        db.execSQL(FavoriteUserExerciseEntry.SQL_CREATE_ENTRIES);
        db.execSQL(MuscleGroupEntry.SQL_CREATE_ENTRIES);
        db.execSQL(ExerciseMuscleGroupEntry.SQL_CREATE_ENTRIES);
        db.execSQL(HistoryEntry.SQL_CREATE_ENTRIES);
        db.execSQL(SyncEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(TrackEntry.SQL_DELETE_ENTRIES);
        db.execSQL(TrackDayEntry.SQL_DELETE_ENTRIES);
        db.execSQL(TrackDayExerciseEntry.SQL_DELETE_ENTRIES);
        db.execSQL(TrackDayExerciseSetEntry.SQL_DELETE_ENTRIES);
        db.execSQL(ExerciseEntry.SQL_DELETE_ENTRIES);
        db.execSQL(FavoriteUserExerciseEntry.SQL_CREATE_ENTRIES);
        db.execSQL(ExerciseMuscleGroupEntry.SQL_DELETE_ENTRIES);
        db.execSQL(MuscleGroupEntry.SQL_DELETE_ENTRIES);
        db.execSQL(HistoryEntry.SQL_DELETE_ENTRIES);
        db.execSQL(SyncEntry.SQL_DELETE_ENTRIES);

        // Create tables again
        onCreate(db);
    }

    public void reinstall() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(TrackEntry.SQL_DELETE_ENTRIES);
        db.execSQL(TrackDayEntry.SQL_DELETE_ENTRIES);
        db.execSQL(TrackDayExerciseEntry.SQL_DELETE_ENTRIES);
        db.execSQL(TrackDayExerciseSetEntry.SQL_DELETE_ENTRIES);
        db.execSQL(ExerciseEntry.SQL_DELETE_ENTRIES);
        db.execSQL(FavoriteUserExerciseEntry.SQL_DELETE_ENTRIES);
        db.execSQL(ExerciseMuscleGroupEntry.SQL_DELETE_ENTRIES);
        db.execSQL(MuscleGroupEntry.SQL_DELETE_ENTRIES);
        db.execSQL(HistoryEntry.SQL_DELETE_ENTRIES);
        db.execSQL(SyncEntry.SQL_DELETE_ENTRIES);

        onCreate(db);
    }


    /**
     * Track
     */
    public ArrayList<Track> getTracks() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Track> list = new ArrayList<Track>();

        String selectQuery = "SELECT _id, name FROM " + TrackEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Track element = new Track(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
                list.add(element);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public long addTrack(Track track) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TrackEntry.COLUMN_NAME, track.name);


        // Inserting Row
        long id = db.insert(TrackEntry.TABLE_NAME, null, values);

        byte[] obj = serializeToByteArray(track);
        addHistory(TrackEntry.TABLE_NAME, id, obj, "insert", db);

        addSyncId(id, TrackEntry.TABLE_NAME);

        db.close();

        return id;
    }

    public long updateTrack(Track track) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackEntry.COLUMN_NAME, track.name);

        byte[] obj = serializeToByteArray(track);
        addHistory(TrackEntry.TABLE_NAME, track.id, obj, "update", db);

        return db.update(TrackEntry.TABLE_NAME, values, TrackEntry._ID + " = ?", new String[] { String.valueOf(track.id) });
    }

    public void deleteTrack(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        addHistory(TrackEntry.TABLE_NAME, id, null, "delete", db);

        db.delete(TrackEntry.TABLE_NAME, TrackEntry._ID + " = ?", new String[]{id + ""});
        db.close();
    }


    /**
     * Track Day
     */
    public ArrayList<TrackDay> getTrackDays(long trackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TrackDay> list = new ArrayList<TrackDay>();

        String selectQuery = "SELECT _id, date FROM " + TrackDayEntry.TABLE_NAME + " WHERE " + TrackDayEntry.COLUMN_TRACK_ID + " = " + trackId + " ORDER BY date DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TrackDay element = new TrackDay(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
                list.add(element);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public long addTrackDay(TrackDay trackDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayEntry.COLUMN_DATE, trackDay.date);
        values.put(TrackDayEntry.COLUMN_TRACK_ID, trackDay.trackId);


        // Inserting Row
        long id = db.insert(TrackDayEntry.TABLE_NAME, null, values);

        addSyncId(id, TrackDayEntry.TABLE_NAME);
        byte[] obj = serializeToByteArray(trackDay);
        addHistory(TrackDayEntry.TABLE_NAME, id, obj, "insert", db);

        db.close();
        return id;
    }

    public long updateTrackDay(TrackDay trackDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayEntry.COLUMN_DATE, trackDay.date);

        byte[] obj = serializeToByteArray(trackDay);
        addHistory(TrackDayEntry.TABLE_NAME, trackDay.id, obj, "update", db);

        return db.update(TrackDayEntry.TABLE_NAME, values, TrackDayEntry._ID + " = ?", new String[] { String.valueOf(trackDay.id) });
    }

    public void deleteTrackDay(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        addHistory(TrackDayEntry.TABLE_NAME, id, null, "delete", db);

        db.delete(TrackDayEntry.TABLE_NAME, TrackDayEntry._ID + " = ?", new String[]{id + ""});
        db.close();
    }


    /**
     * Track Day Exercise
     */
    public ArrayList<TrackDayExercise> getTrackDayExercises(long trackDayId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TrackDayExercise> list = new ArrayList<TrackDayExercise>();

        String selectQuery = "SELECT " +
            TrackDayExerciseEntry.TABLE_NAME + "." + TrackDayExerciseEntry._ID + ", " +
            ExerciseEntry.TABLE_NAME + "." + ExerciseEntry.COLUMN_NAME + ", " +
            TrackDayExerciseEntry.COLUMN_EXERCISE_ID + ", " +
            TrackDayExerciseEntry.COLUMN_TRACK_DAY_ID + ", " +
            TrackDayExerciseEntry.COLUMN_ORD +
            " FROM " +
            TrackDayExerciseEntry.TABLE_NAME +
            " LEFT JOIN " + ExerciseEntry.TABLE_NAME +
            " ON (" + TrackDayExerciseEntry.TABLE_NAME + "." + TrackDayExerciseEntry.COLUMN_EXERCISE_ID + " = " + ExerciseEntry.TABLE_NAME + "." + ExerciseEntry._ID + ")" +
            " WHERE " + TrackDayExerciseEntry.COLUMN_TRACK_DAY_ID + " = " + trackDayId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TrackDayExercise element = new TrackDayExercise(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Long.parseLong(cursor.getString(2)), Long.parseLong(cursor.getString(3)));
                element.sets = getSets(element.id);
                list.add(element);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public long addTrackDayExercise(TrackDayExercise trackDayExercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayExerciseEntry.COLUMN_EXERCISE_ID, trackDayExercise.exerciseId);
        values.put(TrackDayExerciseEntry.COLUMN_TRACK_DAY_ID, trackDayExercise.trackDayId);


        // Inserting Row
        long id = db.insert(TrackDayExerciseEntry.TABLE_NAME, null, values);

        byte[] obj = serializeToByteArray(trackDayExercise);
        addHistory(TrackDayExerciseEntry.TABLE_NAME, id, obj, "insert", db);

        addSyncId(id, TrackDayExerciseEntry.TABLE_NAME);

        db.close();

        return id;
    }

    public long updateTrackDayExercise(TrackDayExercise trackDayExercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayExerciseEntry.COLUMN_EXERCISE_ID, trackDayExercise.exerciseId);

        byte[] obj = serializeToByteArray(trackDayExercise);
        addHistory(TrackDayExerciseEntry.TABLE_NAME, trackDayExercise.id, obj, "update", db);

        return db.update(TrackDayExerciseEntry.TABLE_NAME, values, TrackDayExerciseEntry._ID + " = ?", new String[] { String.valueOf(trackDayExercise.id) });
    }

    public void deleteTrackDayExercise(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        addHistory(TrackDayExerciseEntry.TABLE_NAME, id, null, "delete", db);

        db.delete(TrackDayExerciseEntry.TABLE_NAME, TrackDayExerciseEntry._ID + " = ?", new String[]{id + ""});
        db.close();
    }


    /**
     * Track Day Exercise Set
     */
    public ArrayList<TrackDayExerciseSet> getSets(long trackDayExerciseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<TrackDayExerciseSet> list = new ArrayList<TrackDayExerciseSet>();

        String selectQuery = "SELECT _id, reps, weight, ord FROM " + TrackDayExerciseSetEntry.TABLE_NAME + " WHERE " + TrackDayExerciseSetEntry.COLUMN_TRACK_DAY_EXERCISE_ID + " = " + trackDayExerciseId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TrackDayExerciseSet element = new TrackDayExerciseSet(Long.parseLong(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), trackDayExerciseId);
                list.add(element);
            } while (cursor.moveToNext());
        }
        db.close();

        return list;
    }

    public long addTrackDayExerciseSet(TrackDayExerciseSet trackDayExerciseSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayExerciseSetEntry.COLUMN_WEIGHT, trackDayExerciseSet.weight);
        values.put(TrackDayExerciseSetEntry.COLUMN_REPS, trackDayExerciseSet.reps);
        values.put(TrackDayExerciseSetEntry.COLUMN_TRACK_DAY_EXERCISE_ID, trackDayExerciseSet.trackDayExerciseId);


        // Inserting Row
        long id = db.insert(TrackDayExerciseSetEntry.TABLE_NAME, null, values);

        byte[] obj = serializeToByteArray(trackDayExerciseSet);
        addHistory(TrackDayExerciseSetEntry.TABLE_NAME, id, obj, "insert", db);

        addSyncId(id, TrackDayExerciseSetEntry.TABLE_NAME);

        db.close();

        return id;
    }

    public long updateTrackDayExerciseSet(TrackDayExerciseSet trackDayExerciseSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayExerciseSetEntry.COLUMN_WEIGHT, trackDayExerciseSet.weight);
        values.put(TrackDayExerciseSetEntry.COLUMN_REPS, trackDayExerciseSet.reps);

        byte[] obj = serializeToByteArray(trackDayExerciseSet);
        addHistory(TrackDayExerciseSetEntry.TABLE_NAME, trackDayExerciseSet.id, obj, "update", db);

        return db.update(TrackDayExerciseSetEntry.TABLE_NAME, values, TrackDayExerciseSetEntry._ID + " = ?", new String[] { String.valueOf(trackDayExerciseSet.id) });
    }

    public void deleteTrackDayExerciseSet(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        addHistory(TrackDayExerciseSetEntry.TABLE_NAME, id, null, "delete", db);

        db.delete(TrackDayExerciseSetEntry.TABLE_NAME, TrackDayExerciseSetEntry._ID + " = ?", new String[]{id + ""});
        db.close();
    }


    /**
     * Exercises
     */
    public long addExercise(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_NAME, name);

        // Inserting Row
        long id = db.insert(ExerciseEntry.TABLE_NAME, null, values);
        addSyncId(id, ExerciseEntry.TABLE_NAME);
        db.close();

        return id;
    }

    public ArrayList<Exercise> getExercises() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Exercise> list = new ArrayList<Exercise>();

        String selectQuery = "SELECT e._id, e.name, fae.exercise_id FROM " + ExerciseEntry.TABLE_NAME + " AS e LEFT JOIN " + FavoriteUserExerciseEntry.TABLE_NAME + " AS fae ON(fae.exercise_id=e._id)";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise element = new Exercise(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
                Long ex_id = cursor.getLong(2);
                if (ex_id != 0) {
                    element.favorite = true;
                } else {
                    element.favorite = false;
                }
                list.add(element);
            } while (cursor.moveToNext());
        }
        db.close();

        return list;
    }


    /**
     * Favorite exercises
     */
    public ArrayList<Exercise> getFavoriteExercises() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Exercise> list = new ArrayList<Exercise>();

        String selectQuery = "SELECT e._id, e.name FROM " + FavoriteUserExerciseEntry.TABLE_NAME + " AS fue , " + ExerciseEntry.TABLE_NAME + " AS e WHERE fue.exercise_id=e._id";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise element = new Exercise(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
                element.favorite = true;
                list.add(element);
            } while (cursor.moveToNext());
        }
        db.close();

        return list;
    }

    public long addFavoriteExercise(long exerciseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteUserExerciseEntry.COLUMN_EXERCISE_ID, exerciseId);

        // Inserting Row
        long id = db.insert(FavoriteUserExerciseEntry.TABLE_NAME, null, values);
        addSyncId(id, FavoriteUserExerciseEntry.TABLE_NAME);
        db.close();

        return id;
    }

    public void deleteFavoriteExercise(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteUserExerciseEntry.TABLE_NAME, FavoriteUserExerciseEntry.COLUMN_EXERCISE_ID + " = ?", new String[]{id + ""});
        db.close();
    }


    /**
     * Muscle Groups
     */
    public long addMuscleGroup(String name, Long muscleGroupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MuscleGroupEntry.COLUMN_NAME, name);
        values.put(MuscleGroupEntry.COLUMN_MUSCLE_GROUP_ID, muscleGroupId);

        // Inserting Row
        long id = db.insert(MuscleGroupEntry.TABLE_NAME, null, values);
        addSyncId(id, MuscleGroupEntry.TABLE_NAME);
        db.close();

        return id;
    }


    /**
     * Exercise Muscle Groups
     */
    public long addExerciseMuscleGroup(long exerciseId, long muscleGroupId, boolean primary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExerciseMuscleGroupEntry.COLUMN_EXERCISE_ID, exerciseId);
        values.put(ExerciseMuscleGroupEntry.COLUMN_MUSCLE_GROUP_ID, muscleGroupId);
        values.put(ExerciseMuscleGroupEntry.COLUMN_PRIMARY, primary);

        // Inserting Row
        long id = db.insert(ExerciseMuscleGroupEntry.TABLE_NAME, null, values);
        addSyncId(id, ExerciseMuscleGroupEntry.TABLE_NAME);
        db.close();

        return id;
    }


    /**
     * History
     */
    public long addHistory(String tableName, long localId, byte[] content, String operation, SQLiteDatabase db) {
        Boolean wasNull = false;
        if (db == null) {
            wasNull = true;
            db = this.getWritableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put(HistoryEntry.COLUMN_TABLE_NAME, tableName);
        values.put(HistoryEntry.COLUMN_LOCAL_ID, localId);
        values.put(HistoryEntry.COLUMN_CONTENT, content);
        values.put(HistoryEntry.COLUMN_OPERATION, operation);

        // Inserting Row
        long id = db.insert(HistoryEntry.TABLE_NAME, null, values);
        if (wasNull) {
            db.close();
        }
        return id;
    }

    public void deleteHistory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HistoryEntry.TABLE_NAME, HistoryEntry._ID + " = ?", new String[]{id + ""});
        db.close();
    }

    public ArrayList<History> getAllHistory() {
        ArrayList<History> list = new ArrayList<History>();

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT _id, table_name, content, local_id, operation, timestamp FROM " + HistoryEntry.TABLE_NAME + " ORDER BY timestamp ASC";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                History element = new History(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getBlob(2), cursor.getLong(3), cursor.getString(4), cursor.getString(5));
                list.add(element);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public byte[] serializeToByteArray(Serializable obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.close();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getSyncServerId(long localId, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery =
                "SELECT server_id FROM " + SyncEntry.TABLE_NAME +
                        " WHERE " +
                        SyncEntry.COLUMN_LOCAL_ID + " = " + localId + " AND " +
                        SyncEntry.COLUMN_TABLE_NAME + " = '" + tableName + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return new Long(0);
    }

    public Long getSyncLocalId(long serverId, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery =
                "SELECT server_id FROM " + SyncEntry.TABLE_NAME +
                        " WHERE " +
                        SyncEntry.COLUMN_SERVER_ID + " = " + serverId + " AND " +
                        SyncEntry.COLUMN_TABLE_NAME + " = '" + tableName + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return new Long(0);
    }

    public Boolean checkExerciseSync(long serverId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery =
                "SELECT server_id FROM " + SyncEntry.TABLE_NAME +
                        " WHERE " +
                        SyncEntry.COLUMN_SERVER_ID + " = " + serverId + " AND " +
                        SyncEntry.COLUMN_TABLE_NAME + " = '" + ExerciseEntry.TABLE_NAME + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public Boolean checkSync(long serverId, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery =
            "SELECT server_id FROM " + SyncEntry.TABLE_NAME +
                " WHERE " +
                SyncEntry.COLUMN_SERVER_ID + " = " + serverId + " AND " +
                SyncEntry.COLUMN_TABLE_NAME + " = '" + tableName + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public long setSyncId(long localId, String tableName, long serverId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SyncEntry.COLUMN_SERVER_ID, serverId);

        return db.update(SyncEntry.TABLE_NAME, values, SyncEntry.COLUMN_LOCAL_ID + " = ? AND " + SyncEntry.COLUMN_TABLE_NAME + " = ?", new String[] { String.valueOf(localId), tableName });
    }

    public long addSyncId(long localId, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SyncEntry.COLUMN_LOCAL_ID, localId);
        values.put(SyncEntry.COLUMN_TABLE_NAME, tableName);

        return db.insert(SyncEntry.TABLE_NAME, null, values);
    }

    public void deleteSyncEntry(long localId, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SyncEntry.TABLE_NAME, SyncEntry.COLUMN_LOCAL_ID + " = ? AND " + SyncEntry.COLUMN_TABLE_NAME + " = ?", new String[]{localId + "", tableName});
        db.close();
    }
}
