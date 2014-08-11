package com.beepscore.android.criminalintent;

import android.app.Fragment;
import android.net.Uri;


public class CrimeActivity extends SingleFragmentActivity
        implements CrimeFragment.OnFragmentInteractionListener {

    public void onFragmentInteraction(Uri uri) {
    }

    protected Fragment createFragment() {
        return CrimeFragment.newInstance("foo", "bar");
    }

}
