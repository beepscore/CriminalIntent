package com.beepscore.android.criminalintent;

import android.net.Uri;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity
    implements CrimeListFragment.OnFragmentInteractionListener {

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.crime_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    public void onFragmentInteraction(Uri uri) {
    }

}
