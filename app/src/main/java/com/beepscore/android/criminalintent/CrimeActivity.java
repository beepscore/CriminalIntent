package com.beepscore.android.criminalintent;

import android.app.Fragment;
import android.net.Uri;

import java.util.UUID;


public class CrimeActivity extends SingleFragmentActivity
        implements CrimeFragment.OnFragmentInteractionListener {

    public void onFragmentInteraction(Uri uri) {
    }

    protected Fragment createFragment() {
        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

}
