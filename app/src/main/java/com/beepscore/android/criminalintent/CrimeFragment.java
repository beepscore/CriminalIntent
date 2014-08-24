package com.beepscore.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CrimeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CrimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CrimeFragment extends Fragment {

    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_TIME = "time";
    private static final int REQUEST_TIME = 1;
    public static final String EXTRA_CRIME_ID = "com.beepscore.android.criminalintent.crime_id";
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;

    private OnFragmentInteractionListener mListener;

    /**
     * Use fragment arguments to pass in information
     * Reference book Phillips Ch 10 pg 193, 196
     * @return A new instance of fragment CrimeFragment.
     */
    public static CrimeFragment newInstance(UUID crimeId) {

        CrimeFragment fragment = new CrimeFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeFragment() {
        // Required empty public constructor
    }

    // Fragments declare onCreate() public, for use by any Activity
    // Activities declare onCreate() protected
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Fragments inflate their view in onCreateView, not in onCreate
        View rootView = inflater.inflate(R.layout.fragment_crime, container, false);

        configureTitleField(rootView);
        configureDateButton(rootView);
        configureTimeButton(rootView);
        configureCheckBox(rootView);

        return rootView;
    }

    private void configureTitleField(View rootView) {
        mTitleField = (EditText)rootView.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(
                    CharSequence c, int start, int before, int count) {
                mCrime.setTitle(c.toString());
            }

            public void beforeTextChanged(
                    CharSequence c, int start, int count, int after) {
                // This space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // This one too
            }
        });
    }

    private void configureDateButton(View rootView) {
        mDateButton = (Button)rootView.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });
    }

    // ******************************************************
    // ******************************************************
    // FIXME when list shows and user rotates phone app throws NullPointerException
    // ******************************************************
    // ******************************************************
    private void configureTimeButton(View rootView) {
        mTimeButton = (Button)rootView.findViewById(R.id.crime_time_button);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(mCrime.getDate());
                int hour = calendar.get(calendar.HOUR_OF_DAY);
                int minute = calendar.get(calendar.MINUTE);

                TimePickerFragment dialog = TimePickerFragment.newInstance(hour, minute);
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fm, DIALOG_TIME);
            }
        });
    }

    private void configureCheckBox(View rootView) {
        mSolvedCheckBox = (CheckBox)rootView.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // set the crime's solved property
                mCrime.setSolved(isChecked);
            }
        });
    }

    private void updateDate() {
        mDateButton.setText(formattedDateString(mCrime.getDate()));
    }

    // ******************************************************
    // ******************************************************
    // FIXME when list shows and user rotates phone app throws NullPointerException
    // ******************************************************
    // ******************************************************
    private void updateTime() {
        mTimeButton.setText(formattedTimeString(mCrime.getDate()));
    }

    protected String formattedDateString(Date date) {

        if (date == null) {
            return "";
        }

        // example Fri Aug 08 16:51:55 PDT 2014
        // date.toString()

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayOfTheWeek = sdf.format(date);

        DateFormat df = DateFormat.getDateInstance();
        // example dateString Aug 8 2014
        String dateString = df.format(date);

        return (dayOfTheWeek + ", " + dateString);
    }

    protected String formattedTimeString(Date date) {

        if (date == null) {
            return "";
        }

        // Use mm minutes not MM months
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        // TODO consider show am pm
        // http://stackoverflow.com/questions/1154903/why-does-parsing-2300-pm-with-simpledateformathhmm-aa-return-11-a-m
        // example hoursMinutesString 23:59
        String hoursMinutesString = sdf.format(date);
        return hoursMinutesString;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }

        if (requestCode == REQUEST_TIME) {
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0);
            int minute = data.getIntExtra(TimePickerFragment.EXTRA_MINUTE, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mCrime.getDate());
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            mCrime.setDate(calendar.getTime());
            updateTime();
        }
    }

}
