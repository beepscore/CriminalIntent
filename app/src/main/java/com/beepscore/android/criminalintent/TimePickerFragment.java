package com.beepscore.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by stevebaker on 8/23/14.
 * http://developer.android.com/guide/topics/ui/controls/pickers.html
 * http://www.mkyong.com/android/android-time-picker-example/
 */
public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_HOUR = "com.beepscore.android.criminalintent.hour";
    public static final String EXTRA_MINUTE = "com.beepscore.android.criminalintent.minute";
    private int mHour;
    private int mMinute;

    // construct fragment with fragment arguments
    public static TimePickerFragment newInstance(int hour, int minute) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_HOUR, hour);
        args.putSerializable(EXTRA_MINUTE, minute);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOUR, mHour);
        intent.putExtra(EXTRA_MINUTE, mMinute);

        // When communicating between two fragments hosted by the same activity, we can call Fragment.onActivityResult().
        // When communicating between two activities, don't call Activity.onActivityResult(), let ActivityManager do that.
        // Reference Big Nerd Ranch book chapter 12 pg 222.
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mHour = getArguments().getInt(EXTRA_HOUR);
        mMinute = getArguments().getInt(EXTRA_MINUTE);

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date, null);

        TimePicker timePicker = (TimePicker)v.findViewById(R.id.dialog_time_timePicker);
        timePicker.setCurrentHour(mHour);
        timePicker.setCurrentMinute(mMinute);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hour, int minute) {
                // Save the time.
                // If device is rotated, FragmentManager will destroy current fragment instance
                // and create a new one.
                // Could use onSavedInstanceState().
                // Could use retained fragment.
                // Big Nerd Ranch book says DialogFragment has a bug, retained fragment doesn't work
                // Here save state by writing to fragment argument
                mHour = hour;
                getArguments().putSerializable(EXTRA_HOUR, mHour);
                mMinute = minute;
                getArguments().putSerializable(EXTRA_MINUTE, mMinute);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();
    }

}
