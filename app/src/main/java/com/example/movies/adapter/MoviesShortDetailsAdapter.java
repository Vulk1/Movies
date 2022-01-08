package com.example.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movies.R;
import com.example.movies.model.shortMovieModel;

import java.util.List;

public class MoviesShortDetailsAdapter extends RecyclerView.Adapter<MoviesShortDetailsAdapter.ViewHolder> {


    private List<shortMovieModel> shortMoviesList;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public MoviesShortDetailsAdapter(Context context, List<shortMovieModel>  data) {
        super();
        this.context = context;
        this.shortMoviesList = data;
    }

    public void setShortMoviesList(List<shortMovieModel> shortMoviesList)
    {
        this.shortMoviesList = shortMoviesList;
        notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_movies_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = shortMoviesList.get(position).getTitle();
        String imageUrl = shortMoviesList.get(position).getPoster();

        holder.titleView.setText(title);
        Glide.with(context)
                        .load(shortMoviesList.get(position).getPoster())
                        .apply(RequestOptions.centerCropTransform())
                        .into(holder.imageView);


    }
    // total number of rows
    @Override
    public int getItemCount() {
        if(shortMoviesList != null)
            return shortMoviesList.size();

        return 0;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.rvMovieTitle);
            imageView = itemView.findViewById(R.id.rvMovieImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public shortMovieModel getItem(int id) {
        return shortMoviesList.get(id);
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
