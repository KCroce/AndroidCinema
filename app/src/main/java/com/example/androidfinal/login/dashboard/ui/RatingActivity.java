package com.example.androidfinal.login.dashboard.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.androidfinal.R;
import com.example.androidfinal.login.dashboard.utils.SharedPreferencesManager;

import java.util.Map;

public class RatingActivity extends AppCompatActivity {

    private static final String TAG = "RatingActivity";

    private RatingBar ratingBar;
    private Button btnSubmitRating;

    private int movieId;
    private SharedPreferencesManager prefsManager;
    private Map<Integer, Float> ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Log.d(TAG, "onCreate: Iniciando RatingActivity");

        // Inicializa SharedPreferences e Rating Map
        prefsManager = new SharedPreferencesManager(this);
        ratings = prefsManager.getRatings();

        // Vincula as views
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);

        // Obtém o ID do filme
        if (getIntent() != null && getIntent().hasExtra("MOVIE_ID")) {
            movieId = getIntent().getIntExtra("MOVIE_ID", 0);
            Log.d(TAG, "Recebido MOVIE_ID: " + movieId);
            if (movieId == 0) {
                Toast.makeText(this, "ID do filme inválido", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "ID do filme é 0");
                finish();
            }
        } else {
            Log.e(TAG, "MOVIE_ID não encontrado no Intent");
            Toast.makeText(this, "Erro ao receber dados do filme", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Carrega classificação existente, se disponível
        if (ratings.containsKey(movieId)) {
            float existingRating = ratings.get(movieId);
            ratingBar.setRating(existingRating); // Preenche as estrelas
            Log.d(TAG, "Rating existente encontrado: " + existingRating);
        } else {
            ratingBar.setRating(0); // Deixa as estrelas vazias
            Log.d(TAG, "Nenhum rating existente para este filme.");
        }

        // Configura ação do botão "Enviar Avaliação"
        btnSubmitRating.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            Log.d(TAG, "Rating submetido: " + rating);

            // Salva o rating no SharedPreferences
            ratings.put(movieId, rating);
            prefsManager.saveRatings(ratings);

            Toast.makeText(RatingActivity.this, "Você avaliou com " + rating + " estrelas", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela após envio
        });
    }
}
