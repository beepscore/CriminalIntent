package com.beepscore.android.criminalintent;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by stevebaker on 8/25/14.
 */
public class CriminalIntentJSONSerializer {

    private Context mContext;
    private String mFilename;

    public CriminalIntentJSONSerializer(Context context, String filename) {
        mContext = context;
        mFilename = filename;
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try {
            FileInputStream in = null;
            // Open and read the file into a StringBuilder.
            if (downloadsDirectory() != null) {
                File file = new File(downloadsDirectory(), mFilename);
                String pathedFilename = file.getPath();
                in = new FileInputStream(pathedFilename);
            } else {
                in = mContext.openFileInput(mFilename);
            }
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                // Line breaks are omitted and irrelevant
                jsonString.append(line);
            }

            // Parse the JSON using JSONTokener.
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            crimesFromCrimesJSON(crimes, array);

        } catch (FileNotFoundException e) {
            // Starting without a file, so ignore.
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return crimes;
    }

    public void saveCrimes(ArrayList<Crime> crimes)
            throws JSONException, IOException {

        JSONArray array = crimesJSONfromCrimes(crimes);

        // Write the file to disk
        OutputStreamWriter writer = null;
        try {
            FileOutputStream out = null;
            if (downloadsDirectory() != null) {
                File file = new File(downloadsDirectory(), mFilename);
                String pathedFilename = file.getPath();
                out = new FileOutputStream(pathedFilename);
            } else {
                out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            }
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private File downloadsDirectory() {
        File downloadsDirectory = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // SD card available
            downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        return downloadsDirectory;
    }

    private void crimesFromCrimesJSON(ArrayList<Crime> crimes, JSONArray array) throws JSONException {
        for (int i = 0; i <array.length(); ++i) {
            crimes.add(new Crime(array.getJSONObject(i)));
        }
    }

    private JSONArray crimesJSONfromCrimes(ArrayList<Crime> crimes) throws JSONException {
        JSONArray array = new JSONArray();
        for (Crime crime : crimes) {
            array.put(crime.toJSON());
        }
        return array;
    }

}
