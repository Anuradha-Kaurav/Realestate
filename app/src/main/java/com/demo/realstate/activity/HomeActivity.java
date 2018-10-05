package com.demo.realstate.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.demo.realstate.R;
import com.demo.realstate.adapter.ViewPagerAdapter;
import com.demo.realstate.extra.CommonMethod;
import com.demo.realstate.fragment.HomeFragment;
import com.demo.realstate.fragment.AddFragment;
import com.demo.realstate.fragment.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    static String TAG = HomeActivity.class.getCanonicalName();
    boolean doubleBackToExitPressedOnce = false;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_add_circle,
            R.drawable.ic_person
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        CommonMethod.simpleSnackBar(HomeActivity.this, "Please click BACK again to exit");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void setupViewPager(ViewPager viewPager) {
        try {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext());
            adapter.addFragment(new HomeFragment(getApplicationContext()), "");
            adapter.addFragment(new AddFragment(getApplicationContext()), "");
            adapter.addFragment(new ProfileFragment(getApplicationContext()), "");

            viewPager.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupTabIcons() {
        try {
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);

            tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.yelp), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tab.getIcon().setColorFilter(getResources().getColor(R.color.yelp), PorterDuff.Mode.SRC_IN);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    tab.getIcon().setColorFilter(getResources().getColor(R.color.yelp), PorterDuff.Mode.SRC_IN);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
