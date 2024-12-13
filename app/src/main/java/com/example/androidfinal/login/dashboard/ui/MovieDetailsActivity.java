package com.example.androidfinal.login.dashboard.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.androidfinal.BuildConfig;
import com.example.androidfinal.R;
import com.example.androidfinal.login.dashboard.api.MovieApiService;
import com.example.androidfinal.login.dashboard.api.RetrofitClient;
import com.example.androidfinal.login.dashboard.model.MovieDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView poster;
    private TextView title, overview, genres, releaseDate, rating;
    private Button btnRate, btnFavorite;

    private MovieApiService apiService;

    private int movieId;

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        poster = findViewById(R.id.moviePoster);
        title = findViewById(R.id.movieTitle);
        overview = findViewById(R.id.movieOverview);
        genres = findViewById(R.id.movieGenres);
        releaseDate = findViewById(R.id.movieReleaseDate);
        rating = findViewById(R.id.movieRating);
        btnRate = findViewById(R.id.btnRate);
        btnFavorite = findViewById(R.id.btnFavorite);

        apiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("MOVIE_ID")){
            movieId = intent.getIntExtra("MOVIE_ID", 0);
            fetchMovieDetails(movieId);
        }

        btnRate.setOnClickListener(v -> {
            Intent rateIntent = new Intent(MovieDetailsActivity.this, RatingActivity.class);
            rateIntent.putExtra("MOVIE_ID", movieId);
            startActivity(rateIntent);
        });

        btnFavorite.setOnClickListener(v -> {
            // Implementar lógica para adicionar aos favoritos
            // Por exemplo, salvar no banco de dados Room
            Toast.makeText(this, "Favorito adicionado!", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchMovieDetails(int id){
        Call<MovieDetails> call = apiService.getMovieDetails(id, BuildConfig.TMDB_API_KEY);
        call.enqueue(new Callback<MovieDetails>(){
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response){
                if(response.isSuccessful() && response.body() != null){
                    MovieDetails movie = response.body();
                    title.setText(movie.getTitle());
                    overview.setText(movie.getOverview());
                    releaseDate.setText("Lançamento: " + movie.getRelease_date());
                    rating.setText("Avaliação: " + movie.getVote_average());

                    // Carregar gêneros
                    StringBuilder genresBuilder = new StringBuilder();
                    for(MovieDetails.Genre genre : movie.getGenres()){
                        genresBuilder.append(genre.getName()).append(", ");
                    }
                    if(genresBuilder.length() > 0){
                        genresBuilder.setLength(genresBuilder.length() - 2); // Remover última vírgula
                    }
                    genres.setText("Gêneros: " + genresBuilder.toString());

                    Glide.with(MovieDetailsActivity.this)
                            .load(IMAGE_BASE_URL + movie.getPosterPath())
                            .placeholder(R.drawable.placeholder_background)
                            .into(poster);
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Erro ao carregar detalhes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t){
                Toast.makeText(MovieDetailsActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }
}