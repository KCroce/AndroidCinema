package com.example.androidfinal.login.dashboard.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.androidfinal.R;
import com.example.androidfinal.login.dashboard.adapter.MovieAdapter;
import com.example.androidfinal.login.dashboard.model.Movie;

import java.util.ArrayList;
import java.util.List;

// Implementar o uso do banco de dados Room para recuperar os filmes favoritos

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> favoriteMovies = new ArrayList<>();

    // Placeholder: Implementar banco de dados Room para carregar favoritos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerViewFavorites);
        adapter = new MovieAdapter(this, favoriteMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadFavoriteMovies();
    }

    private void loadFavoriteMovies(){
        // Implementar a lógica para carregar filmes favoritos do banco de dados
        // Por exemplo:
        // favoriteMovies = database.movieDao().getAllFavorites();
        // adapter.setMovies(favoriteMovies);
        // Aqui, apenas como exemplo:
        Toast.makeText(this, "Carregar favoritos implementado aqui", Toast.LENGTH_SHORT).show();
    }
}