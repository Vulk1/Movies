package com.example.movies.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movies.model.shortMovieModel;
import com.example.movies.network.APIMoviesService;
import com.example.movies.network.MovieSearchResponse;
import com.example.movies.network.RetroInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecherchesViewModel  extends ViewModel {

    private MutableLiveData<List<shortMovieModel>> shortMoviesList;
    private final String API_KEY = "f8f3592d";

    public RecherchesViewModel() {
        shortMoviesList = new MutableLiveData<>();
    }

    public MutableLiveData<List<shortMovieModel>> getShortMoviesListObserver()
    {
        return shortMoviesList;
    }

    public void makeApiCall(String movieName)
    {
        APIMoviesService apiService = RetroInstance.getRetroClient().create(APIMoviesService.class);
        Call<MovieSearchResponse> call = apiService.getShortMovieList(movieName, API_KEY);
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
