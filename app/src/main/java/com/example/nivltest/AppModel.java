package com.example.nivltest;

import com.example.nivltest.Net.ApodData;
import com.example.nivltest.Net.Net;

import java.util.Date;
import java.util.List;

public interface AppModel
{
    interface UI
    {
        void onItemUpdate(ApodData apodData);

        void onItemsUpdate(ApodData apodData);

        void setErrorMessage(int code);

        void setConnectionLostMessage();
    }

    interface Network
    {
        void getApodData(Net.getApodDataCallback callback, int year, int month, int day);
    }

    interface Mediator
    {
        void getSomeApodData(int i);

        void getSingleApodData(int year, int month, int day);

        void attachUI(AppModel.UI ui);

        void detachUI();

        void refresh();
    }
}
