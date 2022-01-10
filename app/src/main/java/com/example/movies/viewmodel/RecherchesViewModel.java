package com.example.movies.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movies.model.shortMovieModel;
import com.example.movies.network.APIMoviesService;
import com.example.movies.network.MovieSearchResponse;
import com.example.movies.network.RetroInstance;
import com.example.movies.repository.Movie;
import com.example.movies.repository.MovieRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecherchesViewModel  extends AndroidViewModel {

    private MutableLiveData<List<shortMovieModel>> shortMoviesList;
    private final String API_KEY = "f8f3592d";

    public RecherchesViewModel(@NonNull Application application) {
        super(application);
        shortMoviesList = new MutableLiveData<>();
    }

    public MutableLiveData<List<shortMovieModel>> getShortMoviesListObserver()
    {
        return shortMoviesList;
    }

    public void makeApiCall(String movieName, String movieType, String movieYear)
    {
        APIMoviesService apiService = RetroInstance.getRetroClient().create(APIMoviesService.class);
        Call<MovieSearchResponse> call = apiService.getShortMovieList(movieName, movieType, movieYear, API_KEY);
        call.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                shortMoviesList.postValue(response.body().getShortMovieModel());
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
                shortMoviesList.postValue(null);
            }
        });

    }

}
