package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements PastResearchsAdapter.ItemClickListener {

    private PastResearchsAdapter pastResearchsAdapter;
    private List<String> pastResearchs;
    private Button searchButton;
    private EditText movieTitleEdit;
    private EditText movieYearEdit;
    private RadioGroup typeOfFiction;
    private File pastResearchsSaveFile;
    final private String SAVE_FILE_NAME = "pastResearchs.txt";


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

                    makeRequest(movieInput);
                    saveMovieTitle(inputTitle);
                }
            }
        });

        pastResearchsSaveFile = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + SAVE_FILE_NAME);

        if(!isFilePresent(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + SAVE_FILE_NAME)) {
            try{
                pastResearchsSaveFile.createNewFile();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }else {
            pastResearchs = getPastResearchs();


            RecyclerView recyclerView = findViewById(R.id.rvPastResearchs);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            pastResearchsAdapter = new PastResearchsAdapter(this, pastResearchs);
            pastResearchsAdapter.setClickListener(this);
            recyclerView.setAdapter(pastResearchsAdapter);
        }


    }

    public boolean isFilePresent(String fileName) {
        //String path = getApplicationContext().getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(fileName);
        return file.exists();
    }

    public List<String> getPastResearchs()
    {
        List<String> pastResearchs = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + SAVE_FILE_NAME), 8192);// 2nd arg is buffer size

            try {
                String movieTitle;

                while (true){
                    movieTitle = br.readLine();

                    if(movieTitle == null) break;
                    pastResearchs.add(movieTitle);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch(FileNotFoundException e)
        {

        }
        return pastResearchs;

    }

    public void saveMovieTitle(String movieTitle){
        try {
            /*OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE));
            //outputStreamWriter.write(movieTitle);
            outputStreamWriter.append(movieTitle + "\n");
            outputStreamWriter.close();*/

            BufferedWriter writer_contact = new BufferedWriter(new FileWriter(getApplicationContext().getFilesDir().getAbsolutePath() + "/" + SAVE_FILE_NAME, true));
            writer_contact.write(movieTitle);
            writer_contact.newLine();
            writer_contact.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public void makeRequest(Map<String, String> movieInfos) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.omdbapi.com/?i=tt3896198&apikey=f8f3592d";
        url+= "&s=" + movieInfos.get("Title");

        if(movieInfos.containsKey("Year"))
            url+= "&y=" + movieInfos.get("Year");
        if(movieInfos.containsKey("Type"))
            url+= "&type=" + movieInfos.get("Type");

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {

                            JSONObject jsonResult = new JSONObject(response);
                            if (!jsonResult.getString("Response").equals("False")) {
                                prepareForDisplay(response);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Sorry :( Movie " + movieInfos.get("Title") +" not found", Toast.LENGTH_SHORT).show();
                            }

                           // textView.setText("Titre : " + jsonResult.getJSONArray("Search").length());


                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON on main: \"" + response + "\"");
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

    public void prepareForDisplay(String results) {
        Intent intent = new Intent(this, Recherches.class);
        intent.putExtra("movies", results);

        startActivity(intent);
        //finish();
    }

    public void onResume() {

        super.onResume();
        pastResearchs = getPastResearchs();

        RecyclerView recyclerView = findViewById(R.id.rvPastResearchs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pastResearchsAdapter = new PastResearchsAdapter(this, pastResearchs);
        pastResearchsAdapter.setClickListener(this);
        recyclerView.setAdapter(pastResearchsAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Map<String, String> m = new HashMap<String, String>();
        m.put("Title", pastResearchsAdapter.getItem(position));
        makeRequest(m);
    }



}