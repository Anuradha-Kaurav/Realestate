package com.demo.realstate.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.demo.realstate.R;
import com.demo.realstate.activity.HomeActivity;
import com.demo.realstate.activity.SearchActivity;

public class HomeFragment extends Fragment {

    private static String TAG = HomeFragment.class.getCanonicalName();

    Context context;
    RelativeLayout searchLayout;

    @SuppressLint("ValidFragment")
    public HomeFragment(Context context) {

        try {
            Log.i(TAG, "In [HomeFragment] [Paramerized] [Constructor] ::  ");

            this.context = context;

        } catch (Exception e) {
            Log.e(TAG, "Exception in [HomeFragment]  [Paramerized] [Constructor]::  " + e);
            e.printStackTrace();
        }
        // Required empty public constructor
    }

    public HomeFragment() {

        try {
            Log.i(TAG, "In [HomeFragment] [Empty] [Constructor] ::  ");
        } catch (Exception e) {
            Log.e(TAG, "Exception in [HomeFragment] [Empty] [Constructor] ::  " + e);
            e.printStackTrace();
        }

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);


        searchLayout = view.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SearchActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

}
