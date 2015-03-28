package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.dto.MeetingDto;
import com.ourdea.ourdea.fragments.DatePickerFragment;
import com.ourdea.ourdea.fragments.TimePickerFragment;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.MeetingResource;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddMeetingActivity extends Activity implements PickerResponse {

    private Calendar meetingDue = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        setFormattedDate();
        setFormattedTime();
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
        meetingDue.set(Calendar.YEAR, year);
        meetingDue.set(Calendar.MONTH, month);
        meetingDue.set(Calendar.DAY_OF_MONTH, day);

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
            final Context context = this;
            // Get references
            String meetingName = ((EditText) findViewById(R.id.name)).getText().toString();
            String meetingDescription = ((EditText) findViewById(R.id.description)).getText().toString();
            String meetingLocation = ((EditText) findViewById(R.id.location)).getText().toString();

            if (meetingName.equals("")) {
                Toast.makeText(context, "Meeting needs a name", Toast.LENGTH_SHORT).show();
                return false;
            } else if (meetingDescription.equals("")) {
                Toast.makeText(context, "Meeting needs a description", Toast.LENGTH_SHORT).show();
                return false;
            } else if (meetingLocation.equals("")) {
                Toast.makeText(context, "Meeting needs a location", Toast.LENGTH_SHORT).show();
                return false;
            }

            MeetingDto meeting = new MeetingDto(meetingName, meetingDescription, meetingDue.getTimeInMillis(), meetingLocation, ApiUtilities.Session.getProjectId(this));
            final ProgressDialog progressDialog = ProgressDialog.show(AddMeetingActivity.this, "", "Requesting meeting...", false, false);

            MeetingResource.create(meeting, this, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Meeting requested!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Could not create meeting request (do you have more than 1 person in the project?)", Toast.LENGTH_SHORT).show();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
