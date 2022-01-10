package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.movies.adapter.PastResearchsAdapter;
import com.example.movies.repository.Movie;
import com.example.movies.viewmodel.MainActivityViewModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements PastResearchsAdapter.ItemClickListener {

    private MainActivityViewModel viewModel;
    private List<Movie> movieList;
    private PastResearchsAdapter adapter;

    private Button searchButton;
    private EditText movieTitleEdit;
    private EditText movieYearEdit;
    private RadioGroup typeOfFiction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        searchButton = findViewById(R.id.searchMovieButton);
        movieTitleEdit = findViewById(R.id.editMovieTitle);
        movieYearEdit = findViewById(R.id.editMovieYear);
        typeOfFiction = findViewById(R.id.radioGroup_typeMovie);

        viewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Collections.reverse(movies);
                movieList = movies;
                adapter.setMoviesList(movies);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.rvPastResearchs);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PastResearchsAdapter(this, movieList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputTitle = movieTitleEdit.getText().toString();
                String inputYear = movieYearEdit.getText().toString();
                String inputType;

                switch (typeOfFiction.getCheckedRadioButtonId()) {
                    case R.id.radioButton_movie:
                        inputType = "movie";
                        break;
                    case R.id.radioButton_series:
                        inputType = "series";
                        break;
                    case R.id.radioButton_episode:
                        inputType = "episode";
                        break;
                    default:
                        inputType = "";
                        break;
                }

                if(!inputTitle.isEmpty() ) {
                    Map<String, String> movieInput = new HashMap<>();
                    movieInput.put("Title", inputTitle);

                    if(!inputYear.isEmpty())
                        movieInput.put("Year", inputYear);
                    if(!inputType.isEmpty())
                        movieInput.put("Type", inputType);

                    sendDataToSearchPage(movieInput);
                }
            }
        });


    }

    public void sendDataToSearchPage(Map<String, String> movieSearchData)
    {
        Intent intent = new Intent(this, Recherches.class);

        for (Map.Entry<String, String> entry : movieSearchData.entrySet()) {
            intent.putExtra(entry.getKey(),  entry.getValue());
        }

        startActivity(intent);
        //finish();
    }


    @Override
    public void onItemClick(View view, int position) {
        String movieId = adapter.getItem(position).getId();

        Intent intent = new Intent(this, MovieFullDetails.class);
        intent.putExtra("imdbID", movieId);

        startActivity(intent);
    }



}