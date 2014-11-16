package com.beepscore.android.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

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

    private static final String TAG = "CrimeFragment";

    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_TIME = "time";
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_PHOTO = 2;
    private static final int REQUEST_CONTACT = 3;
    public static final String EXTRA_CRIME_ID = "com.beepscore.android.criminalintent.crime_id";

    private static final String DIALOG_IMAGE = "image";

    private Crime mCrime;
    private ImageButton mPhotoButton;
    private Button mSuspectButton;
    private ImageView mPhotoView;
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
        setHasOptionsMenu(true);

        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    // specify TargetApi so Android Lint won't complain for Froyo or Gingerbread
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Fragments inflate their view in onCreateView, not in onCreate
        View rootView = inflater.inflate(R.layout.fragment_crime, container, false);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        // getParentActivityName looks in AndroidManifest.xml
        if (NavUtils.getParentActivityName(getActivity()) != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

        configurePhotoView(rootView);
        configurePhotoButton(rootView);
        configureTitleField(rootView);
        configureDateButton(rootView);
        configureTimeButton(rootView);
        configureCheckBox(rootView);
        configureReportButton(rootView);
        configureSuspectButton(rootView);

        return rootView;
    }

    private void configurePhotoView(View rootView) {
        mPhotoView = (ImageView)rootView.findViewById(R.id.crime_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Photo photo = mCrime.getPhoto();
                if (photo == null) {
                    return;
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fragmentManager, DIALOG_IMAGE);
            }
        });
    }

    private void configurePhotoButton(View rootView) {
        mPhotoButton = (ImageButton)rootView.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });
        disablePhotoButtonIfNoCamera();
    }

    private void disablePhotoButtonIfNoCamera() {
        // If camera is not available, disable camera functionality
        PackageManager packageManager = getActivity().getPackageManager();
        boolean hasACamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD &&
                        Camera.getNumberOfCameras() > 0);
        if (!hasACamera) {
            mPhotoButton.setEnabled(false);
        }
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

    private void configureReportButton(View rootView) {
        Button reportButton = (Button)rootView.findViewById(R.id.crime_report_button);
        reportButton.setOnClickListener(new View.OnClickListener() {
            public  void onClick(View view) {
                // create implicit intent, describes to OS the job intent wants to be done
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                // when implicit intent is used to start activity,
                // as long as more than one activity can handle the intent, show chooser
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });
    }

    private void configureSuspectButton(View rootView) {
        mSuspectButton = (Button)rootView.findViewById(R.id.crime_suspect_button);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // implicit intent. Android OS will find eligible activities
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }
    }

    private void updateDate() {
        mDateButton.setText(formattedDateString(mCrime.getDate()));
    }

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

        java.text.DateFormat df = java.text.DateFormat.getDateInstance();
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
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        if (requestCode == REQUEST_PHOTO) {
            // Create a new Photo object and attach it to the crime
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                Photo photo = new Photo(filename);
                mCrime.setPhoto(photo);
                showPhoto();
            }
        }

        if (requestCode == REQUEST_CONTACT) {

            Uri contactUri = data.getData();

            // Specify which fields you want your query to return values for.
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            // Perform query - here the contactUri is like a "where" clause
            Cursor cursor = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            // if results are empty, return
            if (cursor.getCount() == 0) {
                cursor.close();
                return;
            }

            cursor.moveToFirst();
            final String COLUMN_NAME = "DISPLAY_NAME";
            int columnNameIndex = cursor.getColumnIndex(COLUMN_NAME);
            String suspectName = cursor.getString(columnNameIndex);
            mCrime.setSuspect(suspectName);
            mSuspectButton.setText(suspectName);
            cursor.close();
        }

    }

    private void showPhoto() {
        // (Re)set the image button's image based on our photo
        Photo photo = mCrime.getPhoto();
        BitmapDrawable bitmapDrawable = null;
        if (photo != null) {
            String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
            bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhotoView.setImageDrawable(bitmapDrawable);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

}
