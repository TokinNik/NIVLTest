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

    public Mediator(AppModel.Network net)
    {
        this.net = net;
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
    public void onUIQueryApodData(int i)
    {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -i);

        for (int j = 0; j < i; j++)
        {
            net.getApodData(new Net.getApodDataCallback() {
                @Override
                public void onComplete(ApodData apodData) {
                    ui.onItemUpdate(apodData);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }
}
