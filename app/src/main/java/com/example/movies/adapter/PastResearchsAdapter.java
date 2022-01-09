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
import com.example.movies.repository.Movie;

import java.util.List;

public class PastResearchsAdapter extends RecyclerView.Adapter<PastResearchsAdapter.ViewHolder> {

    private List<Movie> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public PastResearchsAdapter(Context context, List<Movie> data) {
        super();
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void setMoviesList(List<Movie> moviesList)
    {
        this.mData = moviesList;
        notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rv_past_research_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.titleView.setText(mData.get(position).getTitle());

        Glide.with(context)
                .load(mData.get(position).getPosterUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.posterView);



    }
    // total number of rows
    @Override
    public int getItemCount() {
        if(mData != null)
            return mData.size();
        return 0;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterView;
        TextView titleView;

        ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.titleView);
            posterView = itemView.findViewById(R.id.posterView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
   public Movie getItem(int position) {
        return mData.get(position);
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
