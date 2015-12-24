package com.boamfa.workout.database;

import android.provider.BaseColumns;

/**
 * DatabaseContract
 * database schema
 * Created by bogdan on 20/12/15.
 */
public final class DatabaseContract {

    public static abstract class TrackEntry implements BaseColumns {
        public static final String TABLE_NAME = "tracks";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_NAME = "name";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TrackEntry.TABLE_NAME + " (" +
                TrackEntry._ID + " INTEGER PRIMARY KEY, " +
                TrackEntry.COLUMN_SERVER_ID + " INTEGER UNIQUE DEFAULT NULL, " +
                TrackEntry.COLUMN_NAME + " TEXT" +
            ");" +
            "CREATE INDEX tracks_server_id_index ON " + TrackEntry.TABLE_NAME + " (" + TrackEntry.COLUMN_SERVER_ID +");";

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TrackEntry.TABLE_NAME;
    }

    public static abstract class TrackDayEntry implements BaseColumns {
        public static final String TABLE_NAME = "track_days";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_TRACK_ID = "track_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TrackDayEntry.TABLE_NAME + " (" +
                TrackDayEntry._ID + " INTEGER PRIMARY KEY, " +
                TrackDayEntry.COLUMN_SERVER_ID + " INTEGER UNIQUE DEFAULT NULL, " +
                TrackDayEntry.COLUMN_NAME + " TEXT, " +
                TrackDayEntry.COLUMN_DATE + " DATE, " +
                TrackDayEntry.COLUMN_TRACK_ID + " INTEGER ," +
                "FOREIGN KEY (" + TrackDayEntry.COLUMN_TRACK_ID + ") REFERENCES " + TrackEntry.TABLE_NAME + "(" + TrackEntry._ID + ")" +
            ");" +
            "CREATE INDEX track_days_server_id_index ON " + TrackDayEntry.TABLE_NAME + " (" + TrackDayEntry.COLUMN_SERVER_ID +");";

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TrackDayEntry.TABLE_NAME;
    }

    public static abstract class TrackDayExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "track_day_exercises";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_TRACK_DAY_ID = "track_day_id";
        public static final String COLUMN_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_ORD = "ord";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TrackDayExerciseEntry.TABLE_NAME + " (" +
                TrackDayExerciseEntry._ID + " INTEGER PRIMARY KEY, " +
                TrackDayExerciseEntry.COLUMN_SERVER_ID + " INTEGER UNIQUE DEFAULT NULL, " +
                TrackDayExerciseEntry.COLUMN_TRACK_DAY_ID + " INTEGER, " +
                TrackDayExerciseEntry.COLUMN_EXERCISE_ID + " INTEGER, " +
                TrackDayExerciseEntry.COLUMN_ORD + " INTEGER " +
            ");" +
            "CREATE INDEX track_day_exercises_server_id_index ON " + TrackDayExerciseEntry.TABLE_NAME + " (" + TrackDayExerciseEntry.COLUMN_SERVER_ID +");";

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TrackDayExerciseEntry.TABLE_NAME;
    }

    public static abstract class TrackDayExerciseSetEntry implements BaseColumns {
        public static final String TABLE_NAME = "track_day_exercise_sets";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_TRACK_DAY_EXERCISE_ID = "track_day_exercise_id";
        public static final String COLUMN_REPS = "reps";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_ORD = "ord";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TrackDayExerciseSetEntry.TABLE_NAME + " (" +
                TrackDayExerciseSetEntry._ID + " INTEGER PRIMARY KEY, " +
                TrackDayExerciseSetEntry.COLUMN_SERVER_ID + " INTEGER UNIQUE DEFAULT NULL, " +
                TrackDayExerciseSetEntry.COLUMN_TRACK_DAY_EXERCISE_ID + " INTEGER, " +
                TrackDayExerciseSetEntry.COLUMN_REPS + " INTEGER, " +
                TrackDayExerciseSetEntry.COLUMN_WEIGHT + " FLOAT, " +
                TrackDayExerciseSetEntry.COLUMN_ORD + " INTEGER " +
            ");" +
            "CREATE INDEX track_day_exercise_sets_server_id_index ON " + TrackDayExerciseSetEntry.TABLE_NAME + " (" + TrackDayExerciseSetEntry.COLUMN_SERVER_ID +");";

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TrackDayExerciseSetEntry.TABLE_NAME;
    }

    public static abstract class ExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "exercises";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_MUSCLE_GROUP_ID = "muscle_group_id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_NAME = "name";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExerciseEntry.TABLE_NAME + " (" +
                ExerciseEntry._ID + " INTEGER PRIMARY KEY, " +
                ExerciseEntry.COLUMN_SERVER_ID + " INTEGER UNIQUE DEFAULT NULL, " +
                ExerciseEntry.COLUMN_MUSCLE_GROUP_ID + " INTEGER, " +
                ExerciseEntry.COLUMN_USER_ID + " INTEGER, " +
                ExerciseEntry.COLUMN_NAME + " TEXT " +
            ");" +
            "CREATE INDEX exercises_server_id_index ON " + ExerciseEntry.TABLE_NAME + " (" + ExerciseEntry.COLUMN_SERVER_ID +");";

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME;
    }

    public static abstract class MuscleGroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "muscle_groups";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_NAME = "name";

        public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MuscleGroupEntry.TABLE_NAME + " (" +
                MuscleGroupEntry._ID + " INTEGER PRIMARY KEY, " +
                MuscleGroupEntry.COLUMN_SERVER_ID + " INTEGER UNIQUE DEFAULT NULL, " +
                MuscleGroupEntry.COLUMN_NAME + " TEXT " +
            ");" +
            "CREATE INDEX muscle_groups_server_id_index ON " + MuscleGroupEntry.TABLE_NAME + " (" + MuscleGroupEntry.COLUMN_SERVER_ID +");";

        public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MuscleGroupEntry.TABLE_NAME;
    }
}