package com.example.movies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.adapter.MovieFullDetailsAdapter;
import com.example.movies.adapter.MoviesShortDetailsAdapter;
import com.example.movies.model.MovieModel;
import com.example.movies.viewmodel.MovieFullDetailsViewModel;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MovieFullDetails extends AppCompatActivity implements MovieFullDetailsAdapter.ItemClickListener{

    private MovieFullDetailsViewModel viewModel;
    private MovieModel movieModel;
    private MovieFullDetailsAdapter movieAdapter;
    private ImageView movieImageView;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.movie_full_details);

        movieImageView = findViewById(R.id.MovieImage);
        titleView = findViewById(R.id.MovieTitle);

        Intent intent = getIntent();
        String movieId = intent.getStringExtra("imdbID");
        Boolean inDataBase = intent.getBooleanExtra("inDataBase", false);

        viewModel = new ViewModelProvider(this).get(MovieFullDetailsViewModel.class);
        viewModel.getMovieModelObserver().observe(this, new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel newMovieModel) {
                if(newMovieModel != null) {
                    movieModel = newMovieModel;
                    movieAdapter.setMovie(newMovieModel);
                } else {
                    // afficher erreur
                }

            }
        });

        if(inDataBase)
        {
            viewModel.makeBaseCall(movieId);
        }
        else {
            viewModel.makeApiCall(movieId);
        }



        RecyclerView recyclerView = findViewById(R.id.rvMovieDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieFullDetailsAdapter(this, movieModel);
        movieAdapter.setClickListener(this);
        recyclerView.setAdapter(movieAdapter);


    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
