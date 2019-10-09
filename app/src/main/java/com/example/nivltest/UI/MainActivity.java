package com.example.nivltest.UI;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.nivltest.AppModel;
import com.example.nivltest.Mediator.Mediator;
import com.example.nivltest.Net.ApodData;
import com.example.nivltest.Net.Net;
import com.example.nivltest.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AppModel.UI
{
    public static final  String TAG = "MAIN_ACTIVITY";

    private AppModel.Mediator mediator;

    private RecyclerViewAdapter recyclerViewAdapter;
    List<ApodData> list = new ArrayList<>();//todo move to inner class?

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Net net = new Net();
        mediator = new Mediator(net);
        mediator.attachUI(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerViewAdapter = new RecyclerViewAdapter(list);
        recyclerView.setAdapter(recyclerViewAdapter);

        mediator.onUIQueryApodData(20);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onItemUpdate(ApodData apodData)
    {
        list.add(apodData);
        Collections.sort(list, new Comparator<ApodData>() {
            @Override
            public int compare(ApodData o1, ApodData o2) {
                try
                {
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(o1.getDate());
                    Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(o2.getDate());
                    return date2.compareTo(date1);

                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemsUpdate(List<ApodData> list)
    {
        //todo?
    }
}
