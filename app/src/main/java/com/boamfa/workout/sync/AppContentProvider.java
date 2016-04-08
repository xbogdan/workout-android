package com.boamfa.workout.sync;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.boamfa.workout.classes.Track;
import com.boamfa.workout.database.DatabaseHandler;

/**
 * Created by bogdan on 21/12/15.
 */
public class AppContentProvider extends ContentProvider {
    // helper constants for use with the UriMatcher
    private static final int TRACK_LIST = 1;
    private static final int TRACK_ID = 2;
    private static final UriMatcher URI_MATCHER;
    private static DatabaseHandler db;

    // prepare the UriMatcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AppContentContract.AUTHORITY, "tracks", TRACK_LIST);
        URI_MATCHER.addURI(AppContentContract.AUTHORITY, "tracks/#", TRACK_ID);
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
//            if (!isInBatchMode()) {
//                 notify all listeners of changes:
//                getContext().
//                        getContentResolver().
//                        notifyChange(itemUri, null);
//            }
            return itemUri;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        db = new DatabaseHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (URI_MATCHER.match(uri)) {
            case TRACK_LIST:
                // TODO complete
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case TRACK_LIST:
                return AppContentContract.Tracks.CONTENT_TYPE;
            case TRACK_ID:
                return AppContentContract.Tracks.CONTENT_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) == TRACK_LIST) {
            long id = db.addTrack(new Track(11, "TEST 101"));
            return getUriForId(id, uri);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
