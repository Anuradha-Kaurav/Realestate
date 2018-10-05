package com.demo.realstate.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.realstate.R;

public class AddFragment extends Fragment {

    private static String TAG = HomeFragment.class.getCanonicalName();
    Context context;

    @SuppressLint("ValidFragment")
    public AddFragment(Context context) {
        try {
            this.context = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AddFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_property_fragment, container, false);
        return view;
    }
}