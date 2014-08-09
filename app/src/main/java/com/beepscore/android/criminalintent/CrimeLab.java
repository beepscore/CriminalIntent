package com.beepscore.android.criminalintent;

import android.content.Context;

/**
 * Created by stevebaker on 8/9/14.
 */
public class CrimeLab {

    // s prefix is an Android convention for static variable
    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    // constructor is private, so other classes must use get()
    private CrimeLab(Context appContext) {
       mAppContext = appContext;
    }

    // returns a singleton globally available within app
    public static CrimeLab get(Context c) {
        if (sCrimeLab == null) {
            // global singleton should always use application context
            // application context is global within app
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }
}
