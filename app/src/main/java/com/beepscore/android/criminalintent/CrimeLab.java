package com.beepscore.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by stevebaker on 8/9/14.
 */
public class CrimeLab {
    private ArrayList<Crime> mCrimes;

    // s prefix is an Android convention for static variable
    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    // constructor is private, so other classes must use get()
    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mCrimes = new ArrayList<Crime>();
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

    public void addCrime (Crime crime) {
        mCrimes.add(crime);
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

}
