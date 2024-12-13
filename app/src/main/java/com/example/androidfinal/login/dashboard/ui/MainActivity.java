package com.example.androidfinal.login.dashboard.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.androidfinal.BuildConfig;
import com.example.androidfinal.R;
import com.example.androidfinal.login.dashboard.adapter.MovieAdapter;
import com.example.androidfinal.login.dashboard.api.MovieApiService;
import com.example.androidfinal.login.dashboard.api.RetrofitClient;
import com.example.androidfinal.login.dashboard.model.Movie;
import com.example.androidfinal.login.dashboard.model.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Importações adicionais

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList = new ArrayList<>();

    private MovieApiService apiService;

    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private static final int TOTAL_PAGES = 100; // TMDB permite até 1000 páginas, mas limitamos por exemplo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MovieAdapter(this, movieList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        apiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);

        loadMovies(currentPage);

        // Implementar paginação (opcional)
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if(!isLoading && !isLastPage){
                        if((visibleItemCount + pastVisibleItems) >= totalItemCount){
                            currentPage++;
                            if(currentPage <= TOTAL_PAGES){
                                loadMovies(currentPage);
                            } else {
                                isLastPage = true;
                            }
                        }
                    }
                }
            }
        });
    }

    private void loadMovies(int page){
        isLoading = true;
        Call<MovieResponse> call = apiService.getPopularMovies(BuildConfig.TMDB_API_KEY, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response){
                if(response.isSuccessful() && response.body() != null){
                    movieList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Erro ao carregar filmes", Toast.LENGTH_SHORT).show();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t){
                Toast.makeText(MainActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });
    }

    // Configurar a barra de busca e menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem favoritesItem = menu.findItem(R.id.action_favorites);
        MenuItem ratingItem = menu.findItem(R.id.action_rating);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Buscar filmes...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                searchMovies(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                // Implementar sugestões se desejar
                return false;
            }
        });

        favoritesItem.setOnMenuItemClickListener(item -> {
            // Abrir FavoritesActivity
            startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
            return true;
        });

        ratingItem.setOnMenuItemClickListener(item -> {
            // Abrir RatingActivity
            startActivity(new Intent(MainActivity.this, RatingActivity.class));
            return true;
        });

        return true;
    }

    private void searchMovies(String query){
        Call<MovieResponse> call = apiService.searchMovies(BuildConfig.TMDB_API_KEY, query, 1);
        call.enqueue(new Callback<MovieResponse>(){
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response){
                if(response.isSuccessful() && response.body() != null){
                    movieList.clear();
                    movieList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Nenhum resultado encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t){
                Toast.makeText(MainActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}