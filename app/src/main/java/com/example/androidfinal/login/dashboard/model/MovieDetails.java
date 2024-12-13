package com.example.androidfinal.login.dashboard.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetails {
    private int id;
    private String title;
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    private String release_date;

    private double vote_average;

    private List<Genre> genres;

    // Classe interna para gêneros
    public class Genre {
        private int id;
        private String name;

        // Getters e Setters

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

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

    public String getRelease_date() {
        return release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public List<Genre> getGenres() {
        return genres;
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

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}