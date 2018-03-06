package com.example.android.MyMovie.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Movie implements Parcelable {
    private String poster_path;
    private String overview;
    private String release_date;
    private String id;
    private String original_title;
    private String title;
    private Float vote_average;

    public String getPosterPath() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return "http://image.tmdb.org/t/p/" + "w185" + getPosterPath();
    }
    public String getRating() {
        return String.valueOf(vote_average);    }

    public String getMovieReleaseDate() {
        return  release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Movie(Parcel in) {
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        original_title = in.readString();
        vote_average = in.readFloat();
        id = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(original_title);
        dest.writeFloat(vote_average);
        dest.writeString(id);
    }
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
