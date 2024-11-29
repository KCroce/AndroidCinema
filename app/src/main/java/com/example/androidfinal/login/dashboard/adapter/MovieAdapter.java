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
import com.example.androidfinal.R;
import com.example.androidfinal.login.dashboard.model.Movie;
import com.example.androidfinal.login.dashboard.ui.MovieDetailsActivity;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;

    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }

    public void setMovies(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position){
        Movie movie = movies.get(position);
        holder.title.setText(movie.getTitle());

        // Carregar imagem usando Glide
        Glide.with(context)
                .load(IMAGE_BASE_URL + movie.getPosterPath())
                .placeholder(R.drawable.placeholder_background) // Adicione um drawable placeholder
                .into(holder.poster);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("MOVIE_ID", movie.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return movies != null ? movies.size() : 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView poster;

        public MovieViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.movieTitle);
            poster = itemView.findViewById(R.id.moviePoster);
        }
    }
}