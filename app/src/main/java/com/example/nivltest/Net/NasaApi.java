package com.example.nivltest.Net;

import java.text.DateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaApi
{
    @GET("planetary/apod")
    Call<ApodData> getApodData(@Query("date")String date, @Query("hd") boolean hd, @Query("api_key") String key);
}
