package com.example.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.adapter.MoviesShortDetailsAdapter;
import com.example.movies.model.shortMovieModel;
import com.example.movies.viewmodel.RecherchesViewModel;

import java.util.List;

public class Recherches extends AppCompatActivity implements MoviesShortDetailsAdapter.ItemClickListener {

    private MoviesShortDetailsAdapter shortMoviesAdapter;
    private List<shortMovieModel> shortMovieModelList;
    private RecherchesViewModel viewModel;
    private TextView noResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.recherches_layout);

        noResultView = findViewById(R.id.noResultView);

        Intent intent = getIntent();
        String movieTitle = intent.getStringExtra("Title");
        String movieType = intent.getStringExtra("Type");
        String movieYear = intent.getStringExtra("Year");


        viewModel = new ViewModelProvider(this).get(RecherchesViewModel.class);
        viewModel.getShortMoviesListObserver().observe(this, new Observer<List<shortMovieModel>>() {
            @Override
            public void onChanged(List<shortMovieModel> shortMovieModels) {
                if(shortMovieModels != null)
                {
                    shortMovieModelList = shortMovieModels;
                    shortMoviesAdapter.setShortMoviesList(shortMovieModelList);
                    noResultView.setVisibility(View.GONE);
                }
                else {
                    noResultView.setVisibility(View.VISIBLE);
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shortMoviesAdapter = new MoviesShortDetailsAdapter(this, shortMovieModelList);
        shortMoviesAdapter.setClickListener(this);
        recyclerView.setAdapter(shortMoviesAdapter);

        viewModel.makeApiCall(movieTitle, movieType, movieYear);

        }




    @Override
    public void onItemClick(View view, int position) {
        String movieId = shortMoviesAdapter.getItem(position).getMovieId();

        Intent intent = new Intent(this, MovieFullDetails.class);
        intent.putExtra("imdbID", movieId);

        viewModel.insertMovieInBase(shortMoviesAdapter.getItem(position));
        startActivity(intent);
    }

}

