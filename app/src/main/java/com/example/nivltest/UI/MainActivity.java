package com.example.nivltest.UI;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

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


public class MainActivity extends AppCompatActivity implements AppModel.UI, ObserveFragment.OnFragmentInteractionListener
{
    public static final  String TAG = "MAIN_ACTIVITY";

    private AppModel.Mediator mediator;

    private RecyclerView recyclerView;
    List<ApodData> list = new ArrayList<>();//todo move to inner class?

    private FragmentManager fragmentManager;
    private OnListInteractionListener listener = new OnListInteractionListener() {
        @Override
        public void OnListInteraction(int position) {
            setObserveFragment(position);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate();

        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Net net = new Net();
        mediator = new Mediator(net);
        mediator.attachUI(this);

        fragmentManager = getSupportFragmentManager();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        recyclerView.setAdapter(new RecyclerViewAdapter(list, listener));

        mediator.onUIQueryApodData(20);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void setObserveFragment(int position)
    {
        recyclerView.setVisibility(View.GONE);
        fragmentManager.beginTransaction()
                .add(R.id.container, new ObserveFragment(list.get(position)), ObserveFragment.TAG)
                .commit();
        getSupportActionBar().hide();
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
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemsUpdate(List<ApodData> list)
    {
        //todo?
    }

    @Override
    public void onBackPressed()
    {
        if (recyclerView.getVisibility() == View.GONE)
        {
            fragmentManager.beginTransaction()
                    .remove(fragmentManager.findFragmentByTag(ObserveFragment.TAG))
                    .commit();
            recyclerView.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        Intent videoIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(videoIntent);
    }

    interface OnListInteractionListener
    {
        void OnListInteraction(int position);
    }
}
