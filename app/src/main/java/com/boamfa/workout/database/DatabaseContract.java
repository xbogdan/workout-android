package com.boamfa.workout.database;

import android.provider.BaseColumns;

/**
 * DatabaseContract
 * database schema
 * Created by bogdan on 20/12/15.
 */
public final class DatabaseContract {


    /**
     * Tracks table
     */
    public static abstract class TrackEntry implements BaseColumns {
        public static final String TABLE_NAME = "tracks";
        public static final String COLUMN_NAME = "name";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT" +
            ")" ;

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    /**
     * Track days table
     */
    public static abstract class TrackDayEntry implements BaseColumns {
        public static final String TABLE_NAME = "track_days";
        public static final String COLUMN_TRACK_ID = "track_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DATE + " DATE, " +
                COLUMN_TRACK_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_TRACK_ID + ") REFERENCES " + TrackEntry.TABLE_NAME + "(" + TrackEntry._ID + ")" +
            ");" ;

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    /**
     * Track day exercises table
     */
    public static abstract class TrackDayExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "track_day_exercises";
        public static final String COLUMN_TRACK_DAY_ID = "track_day_id";
        public static final String COLUMN_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_ORD = "ord";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TRACK_DAY_ID + " INTEGER, " +
                COLUMN_EXERCISE_ID + " INTEGER, " +
                COLUMN_ORD + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_TRACK_DAY_ID + ") REFERENCES " + TrackDayEntry.TABLE_NAME + "(" + TrackDayEntry._ID + ")" +
                "FOREIGN KEY (" + COLUMN_EXERCISE_ID + ") REFERENCES " + ExerciseEntry.TABLE_NAME + "(" + ExerciseEntry._ID + ")" +
            ");" ;

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    /**
     * Track day exercise sets table
     */
    public static abstract class TrackDayExerciseSetEntry implements BaseColumns {
        public static final String TABLE_NAME = "track_day_exercise_sets";
        public static final String COLUMN_TRACK_DAY_EXERCISE_ID = "track_day_exercise_id";
        public static final String COLUMN_REPS = "reps";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_ORD = "ord";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TRACK_DAY_EXERCISE_ID + " INTEGER, " +
                COLUMN_REPS + " INTEGER, " +
                COLUMN_WEIGHT + " FLOAT, " +
                COLUMN_ORD + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_TRACK_DAY_EXERCISE_ID + ") REFERENCES " + TrackDayExerciseEntry.TABLE_NAME + "(" + TrackDayExerciseEntry._ID + ")" +
            ");" ;

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    /**
     * Exercises table
     */
    public static abstract class ExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "exercises";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_NAME = "name";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_NAME + " TEXT " +
            ");" ;

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    /**
     * Exercise muscle groups table
     */
    public static abstract class ExerciseMuscleGroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "exercise_muscle_groups";
        public static final String COLUMN_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_MUSCLE_GROUP_ID = "muscle_group_id";
        public static final String COLUMN_PRIMARY = "primary_group";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_EXERCISE_ID + " INTEGER, " +
                COLUMN_MUSCLE_GROUP_ID + " INTEGER, " +
                COLUMN_PRIMARY + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_EXERCISE_ID + ") REFERENCES " + ExerciseEntry.TABLE_NAME + "(" + ExerciseEntry._ID + ")," +
                "FOREIGN KEY (" + COLUMN_MUSCLE_GROUP_ID + ") REFERENCES " + MuscleGroupEntry.TABLE_NAME + "(" + MuscleGroupEntry._ID + ")" + // TODO add indexes
            ");" ;

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    /**
     * Muscle groups table
     */
    public static abstract class MuscleGroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "muscle_groups";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MUSCLE_GROUP_ID = "muscle_group_id";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_MUSCLE_GROUP_ID + " INTEGER " +
            ");" ;

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    /**
     * Favorite user exercise
     */
    public static abstract class FavoriteUserExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite_user_exercises";
        public static final String COLUMN_EXERCISE_ID = "exercise_id";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_EXERCISE_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_EXERCISE_ID + ") REFERENCES " + ExerciseEntry.TABLE_NAME + "(" + ExerciseEntry._ID + ")" +
            ");" ;

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    /**
     * History table
     */
    public static abstract class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_TABLE_NAME = "table_name";
        public static final String COLUMN_LOCAL_ID = "local_id";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_OPERATION = "operation";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HistoryEntry.TABLE_NAME + " (" +
                HistoryEntry._ID + " INTEGER PRIMARY KEY, " +
                HistoryEntry.COLUMN_TABLE_NAME + " TEXT, " +
                HistoryEntry.COLUMN_LOCAL_ID + " INTEGER, " +
                HistoryEntry.COLUMN_CONTENT + " BLOB, " +
                HistoryEntry.COLUMN_OPERATION + " TEXT, " +
                HistoryEntry.COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + // TODO add index
//                "UNIQUE (" + HistoryEntry.COLUMN_LOCAL_ID + ", " + HistoryEntry.COLUMN_TABLE_NAME + ", " + HistoryEntry.COLUMN_OPERATION + ")" +
            ");";

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HistoryEntry.TABLE_NAME;
    }


    /**
     * SyncTable
     */
    public static abstract class SyncEntry implements BaseColumns {
        public static final String TABLE_NAME = "sync";
        public static final String COLUMN_TABLE_NAME = "table_name";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_LOCAL_ID = "local_id";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SyncEntry.TABLE_NAME + " (" +
                SyncEntry._ID + " INTEGER PRIMARY KEY, " +
                SyncEntry.COLUMN_SERVER_ID + " INTEGER DEFAULT NULL, " +
                SyncEntry.COLUMN_LOCAL_ID + " INTEGER NOT NULL, " +
                SyncEntry.COLUMN_TABLE_NAME + " TEXT, " +
                SyncEntry.COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "UNIQUE (" + SyncEntry.COLUMN_LOCAL_ID + ", " + SyncEntry.COLUMN_TABLE_NAME + ")" +
            ");" +
            "CREATE INDEX local_id_index ON " + SyncEntry.TABLE_NAME + " (" + SyncEntry.COLUMN_LOCAL_ID + ", " + SyncEntry.COLUMN_TABLE_NAME + ")";

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SyncEntry.TABLE_NAME;
    }
}
