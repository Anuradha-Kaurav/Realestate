package com.demo.realstate.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.demo.realstate.fragment.HomeFragment;
import com.demo.realstate.fragment.AddFragment;
import com.demo.realstate.fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static String TAG = ViewPagerAdapter.class.getCanonicalName();
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    Context context;

    public ViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new HomeFragment(context);
            case 1:
                return new AddFragment(context);
            case 2:
                return new ProfileFragment(context);
            default:
                return new HomeFragment(context);
        }

    }


    @Override
    public int getCount() {

        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        try {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
