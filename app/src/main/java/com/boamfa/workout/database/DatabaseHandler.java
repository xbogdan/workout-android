package com.boamfa.workout.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.boamfa.workout.classes.Exercise;
import com.boamfa.workout.classes.Track;
import com.boamfa.workout.classes.TrackDay;
import com.boamfa.workout.classes.TrackDayExercise;
import com.boamfa.workout.classes.TrackDayExerciseSet;

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
        db.execSQL(MuscleGroupEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(TrackEntry.SQL_DELETE_ENTRIES);
        db.execSQL(TrackDayEntry.SQL_DELETE_ENTRIES);
        db.execSQL(TrackDayExerciseEntry.SQL_DELETE_ENTRIES);
        db.execSQL(TrackDayExerciseSetEntry.SQL_DELETE_ENTRIES);
        db.execSQL(ExerciseEntry.SQL_DELETE_ENTRIES);
        db.execSQL(MuscleGroupEntry.SQL_DELETE_ENTRIES);

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
        db.execSQL(MuscleGroupEntry.SQL_DELETE_ENTRIES);

        onCreate(db);
    }

    public void print() {
        System.out.println(TrackEntry.SQL_CREATE_ENTRIES);
        System.out.println(TrackDayEntry.SQL_CREATE_ENTRIES);
        System.out.println(TrackDayExerciseEntry.SQL_CREATE_ENTRIES);
        System.out.println(TrackDayExerciseSetEntry.SQL_CREATE_ENTRIES);
        System.out.println(ExerciseEntry.SQL_CREATE_ENTRIES);
        System.out.println(MuscleGroupEntry.SQL_CREATE_ENTRIES);
    }

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

    public void getTrack(long trackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT _id, name FROM " + TrackEntry.TABLE_NAME + " WHERE _id";
    }

    public long addTrack(Track track) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackEntry.COLUMN_NAME, track.name);

        // Inserting Row
        long id = db.insert(TrackEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public long updateTrack(Track track) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackEntry.COLUMN_NAME, track.name);
        return db.update(TrackEntry.TABLE_NAME, values, TrackEntry._ID + " = ?", new String[] { String.valueOf(track.id) });
    }

    public void deleteTrack(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TrackEntry.TABLE_NAME, TrackEntry._ID + " = ?",
                new String[]{id + ""});
        db.close();
    }

    public long addTrackDay(TrackDay trackDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayEntry.COLUMN_DATE, trackDay.date);
        values.put(TrackDayEntry.COLUMN_TRACK_ID, trackDay.trackId);

        // Inserting Row
        long id = db.insert(TrackDayEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

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

    public long updateTrackDay(TrackDay trackDay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayEntry.COLUMN_DATE, trackDay.date);
        return db.update(TrackDayEntry.TABLE_NAME, values, TrackDayEntry._ID + " = ?", new String[] { String.valueOf(trackDay.id) });
    }

    public void deleteTrackDay(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TrackDayEntry.TABLE_NAME, TrackDayEntry._ID + " = ?",
                new String[]{id + ""});
        db.close();
    }

    public long addTrackDayExercise(TrackDayExercise trackDayExercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayExerciseEntry.COLUMN_EXERCISE_ID, trackDayExercise.exerciseId);
        values.put(TrackDayExerciseEntry.COLUMN_TRACK_DAY_ID, trackDayExercise.trackDayId);

        // Inserting Row
        long id = db.insert(TrackDayExerciseEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

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

    public void deleteTrackDayExercise(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TrackDayExerciseEntry.TABLE_NAME, TrackDayExerciseEntry._ID + " = ?",
                new String[]{id + ""});
        db.close();
    }

    public long updateTrackDayExercise(TrackDayExercise trackDayExercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayExerciseEntry.COLUMN_EXERCISE_ID, trackDayExercise.exerciseId);
        return db.update(TrackDayExerciseEntry.TABLE_NAME, values, TrackDayExerciseEntry._ID + " = ?", new String[] { String.valueOf(trackDayExercise.id) });
    }

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
        db.close();
        return id;
    }

    public long updateTrackDayExerciseSet(TrackDayExerciseSet trackDayExerciseSet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackDayExerciseSetEntry.COLUMN_WEIGHT, trackDayExerciseSet.weight);
        values.put(TrackDayExerciseSetEntry.COLUMN_REPS, trackDayExerciseSet.reps);
        return db.update(TrackDayExerciseSetEntry.TABLE_NAME, values, TrackDayExerciseSetEntry._ID + " = ?", new String[] { String.valueOf(trackDayExerciseSet.id) });
    }

    public void deleteTrackDayExerciseSet(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TrackDayExerciseSetEntry.TABLE_NAME, TrackDayExerciseSetEntry._ID + " = ?",
                new String[]{id + ""});
        db.close();
    }

    public long addExercise(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_NAME, name);

        // Inserting Row
        long id = db.insert(ExerciseEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public ArrayList<Exercise> getExercises() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Exercise> list = new ArrayList<Exercise>();

        String selectQuery = "SELECT _id, name FROM " + ExerciseEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Exercise element = new Exercise(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
                list.add(element);
            } while (cursor.moveToNext());
        }

        return list;
    }
}
