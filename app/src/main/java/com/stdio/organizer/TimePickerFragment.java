package com.stdio.organizer;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // initialize a new Calendar instance
        final Calendar c = Calendar.getInstance();
        // get the current hour of day from calendar
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        // get the current minute from calendar
        int minute = c.get(Calendar.MINUTE);

        // boolean is24HourView = DateFormat.is24HourFormat(getActivity());
        // initialize a new time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(), // Context
                this, // Listener
                hourOfDay, // hourOfDay
                minute, // Minute
                true // is24HourView
        );

        // return the newly created time picker dialog
        return timePickerDialog;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // get the TextView reference from activity

        // do something with selected time
        String strHour = null;
        String strMinute = null;
        if (String.valueOf(hourOfDay).length() == 1) {
            strHour = "0" + hourOfDay;
        } else {
            strHour = String.valueOf(hourOfDay);
        }
        if (String.valueOf(minute).length() == 1) {
            strMinute = "0" + minute;
        } else {
            strMinute = String.valueOf(minute);
        }
        MainActivity.tvTime.setText("" + strHour + ":" + strMinute);
    }
}