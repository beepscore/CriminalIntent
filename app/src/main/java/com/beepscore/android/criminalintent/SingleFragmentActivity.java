package com.beepscore.android.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

/**
 * Created by stevebaker on 8/10/14.
 */

// extends Activity, not FragmentActivity as in book
public abstract class SingleFragmentActivity extends Activity {

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        // FragmentManager manages a list of fragments and a backStack of fragment transactions
        // http://developer.android.com/reference/android/app/FragmentManager.html
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    // use CrimeFragment factory method newInstance
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
