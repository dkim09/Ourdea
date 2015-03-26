package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.ourdea.ourdea.R;
import com.ourdea.ourdea.fragments.DatePickerFragment;
import com.ourdea.ourdea.fragments.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddMeetingActivity extends Activity implements PickerResponse {

    private Calendar meetingDue = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_meeting, menu);
        return true;
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
        newFragment.delegate = this;
    }

    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
        newFragment.delegate = this;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        setFormattedDate();
    }

    private void setFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
        String formattedDateString = formatter.format(meetingDue.getTime());

        EditText editText = (EditText) findViewById(R.id.select_date);
        editText.setText(formattedDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        meetingDue.set(Calendar.HOUR_OF_DAY, hour);
        meetingDue.set(Calendar.MINUTE, minute);

        setFormattedTime();
    }

    private void setFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        String formattedTimeString = formatter.format(meetingDue.getTime());

        EditText editText = (EditText) findViewById(R.id.select_time);
        editText.setText(formattedTimeString);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_meeting) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
