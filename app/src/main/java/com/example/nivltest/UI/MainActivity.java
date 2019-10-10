package com.example.nivltest.UI;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.util.GregorianCalendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AppModel.UI, ObserveFragment.OnFragmentInteractionListener
{
    public static final  String TAG = "MAIN_ACTIVITY";
    private static final int START_IITEMS_COUNT = 20;
    private AppModel.Mediator mediator;

    private RecyclerView recyclerView;
    private Button updateButton;
    List<ApodData> list = new ArrayList<>();//todo move to inner class?

    private FragmentManager fragmentManager;
    private OnListInteractionListener listener = new OnListInteractionListener() {
        @Override
        public void OnListInteraction(int position) {
            setObserveFragment(list.get(position));
        }
    };
    private MenuItem downloadMoreApodItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        downloadMoreApodItem = menu.findItem(R.id.download_more_apod);

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

        updateButton = findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediator.refresh();
                mediator.getSomeApodData(START_IITEMS_COUNT);
                updateButton.setVisibility(View.GONE);
            }
        });

        mediator.getSomeApodData(START_IITEMS_COUNT);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.download_more_apod:
                mediator.getSomeApodData(START_IITEMS_COUNT/2);
                return true;
            case R.id.get_apod_on_date:
                final DatePicker datePicker = new DatePicker(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(datePicker)
                        .setTitle(R.string.date_picker)
                        .setCancelable(true)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                GregorianCalendar calendarDatePicker = new GregorianCalendar(datePicker.getYear(), datePicker. getMonth(), datePicker.getDayOfMonth());
                                GregorianCalendar calendarToday = new GregorianCalendar();
                                calendarToday.setTime(new Date());

                                if(calendarDatePicker.before(calendarToday))
                                {
                                    mediator.getSingleApodData(datePicker.getYear(), datePicker. getMonth(), datePicker.getDayOfMonth());
                                    dialog.cancel();
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, R.string.date_on_future_err, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void setObserveFragment(ApodData apodData)
    {
        recyclerView.setVisibility(View.GONE);
        fragmentManager.beginTransaction()
                .add(R.id.container, new ObserveFragment(apodData), ObserveFragment.TAG)
                .commit();
        getSupportActionBar().hide();
    }

    @Override
    public void onItemUpdate(ApodData apodData)
    {
        setObserveFragment(apodData);
    }

    @Override
    public void onItemsUpdate(ApodData apodData)
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
    public void setErrorMessage(int code)
    {
        Toast.makeText(this, R.string.download_error,  Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setConnectionLostMessage()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.connection_error)
                .setMessage(R.string.connection_error_message)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                       dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        updateButton.setVisibility(View.VISIBLE);
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
