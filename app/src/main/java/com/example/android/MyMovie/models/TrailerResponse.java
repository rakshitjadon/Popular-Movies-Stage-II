package com.example.android.MyMovie.models;

import java.util.List;


public class TrailerResponse {
    private String id;

    private List<Trailer> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }



}
