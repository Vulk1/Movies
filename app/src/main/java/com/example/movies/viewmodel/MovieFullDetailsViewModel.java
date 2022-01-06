package com.example.movies.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movies.model.MovieModel;
import com.example.movies.network.APIMoviesService;
import com.example.movies.network.MovieSearchResponse;
import com.example.movies.network.RetroInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFullDetailsViewModel extends ViewModel {

        private MutableLiveData<MovieModel> movieModel;
        private final String API_KEY = "f8f3592d";

        public MovieFullDetailsViewModel() {
                movieModel = new MutableLiveData<>();
        }

        public MutableLiveData<MovieModel> getMovieModelObserver() {
                return movieModel;
        }

        public void makeApiCall(String movieId)
        {
                APIMoviesService apiService = RetroInstance.getRetroClient().create(APIMoviesService.class);
                Call<MovieModel> call = apiService.getMovie(movieId, API_KEY);
                call.enqueue(new Callback<MovieModel>() {
                        @Override
                        public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                                movieModel.postValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<MovieModel> call, Throwable t) {
                                movieModel.postValue(null);
                        }
                });

        }

        public void makeBaseCall(String movieId) {

        }
}
