package com.beepscore.android.criminalintent;

import android.content.Context;
import android.test.AndroidTestCase;

import java.util.ArrayList;

/**
 * Created by stevebaker on 8/9/14.
 */
public class CrimeLabTest extends AndroidTestCase {

    public void testCrimeLabGet() {
        // Can't use get(null) because that throws NullPointerException
        // Can't extends TestCase and use MockContext because that throws UnsupportedOperationException
        // http://developer.android.com/tools/testing/testing_android.html#MockObjectClasses
        // http://developer.android.com/reference/android/test/mock/MockContext.html
        // http://stackoverflow.com/questions/5267671/unsupportedoperationexception-while-calling-getsharedpreferences-from-unit-tes
        // Context mockContext = new MockContext();
        // Could stub mockContext.getApplicationContext to avoid UnsupportedOperationException, but this is tedious

        // test class extends AndroidTestCase to use getContext
        // http://stackoverflow.com/questions/2095695/android-unit-tests-requiring-context
        Context testContext = getContext();
        CrimeLab crimeLab = CrimeLab.get(testContext);
        assertNotNull(crimeLab);
    }

    public void testCrimeLabCrimesEmpty() {
        Context testContext = getContext();
        CrimeLab crimeLab = CrimeLab.get(testContext);

        ArrayList<Crime> crimes = crimeLab.getCrimes();
        assert(crimes.isEmpty());
    }

    public void testAddCrime() {
        Context testContext = getContext();
        CrimeLab crimeLab = CrimeLab.get(testContext);

        // TODO: use a mock crime
        Crime testCrime = new Crime();
        crimeLab.addCrime(testCrime);
        ArrayList<Crime> crimes = crimeLab.getCrimes();
        assertEquals("", testCrime, crimes.get(0));
    }

}
