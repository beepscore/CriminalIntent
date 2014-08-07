package com.beepscore.android.criminalintent;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;


public class CrimeActivity extends Activity
    implements CrimeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);
        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                    // use CrimeFragment factory method newInstance
                    .add(R.id.container, CrimeFragment.newInstance("foo", "bar"))
                    .commit();
        }
    }

    public void onFragmentInteraction(Uri uri) {
    }

}
