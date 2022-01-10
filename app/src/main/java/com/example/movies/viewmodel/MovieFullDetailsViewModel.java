package com.example.movies.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movies.model.MovieModel;
import com.example.movies.model.shortMovieModel;
import com.example.movies.network.APIMoviesService;
import com.example.movies.network.MovieSearchResponse;
import com.example.movies.network.RetroInstance;
import com.example.movies.repository.Movie;
import com.example.movies.repository.MovieRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieFullDetailsViewModel extends AndroidViewModel {

        private MutableLiveData<MovieModel> movieModel;
        private MovieRepository movieRepository;
        private final String API_KEY = "f8f3592d";

        public MovieFullDetailsViewModel(Application application) {
                super(application);
                movieModel = new MutableLiveData<>();
                movieRepository = new MovieRepository(application);
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
                                insertMovieInBase(response.body());
                        }

                        @Override
                        public void onFailure(Call<MovieModel> call, Throwable t) {
                                movieModel.postValue(null);
                        }
                });

        }

        public void insertMovieInBase(MovieModel movieModel) {
                movieRepository.insert(new Movie(movieModel.getImdbID(), movieModel.getTitle(),
                        movieModel.getPoster()));
        }
}
