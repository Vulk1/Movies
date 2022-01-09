package com.example.movies.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MovieRepository {

    private MovieDao movieDao;
    private LiveData<List<Movie>> allMovies;

    public MovieRepository(Application application) {
        MovieDataBase dataBase = MovieDataBase.getInstance(application);
        this.movieDao = dataBase.movieDao();
        allMovies = movieDao.getAllMovies();
    }

    public void insert(Movie movie) {
        new InsertMovieAsyncTask(movieDao).execute(movie);
    }

    public void update(Movie movie) {
        new UpdateMovieAsyncTask(movieDao).execute(movie);

    }
    public void delete(Movie movie) {
        new DeleteMovieAsyncTask(movieDao).execute(movie);

    }

    public void deleteAllMovies() {
        new DeleteAllMovieAsyncTask(movieDao).execute();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public static class InsertMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

        InsertMovieAsyncTask(MovieDao movieDao)
        {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.insert(movies[0]);
            return null;
        }
    }

    public static class UpdateMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

        UpdateMovieAsyncTask(MovieDao movieDao)
        {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.update(movies[0]);
            return null;
        }
    }

    public static class DeleteMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

        DeleteMovieAsyncTask(MovieDao movieDao)
        {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.delete(movies[0]);
            return null;
        }
    }

    public static class DeleteAllMovieAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieDao movieDao;

        DeleteAllMovieAsyncTask(MovieDao movieDao)
        {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            movieDao.deleteAllMovies();
            return null;
        }
    }


}
