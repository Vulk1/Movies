package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.movies.adapter.PastResearchsAdapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements PastResearchsAdapter.ItemClickListener {

    private Button searchButton;
    private EditText movieTitleEdit;
    private EditText movieYearEdit;
    private RadioGroup typeOfFiction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.searchMovieButton);
        movieTitleEdit = findViewById(R.id.editMovieTitle);
        movieYearEdit = findViewById(R.id.editMovieYear);
        typeOfFiction = findViewById(R.id.radioGroup_typeMovie);

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
        //Map<String, String> m = new HashMap<String, String>();
       // m.put("Title", pastResearchsAdapter.getItem(position));

    }



}