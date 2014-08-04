package com.beepscore.android.criminalintent;

import java.util.UUID;

/**
 * Created by stevebaker on 8/3/14.
 */
public class Crime {

    private UUID mId;
    private String mTitle;

    public Crime() {
        // Generate unique identifier
        mId = UUID.randomUUID();
    }
}
