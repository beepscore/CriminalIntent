package com.beepscore.android.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;


public class CrimeActivity extends Activity
        implements CrimeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);

        if (fragment == null) {
            fragment = CrimeFragment.newInstance("foo", "bar");
            fm.beginTransaction()
                    // use CrimeFragment factory method newInstance
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    public void onFragmentInteraction(Uri uri) {
    }

}
