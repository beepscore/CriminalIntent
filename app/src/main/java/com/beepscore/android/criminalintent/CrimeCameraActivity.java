package com.beepscore.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by stevebaker on 10/14/14.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}