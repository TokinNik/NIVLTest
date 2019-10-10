package com.example.nivltest.Net;

import android.util.Log;

import com.example.nivltest.AppModel;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Net implements AppModel.Network
{
    public static final String TAG = "NET";
    private static final  String NASA_KEY = "8YFsEZHG8jVZvXHFH5Fr9HrhR3OkTf7sfSdrx7qc";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final NasaApi nasaApi = retrofit.create(NasaApi.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Net()
    {

    }

    public interface getApodDataCallback
    {
        void onComplete(ApodData apodData);

        void onCompleteError(int code);

        void onFailture();
    }

    @Override
    public void getApodData(getApodDataCallback callback, int year, int month, int day)
    {
        String date;
        Call<ApodData> apodData;

        date = dateFormat.format(new GregorianCalendar(year, month, day).getTime());
        apodData = nasaApi.getApodData(date,false, NASA_KEY);
        apodData.enqueue(new ApodCallback(callback));
    }

    class ApodCallback implements Callback<ApodData>
    {
        private final getApodDataCallback callback;

        ApodCallback(getApodDataCallback callback)
        {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<ApodData> call, Response<ApodData> response) {
            if (response.isSuccessful())
            {
                Log.d(TAG, "onResponse: " + response.body().getDate());
                if(callback != null)
                {
                    callback.onComplete(response.body());
                }
            } else
            {
                Log.d(TAG, "onResponse: err " + response.code() + " " + response.message());
                if(callback != null)
                {
                    callback.onCompleteError(response.code());
                }
            }
        }

        @Override
        public void onFailure(Call<ApodData> call, Throwable t) {
            Log.d(TAG, "onFailure: " + t + " " + retrofit.baseUrl());
            callback.onFailture();
        }
    }

}
