package com.example.androidfinal.login.dashboard.model;

import com.google.gson.annotations.SerializedName;

public class Movie {
    private int id;
    private String title;
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    // Getters e Setters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}