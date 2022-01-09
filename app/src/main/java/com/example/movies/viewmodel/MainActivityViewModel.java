package com.example.movies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.movies.repository.Movie;
import com.example.movies.repository.MovieRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private LiveData<List<Movie>> allMovies;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        allMovies = movieRepository.getAllMovies();
    }

    public void insert(Movie movie) {
        movieRepository.insert(movie);
    }
    public void update(Movie movie) {
        movieRepository.update(movie);
    }
    public void delete(Movie movie) {
        movieRepository.delete(movie);
    }
    public void deleteAllMovies() {
        movieRepository.deleteAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }
}
