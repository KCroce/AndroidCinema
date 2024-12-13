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
import com.example.androidfinal.login.dashboard.model.Movie;
import com.example.androidfinal.login.dashboard.model.MovieDetails;
import com.example.androidfinal.login.dashboard.utils.SharedPreferencesManager;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView poster;
    private TextView title, overview, genres, releaseDate, apiRating, userRating;
    private Button btnRate;
    private com.google.android.material.button.MaterialButton btnFavorite;

    private MovieApiService apiService;

    private int movieId;
    private String moviePosterPath;
    private SharedPreferencesManager prefsManager;

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        prefsManager = new SharedPreferencesManager(this);

        // Inicializando as views
        poster = findViewById(R.id.moviePoster);
        title = findViewById(R.id.movieTitle);
        overview = findViewById(R.id.movieOverview);
        genres = findViewById(R.id.movieGenres);
        releaseDate = findViewById(R.id.movieReleaseDate);
        apiRating = findViewById(R.id.movieRating);
        userRating = findViewById(R.id.userRating);
        btnRate = findViewById(R.id.btnRate);
        btnFavorite = findViewById(R.id.btnFavorite);

        apiService = RetrofitClient.getRetrofitInstance().create(MovieApiService.class);

        // Recebendo o ID do filme
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("MOVIE_ID")) {
            movieId = intent.getIntExtra("MOVIE_ID", 0);
            fetchMovieDetails(movieId);
        } else {
            Toast.makeText(this, "Detalhes do filme não encontrados", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Configurando clique no botão de favoritos
        btnFavorite.setOnClickListener(v -> {
            toggleFavorite();
        });

        btnRate.setOnClickListener(v -> {
            Intent rateIntent = new Intent(MovieDetailsActivity.this, RatingActivity.class);
            rateIntent.putExtra("MOVIE_ID", movieId);
            startActivity(rateIntent);
        });

        updateFavoriteButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayUserRating();
    }

    private void fetchMovieDetails(int id) {
        Call<MovieDetails> call = apiService.getMovieDetails(id, BuildConfig.TMDB_API_KEY);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetails movie = response.body();
                    title.setText(movie.getTitle());
                    overview.setText(movie.getOverview());
                    releaseDate.setText("Lançamento: " + movie.getRelease_date());
                    apiRating.setText("Avaliação API: " + movie.getVote_average());

                    // Configurando gêneros
                    StringBuilder genresBuilder = new StringBuilder();
                    for (MovieDetails.Genre genre : movie.getGenres()) {
                        genresBuilder.append(genre.getName()).append(", ");
                    }
                    if (genresBuilder.length() > 0) {
                        genresBuilder.setLength(genresBuilder.length() - 2); // Remover última vírgula
                    }
                    genres.setText("Gêneros: " + genresBuilder.toString());

                    // Configurando o posterPath
                    moviePosterPath = movie.getPosterPath();

                    Glide.with(MovieDetailsActivity.this)
                            .load(IMAGE_BASE_URL + moviePosterPath)
                            .placeholder(R.drawable.placeholder_background)
                            .into(poster);
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Erro ao carregar detalhes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Toast.makeText(MovieDetailsActivity.this, "Falha na conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleFavorite() {
        List<Movie> favorites = prefsManager.getFavorites();
        boolean isFavorite = false;

        for (Movie movie : favorites) {
            if (movie.getId() == movieId) {
                isFavorite = true;
                favorites.remove(movie); // Remove o filme se já for favorito
                prefsManager.saveFavorites(favorites);
                Toast.makeText(this, "Removido dos favoritos", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        if (!isFavorite) {
            Movie newFavorite = new Movie();
            newFavorite.setId(movieId);
            newFavorite.setTitle(title.getText().toString());
            newFavorite.setPoster(moviePosterPath);
            newFavorite.setOverview(overview.getText().toString());
            newFavorite.setReleaseDate(releaseDate.getText().toString().replace("Lançamento:", ""));
            newFavorite.setRating(Float.parseFloat(apiRating.getText().toString().split(":")[1].trim()));
            favorites.add(newFavorite); // Adiciona o filme como favorito
            prefsManager.saveFavorites(favorites);
            Toast.makeText(this, "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
        }

        updateFavoriteButton(); // Atualiza a aparência do botão
    }

    private void updateFavoriteButton() {
        List<Movie> favorites = prefsManager.getFavorites();
        boolean isFavorite = false;

        for (Movie movie : favorites) {
            if (movie.getId() == movieId) {
                isFavorite = true;
                break;
            }
        }

        if (isFavorite) {
            btnFavorite.setText("Favorito");
            btnFavorite.setIconResource(R.drawable.ic_favorite_filled);
            btnFavorite.setBackgroundTintList(getColorStateList(R.color.colorAccent));
        } else {
            btnFavorite.setText("Favoritos");
            btnFavorite.setIconResource(R.drawable.ic_favorite_border);
            btnFavorite.setBackgroundTintList(getColorStateList(R.color.colorGrey));
        }
    }

    private void displayUserRating() {
        Map<Integer, Float> ratings = prefsManager.getRatings();
        if (ratings.containsKey(movieId)) {
            float userRatingValue = ratings.get(movieId);
            userRating.setText("Sua Avaliação: " + userRatingValue + " estrelas");
        } else {
            userRating.setText("Sua Avaliação: N/A");
        }
    }
}
