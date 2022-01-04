package com.example.movies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

public class MovieFullDetails extends AppCompatActivity {

    private JSONObject movieDetails;
    private ImageView movieImageView;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.movie_full_details);

        movieImageView = findViewById(R.id.MovieImage);
        titleView = findViewById(R.id.MovieTitle);

        Intent intent = getIntent();
        String movieStr = intent.getStringExtra("movieId");

        try {
            movieDetails = new JSONObject(movieStr);
            String movieImageLink = movieDetails.getString("Poster");
            String movieTitle = movieDetails.getString("Title");

            titleView.setText(movieTitle);

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
            imageLoader.displayImage(movieImageLink, movieImageView);

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON on movie full details: \"" + movieStr + "\"");
        }

    }
}
