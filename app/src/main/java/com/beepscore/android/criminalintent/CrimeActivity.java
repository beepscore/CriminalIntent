package com.beepscore.android.criminalintent;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;


public class CrimeActivity extends Activity
    implements CrimeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CrimeFragment())
                    .commit();
        }
    }

    public void onFragmentInteraction(Uri uri) {
    }

}
