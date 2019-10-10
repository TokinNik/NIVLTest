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
        final TextView contentTextView;
        final ImageView imageView;

        ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.view = itemView;
            titleTextView = itemView.findViewById(R.id.title_textView);
            dateTextView = itemView.findViewById(R.id.date_textView);
            contentTextView = itemView.findViewById(R.id.content_textView);
            contentTextView.setVisibility(View.GONE);
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
        holder.contentTextView.setText(dataList.get(position).getExplanation());
        if (dataList.get(position).getMedia_type().equals("image"))
        {
            Picasso.with(holder.view.getContext())
                    .load(dataList.get(position).getUrl())
                    .placeholder(R.drawable.ic_launcher_background)//todo set normal image
                    .into(holder.imageView);
        }
        else
        {
           // holder.imageView.setImageDrawable(holder.view.getResources().getDrawable(R.drawable.ic_launcher_foreground));
        }

        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (holder.contentTextView.getVisibility() == View.GONE)
                {
                    holder.titleTextView.setBackgroundColor(Color.GREEN);
                    //holder.contentTextView.setVisibility(View.VISIBLE);
                    //holder.imageView.setVisibility(View.VISIBLE);
                    lisener.OnListInteraction(position);
                }
                else
                {
                    holder.titleTextView.setBackgroundColor(Color.YELLOW);
                    //holder.contentTextView.setVisibility(View.GONE);
                    //holder.imageView.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
