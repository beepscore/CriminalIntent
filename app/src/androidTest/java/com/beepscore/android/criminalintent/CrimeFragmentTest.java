package com.beepscore.android.criminalintent;

import junit.framework.TestCase;

/**
 * Created by stevebaker on 8/8/14.
 */
public class CrimeFragmentTest extends TestCase {

    public void testArithmetic() {
        Integer expected = 3;
        Integer actual = 1 + 1;
        String failMessage = "test failed";
        assertEquals(failMessage, expected, actual);
    }

    public void testCrimeFragmentNewInstance() {
        CrimeFragment crimeFragment = CrimeFragment.newInstance("foo", "bar");
        assertNotNull("CrimeFragment null", crimeFragment);
    }
}
