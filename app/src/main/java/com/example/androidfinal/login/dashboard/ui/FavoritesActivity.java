package com.example.androidfinal.login.dashboard.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.androidfinal.R;
import com.example.androidfinal.login.dashboard.adapter.MovieAdapter;
import com.example.androidfinal.login.dashboard.model.Movie;
import com.example.androidfinal.login.dashboard.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> favoriteMovies = new ArrayList<>();
    private SharedPreferencesManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        prefsManager = new SharedPreferencesManager(this);

        recyclerView = findViewById(R.id.recyclerViewFavorites);
        adapter = new MovieAdapter(this, favoriteMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadFavoriteMovies();
    }

    private void loadFavoriteMovies(){
        favoriteMovies = prefsManager.getFavorites();
        if(favoriteMovies.isEmpty()){
            Toast.makeText(this, "Nenhum favorito encontrado", Toast.LENGTH_SHORT).show();
        }
        adapter.setMovies(favoriteMovies);
    }
}
