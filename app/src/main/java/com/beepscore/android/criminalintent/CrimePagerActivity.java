package com.beepscore.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * Created by stevebaker on 8/13/14.
 */
public class CrimePagerActivity extends FragmentActivity {
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);
    }
}
