package com.example.nivltest.UI;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.nivltest.Net.ApodData;
import com.example.nivltest.Net.NasaApi;
import com.example.nivltest.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity
{
    public static final  String TAG = "MAIN_ACTIVITY";
    private static final  String NASA_KEY = "8YFsEZHG8jVZvXHFH5Fr9HrhR3OkTf7sfSdrx7qc";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final NasaApi nasaApi = retrofit.create(NasaApi.class);

    private RecyclerView recyclerView;
    List<ApodData> list = new ArrayList<>();//todo move to inner class?

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        getSomeApodData(20);

        recyclerView.setAdapter(new RecyclerViewAdapter(list));


    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void getSomeApodData(int i)
    {
        for (int j = 1; j < i; j++)
        {
            getApodData(2019, 9, j);
        }
    }

    private void newItemNotify(ApodData apodData)
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
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    void getApodData(int year, int month, int day)
    {
        ApodData returnData = new ApodData();
        String date = new SimpleDateFormat("yyyy-MM-dd")
                .format(new GregorianCalendar(year, month, day).getTime());

        Call<ApodData> apodData = nasaApi.getApodData(date,false, NASA_KEY);
        apodData.enqueue(new Callback<ApodData>() {
            @Override
            public void onResponse(Call<ApodData> call, Response<ApodData> response)
            {
                if(response.isSuccessful())
                {
                    Log.d(TAG, "onResponse: " + response.body().getDate());
                    newItemNotify(response.body());
                }
                else
                {
                    Log.d(TAG, "onResponse: err " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApodData> call, Throwable t)
            {
                Log.d(TAG, "onFailure: " + t + " " + retrofit.baseUrl());
            }
        });
    }


}
