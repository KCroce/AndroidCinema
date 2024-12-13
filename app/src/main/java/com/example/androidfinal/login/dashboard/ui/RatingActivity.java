package com.example.androidfinal.login.dashboard.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.androidfinal.R;

public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button btnSubmitRating;

    private int movieId;
    // Aqui você pode implementar o armazenamento da avaliação, por exemplo, usando Room ou SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingBar = findViewById(R.id.ratingBar);
        btnSubmitRating = findViewById(R.id.btnSubmitRating);

        if(getIntent() != null && getIntent().hasExtra("MOVIE_ID")){
            movieId = getIntent().getIntExtra("MOVIE_ID", 0);
        }

        btnSubmitRating.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            // Implementar lógica para salvar a avaliação
            Toast.makeText(RatingActivity.this, "Você avaliou com " + rating + " estrelas", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}