package com.example.nivltest;

import com.example.nivltest.Net.ApodData;
import com.example.nivltest.Net.Net;

import java.util.List;

public interface AppModel
{
    interface UI
    {
        void onItemUpdate(ApodData apodData);

        void onItemsUpdate(List<ApodData> list);
    }

    interface Network
    {
        void getApodData(Net.getApodDataCallback callback, int year, int month, int day);
    }

    interface Mediator
    {
        void onUIQueryApodData(int i);

        void attachUI(AppModel.UI ui);

        void detachUI();
    }
}
