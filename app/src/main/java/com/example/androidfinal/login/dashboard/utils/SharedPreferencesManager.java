package com.example.androidfinal.login.dashboard.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.androidfinal.login.dashboard.model.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "movie_prefs";
    private static final String KEY_FAVORITES = "favorites";
    private static final String KEY_RATINGS = "ratings";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferencesManager(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Salvar favoritos
    public void saveFavorites(List<Movie> favorites){
        String json = gson.toJson(favorites);
        Log.d("SharedPreferencesManager", "Favoritos salvos: " + json);
        sharedPreferences.edit().putString(KEY_FAVORITES, json).apply();
    }

    // Recuperar favoritos
    public List<Movie> getFavorites(){
        String json = sharedPreferences.getString(KEY_FAVORITES, null);
        if(json != null){
            Type type = new TypeToken<List<Movie>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    // Salvar avaliações (mapa de ID do filme para rating)
    public void saveRatings(Map<Integer, Float> ratings){
        String json = gson.toJson(ratings);
        sharedPreferences.edit().putString(KEY_RATINGS, json).apply();
    }

    // Recuperar avaliações
    public Map<Integer, Float> getRatings(){
        String json = sharedPreferences.getString(KEY_RATINGS, null);
        if(json != null){
            Type type = new TypeToken<Map<Integer, Float>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new HashMap<>();
    }
}
