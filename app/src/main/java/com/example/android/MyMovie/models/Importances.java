package com.example.android.MyMovie.models;

import android.content.ContentValues;

import com.example.android.MyMovie.Database.MovieContract;
import com.example.android.MyMovie.models.Movie;


public class Importances {
    public static ContentValues toContentValue(Movie movie) {
        ContentValues Values = new ContentValues();
        Values.put(MovieContract.MovieEntry._ID, movie.getId());
        Values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
        Values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        Values.put(MovieContract.MovieEntry.COLUMN_POSTER_IMAGE, movie.getPosterPath());
        Values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getMovieReleaseDate());
        Values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getRating());
        return Values;
    }
}
