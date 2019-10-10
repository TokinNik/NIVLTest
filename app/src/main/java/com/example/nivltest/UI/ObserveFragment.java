package com.example.nivltest.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.nivltest.Net.ApodData;
import com.example.nivltest.R;
import com.squareup.picasso.Picasso;


public class ObserveFragment extends Fragment
{
    public static final String TAG = "OBSERVE_FRAMENT";
    private View view;
    private OnFragmentInteractionListener mListener;
    private ApodData observeApodData;

    public ObserveFragment()
    {

    }

    public ObserveFragment(ApodData apodData)
    {
        this.observeApodData = apodData;
    }

    public static ObserveFragment newInstance()
    {
        ObserveFragment fragment = new ObserveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_obsertve, container, false);
        if (observeApodData != null)
        {
            ((TextView)view.findViewById(R.id.observe_title_textView)).setText(observeApodData.getTitle());
            ((TextView)view.findViewById(R.id.observe_date_textView)).setText(observeApodData.getDate());
            ((TextView)view.findViewById(R.id.observe_content_textView)).setText(observeApodData.getExplanation());

            if (observeApodData.getMedia_type().equals("image"))
            {
                Picasso.with(view.getContext())
                        .load(observeApodData.getUrl())
                        .placeholder(R.drawable.nasa_logo)//todo set normal image
                        .into(((ImageView)view.findViewById(R.id.observe_imageView)));
                view.findViewById(R.id.observe_videoButton).setVisibility(View.GONE);
            }
            else
            {
                ((Button)view.findViewById(R.id.observe_videoButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onFragmentInteraction(Uri.parse(observeApodData.getUrl()));
                    }
                });
            }
        }
        else
        {
            //todo err
        }
        return view;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
