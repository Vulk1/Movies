package com.example.movies.adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fasterxml.jackson.databind.ObjectMapper;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movies.R;
import com.example.movies.model.MovieModel;
import com.example.movies.model.shortMovieModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MovieFullDetailsAdapter extends RecyclerView.Adapter<MovieFullDetailsAdapter.ViewHolder>{


    private MovieModel movieModel;
    private Map<String, String> movieAttributsMap;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public MovieFullDetailsAdapter(Context context, MovieModel  data) {
        super();
        this.context = context;
        this.movieModel = data;
        this.movieAttributsMap = new LinkedHashMap<>();
    }

    public void setMovie(MovieModel movieModel)
    {
        this.movieModel = movieModel;
        updateMovieAttributes();
        notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_detail_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        updateMovieAttributes();

        String movieAttributLabel = (new ArrayList<>(movieAttributsMap.keySet())).get(position);
        String movieAttributDescription = movieAttributsMap.get(movieAttributLabel);
        Log.d("hh", "Movie attribut : " + movieAttributsMap.toString());


        holder.attributeLabelView.setText(movieAttributLabel);
        holder.attributeDescriptionView.setText(movieAttributDescription);

    }

    public void updateMovieAttributes()
    {
        movieAttributsMap = new ObjectMapper().convertValue(movieModel, LinkedHashMap.class);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        if(movieAttributsMap != null)
            return movieAttributsMap.size();
        return 0;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView attributeLabelView;
        TextView attributeDescriptionView;

        ViewHolder(View itemView) {
            super(itemView);
            attributeLabelView = itemView.findViewById(R.id.attributeLabel);
            attributeDescriptionView = itemView.findViewById(R.id.attributeDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int position) {
        return (new ArrayList<>(movieAttributsMap.keySet())).get(position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
