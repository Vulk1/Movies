package com.example.movies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

public class Recherches extends AppCompatActivity implements MoviesRecyclerViewAdapter.ItemClickListener {

    private MoviesRecyclerViewAdapter moviesAdapter;
    private JSONArray moviesDetails;
    private List<Map<String, String>> moviesInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.recherches_layout);

        Intent intent = getIntent();
        String moviesStr = intent.getStringExtra("movies");

        try {

            JSONObject moviesJson = new JSONObject(moviesStr);
            moviesDetails = moviesJson.getJSONArray("Search");
            moviesInfo = getMoviesInfo(moviesDetails);

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON on recherches 1: \"" + moviesStr + "\"");
        }


        RecyclerView recyclerView = findViewById(R.id.rvMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        moviesAdapter = new MoviesRecyclerViewAdapter(this, moviesInfo);
        moviesAdapter.setClickListener(this);
        recyclerView.setAdapter(moviesAdapter);



        }

    public List< Map<String, String> > getMoviesInfo(JSONArray moviesDetails) {
        List< Map<String, String> > movies = new ArrayList<>();

        for(int i = 0; i < moviesDetails.length(); i++)
        {
            try {
                JSONObject l = (JSONObject) moviesDetails.get(i);
                String movieTitle = l.getString("Title");
                String movieImageUrl = l.getString("Poster");
                String movieId       = l.getString("imdbID");


                Map<String, String> movieMap = new HashMap<String, String>();
                movieMap.put("Title", movieTitle);
                movieMap.put("Url", movieImageUrl);
                movieMap.put("Id", movieId);

                movies.add(movieMap);

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON");
            }

        }
        return movies;
    }

    public void makeRequest(String movieId) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.omdbapi.com/?i="+ movieId+"&apikey=f8f3592d";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {

                           JSONObject jsonResult = new JSONObject(response);

                          if (!jsonResult.getString("Response").equals("False")) {
                              prepareForDisplayMovieDetails(response);
                            }
                            //gerer cas ou erreur
                            //prepareForDisplayMovieDetails(response);
                            } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON on recherches 2: \"" + response + "\"");
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
    public void prepareForDisplayMovieDetails(String results) {
        Intent intent = new Intent(this, MovieFullDetails.class);
        intent.putExtra("movieId", results);

        startActivity(intent);
        //finish();
    }


    @Override
    public void onItemClick(View view, int position) {
        String movieId = moviesAdapter.getItem(position).get("Id");

        makeRequest(movieId);
    }

}

