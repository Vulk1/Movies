package com.example.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movies.model.MovieModel;
import com.example.movies.viewmodel.MovieFullDetailsViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class MovieFullDetails extends AppCompatActivity {

    private MovieFullDetailsViewModel viewModel;
    private MovieModel movieModel;
    private ImageView movieImageView;
    private TextView movieTitleView;
    private TextView moviePlotView;
    private LinearLayout movieInfoLayout;
    private TextView noResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.movie_full_details);

        movieImageView = findViewById(R.id.MovieImage);
        movieTitleView = findViewById(R.id.MovieTitle);
        moviePlotView = findViewById(R.id.MoviePlot);
        movieInfoLayout = findViewById(R.id.movieInfoScrollView);
        noResultView = findViewById(R.id.noResultView);

        Intent intent = getIntent();
        String movieId = intent.getStringExtra("imdbID");

        viewModel = new ViewModelProvider(this).get(MovieFullDetailsViewModel.class);
        viewModel.getMovieModelObserver().observe(this, new Observer<MovieModel>() {
            @Override
            public void onChanged(MovieModel newMovieModel) {
                if(newMovieModel != null) {
                    movieModel = newMovieModel;

                    Glide.with(getApplicationContext())
                            .load(movieModel.getPoster())
                            .apply(RequestOptions.centerCropTransform())
                            .into(movieImageView);

                    movieTitleView.setText(movieModel.getTitle());
                    moviePlotView.setText(movieModel.getPlot());

                    Map<String, String> movieAttributesMap = new ObjectMapper().convertValue(movieModel, LinkedHashMap.class);


                    for (Map.Entry<String, String> entry : movieAttributesMap.entrySet()) {
                        if( viewModel.getMovieInfosLabels().contains(entry.getKey()) ) {
                            View child = getLayoutInflater().inflate(R.layout.movie_detail_row, null);
                            movieInfoLayout.addView(child);

                            TextView label = child.findViewById(R.id.attributeLabel);
                            TextView description = child.findViewById(R.id.attributeDescription);

                            String labelStr = entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1);
                            label.setText(labelStr);
                            description.setText(entry.getValue());
                            noResultView.setVisibility(View.GONE);
                        }

                    }
                } else {

                    noResultView.setVisibility(View.VISIBLE);
                }

            }
        });


        viewModel.makeApiCall(movieId);

    }
}
