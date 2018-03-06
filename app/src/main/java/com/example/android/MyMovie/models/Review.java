package com.example.android.MyMovie.models;


public class Review {
    private String id;
    private String author;
    private String url;
    private String content;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAuthor() {
        return author;
    }
    public String getContent() {
        if(content==null)
            return "No Review Found" ;
            else
            return content;
    }


}
