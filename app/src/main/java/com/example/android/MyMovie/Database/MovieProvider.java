package com.example.android.MyMovie.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.MyMovie.Database.MovieContract.MovieEntry;

public class MovieProvider extends ContentProvider {
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieDbHelper dbHelper;
    // Codes for the UriMatcher //////
    static final int MOVIE = 100;
    static final int MOVIE_ID = 101;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieEntry.TABLE_NAME, MOVIE);
        matcher.addURI(authority, MovieEntry.TABLE_NAME + "/#", MOVIE_ID);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        String type;
        switch (match) {
            case MOVIE:
                type = MovieEntry.CONTENT_TYPE;
                break;
            case MOVIE_ID:
                type = MovieEntry.CONTENT_ITEM_TYPE;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return type;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                cursor = dbHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_ID:
                String movieIdSelection = MovieEntry.TABLE_NAME + "." + MovieEntry._ID + "= ?";
                String[] movieSelectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = dbHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        movieIdSelection,
                        movieSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException();

        }
        if (getContext() != null && getContext().getContentResolver() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri Uri;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (match) {
            case MOVIE:
                long Id = db.insert(MovieEntry.TABLE_NAME, null, values);
                if (Id > 0) {
                    Uri = MovieEntry.buildMovieUri(Id);
                } else {
                    Uri = null;
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        db.close();
        return Uri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int Deleted;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (match) {
            case MOVIE:
                Deleted = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (Deleted != 0) {
            if (getContext() != null && getContext().getContentResolver() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        db.close();
        return Deleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int Updated;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (match) {
            case MOVIE:
                Updated = db.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (Updated != 0) {
            if (getContext() != null && getContext().getContentResolver() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        db.close();
        return Updated;
    }

}
