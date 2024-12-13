package com.example.androidfinal.login.dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.androidfinal.R;
import com.example.androidfinal.login.dashboard.model.Movie;
import com.example.androidfinal.login.dashboard.ui.MovieDetailsActivity;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private final Context context;

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        // Configurar os dados do filme
        holder.title.setText(movie.getTitle() != null ? movie.getTitle() : "Título indisponível");
        holder.releaseDate.setText("Lançamento: " + (movie.getReleaseDate() != null ? movie.getReleaseDate() : "N/A"));
        holder.rating.setText(String.format("Nota: %.1f", movie.getRating()));

        Glide.with(context)
                .load(movie.getPoster() != null ? IMAGE_BASE_URL + movie.getPoster() : null)
                .placeholder(R.drawable.placeholder_background)
                .error(R.drawable.error_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.poster);

        // Evento de clique longo para exibir o nome do filme
        holder.itemView.setOnLongClickListener(v -> {
            showMovieNameDialog(movie.getTitle()); // Exibe o diálogo com o nome do filme
            return true; // Retorna true para indicar que o evento foi tratado
        });

        // Ação de clique curto para abrir os detalhes do filme
        holder.itemView.setOnClickListener(v -> {
            if (movie.getId() != 0) {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("MOVIE_ID", movie.getId());
                context.startActivity(intent);
            } else {
                holder.itemView.setClickable(false);
            }
        });
    }


    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView genres;
        TextView releaseDate;
        TextView rating;
        ImageView poster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movieTitle);
            genres = itemView.findViewById(R.id.movieGenres);
            releaseDate = itemView.findViewById(R.id.movieReleaseDate);
            rating = itemView.findViewById(R.id.movieRating);
            poster = itemView.findViewById(R.id.moviePoster);
        }
    }


    private void showMovieNameDialog(String movieName) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Nome do Filme");
        builder.setMessage(movieName != null ? movieName : "Título indisponível");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

}
