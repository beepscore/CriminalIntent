package com.beepscore.android.criminalintent;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by stevebaker on 8/8/14.
 */
public class CrimeFragmentTest extends TestCase {

    public void testArithmetic() {
        Integer expected = 3;
        Integer actual = 1 + 2;
        String failMessage = "test failed";
        assertEquals(failMessage, expected, actual);
    }

    public void testCrimeFragmentNewInstance() {
        UUID testUUID = UUID.randomUUID();
        CrimeFragment crimeFragment = CrimeFragment.newInstance(testUUID);
        assertNotNull("CrimeFragment null", crimeFragment);
    }

    public void testFormattedDateStringDateNull() {
        UUID testUUID = UUID.randomUUID();
        CrimeFragment crimeFragment = CrimeFragment.newInstance(testUUID);

        String expected = "";
        String actual = crimeFragment.formattedDateString(null);
        assertEquals("", expected, actual);
    }

    public void testFormattedDateStringDate0() {
        UUID testUUID = UUID.randomUUID();
        CrimeFragment crimeFragment = CrimeFragment.newInstance(testUUID);

        // beginning of UNIX epoch
        Date testDate = new Date(0);
        
        String expected = "Wednesday, Dec 31, 1969";
        String actual = crimeFragment.formattedDateString(testDate);
        assertEquals("", expected, actual);
    }

    public void testFormattedDateString() {
        UUID testUUID = UUID.randomUUID();
        CrimeFragment crimeFragment = CrimeFragment.newInstance(testUUID);

        // set up test by instantiating a test date
        Date testDate = new Date(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String dateStringYearMonthDay = "2011-12-13";

        try {
            testDate = sdf.parse(dateStringYearMonthDay);
        }
        catch (Exception e) {
            //The handling for the code
        }

        // test
        String expected = "Tuesday, Dec 13, 2011";
        String actual = crimeFragment.formattedDateString(testDate);
        assertEquals("", expected, actual);
    }

    public void testFormattedTimeStringDateNull() {
        UUID testUUID = UUID.randomUUID();
        CrimeFragment crimeFragment = CrimeFragment.newInstance(testUUID);

        String expected = "";
        String actual = crimeFragment.formattedTimeString(null);
        assertEquals("", expected, actual);
    }

    public void testFormattedTimeStringDate0() {
        UUID testUUID = UUID.randomUUID();
        CrimeFragment crimeFragment = CrimeFragment.newInstance(testUUID);

        // beginning of UNIX epoch
        Date testDate = new Date(0);

        String expected = "16:00";
        String actual = crimeFragment.formattedTimeString(testDate);
        assertEquals("", expected, actual);
    }

    public void testFormattedTimeString() {
        UUID testUUID = UUID.randomUUID();
        CrimeFragment crimeFragment = CrimeFragment.newInstance(testUUID);

        // set up test by instantiating a test date
        Date testDate = new Date(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.US);
        String testDateString = "2011-12-13-23-59";

        try {
            testDate = sdf.parse(testDateString);
        }
        catch (Exception e) {
            //The handling for the code
        }

        // test
        String expected = "23:59";
        String actual = crimeFragment.formattedTimeString(testDate);
        assertEquals("", expected, actual);
    }

}
