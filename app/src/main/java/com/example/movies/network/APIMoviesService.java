package com.example.movies.network;

import com.example.movies.model.MovieModel;
import com.example.movies.model.shortMovieModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIMoviesService {


    @GET(".")
    Call<MovieSearchResponse> getShortMovieList( @Query("s") String movieName, @Query("apikey") String apiKey);

    @GET(".")
    Call<MovieModel> getMovie( @Query("i") String movieId, @Query("apikey") String apiKey);

}

