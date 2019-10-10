package com.example.nivltest.Mediator;

import android.util.Log;

import com.example.nivltest.AppModel;
import com.example.nivltest.Net.ApodData;
import com.example.nivltest.Net.Net;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Mediator implements AppModel.Mediator
{
    public static final String TAG = "MEDIATOR";
    private AppModel.UI ui;
    private AppModel.Network net;
    private GregorianCalendar calendar;
    private boolean connectionLost;
    private int lastGetCount;

    public Mediator(AppModel.Network net)
    {
        this.net = net;
        calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        connectionLost = false;
        lastGetCount = 0;
    }

    @Override
    public void attachUI(AppModel.UI ui)
    {
        this.ui = ui;
    }

    @Override
    public void detachUI()
    {
        this.ui = null;
    }

    @Override
    public void refresh()
    {
        if (lastGetCount > 0)
        {
            calendar.add(Calendar.DAY_OF_YEAR, lastGetCount);
        }
        else
        {
            calendar.setTime(new Date());
        }
        connectionLost = false;
        lastGetCount = 0;
    }

    @Override
    public void getSomeApodData(int i)
    {
        lastGetCount = i;
        calendar.add(Calendar.DAY_OF_YEAR, -i);
        for (int j = 0; j < i; j++)
        {
            net.getApodData(new Net.getApodDataCallback() {
                @Override
                public void onComplete(ApodData apodData)
                {
                    ui.onItemsUpdate(apodData);
                    connectionLost = false;
                }

                @Override
                public void onCompleteError(int code) {

                }

                @Override
                public void onFailture()
                {
                    if (!connectionLost)
                    {
                        ui.setConnectionLostMessage();
                        connectionLost = true;
                    }

                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        calendar.add(Calendar.DAY_OF_YEAR, -i);
    }

    @Override
    public void getSingleApodData(int year, int month, int day)
    {
        net.getApodData(new Net.getApodDataCallback() {
            @Override
            public void onComplete(ApodData apodData)
            {
                ui.onItemUpdate(apodData);
                connectionLost = false;
            }

            @Override
            public void onCompleteError(int code)
            {
                ui.setErrorMessage(code);
            }

            @Override
            public void onFailture()
            {
                if (!connectionLost)
                {
                    ui.setConnectionLostMessage();
                    connectionLost = true;
                }
            }
        }, year, month, day);
    }
}
