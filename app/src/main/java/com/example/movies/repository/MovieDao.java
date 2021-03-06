package com.example.movies.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movie_table")
    LiveData<List<Movie>> getAllMovies();

    @Query("DELETE FROM movie_table")
    void deleteAllMovies();

    @Query("SELECT EXISTS(SELECT * FROM movie_table WHERE id LIKE :movieId)")
    Boolean isMovieExist(String movieId);

}
