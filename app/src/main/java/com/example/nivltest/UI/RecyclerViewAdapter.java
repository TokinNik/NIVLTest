package com.example.nivltest.UI;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nivltest.Net.ApodData;
import com.example.nivltest.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private List<ApodData> dataList;
    private MainActivity.OnListInteractionListener lisener;

    class ViewHolder extends RecyclerView.ViewHolder
    {
        final View view;
        final TextView titleTextView;
        final TextView dateTextView;
        final ImageView imageView;

        ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.view = itemView;
            titleTextView = itemView.findViewById(R.id.title_textView);
            dateTextView = itemView.findViewById(R.id.date_textView);
            imageView = itemView.findViewById(R.id.imageView);
            //imageView.setVisibility(View.GONE);

        }
    }

    public RecyclerViewAdapter(List<ApodData> data, MainActivity.OnListInteractionListener listener)
    {
        this.dataList = data;
        this.lisener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v;
         v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder holder, final int position)
    {
        holder.titleTextView.setText(dataList.get(position).getTitle());
        holder.dateTextView.setText(dataList.get(position).getDate());
        if (dataList.get(position).getMedia_type().equals("image"))
        {
            Picasso.with(holder.view.getContext())
                    .load(dataList.get(position).getUrl())
                    .placeholder(R.drawable.nasa_logo)//todo set normal image
                    .into(holder.imageView);
        }
        else
        {
            holder.imageView.setImageDrawable(holder.view.getContext().getResources().getDrawable(R.drawable.nasa_logo));
        }

        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                lisener.OnListInteraction(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
