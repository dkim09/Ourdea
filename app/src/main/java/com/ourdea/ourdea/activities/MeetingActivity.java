package com.ourdea.ourdea.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.dto.MeetingDto;
import com.ourdea.ourdea.dto.UserDto;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.MeetingResource;
import com.ourdea.ourdea.resources.ProjectResource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MeetingActivity extends DrawerActivity {

    private MeetingDto activeMeeting;

    private boolean loadingMeetingInformation = false;

    private boolean activeMeetingExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_meeting);
        super.onCreate(savedInstanceState);
        this.setActivity("Meetings");
    }

    @Override
    public void onResume() {
        super.onResume();
        loadActiveMeeting();
    }

    private void loadActiveMeeting() {
        loadingMeetingInformation = true;
        MeetingResource.getAll("active", this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // We'll assume at most one meeting right now
                        try {
                            if (response.length() != 0) {
                                activeMeetingExists = true;
                                activeMeeting = new MeetingDto(response.getJSONObject(0));
                            } else {
                                activeMeetingExists = false;
                            }

                            buildActiveMeeting();
                            loadingMeetingInformation = false;
                        } catch (Exception exception) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Could not get meetings");
                        loadingMeetingInformation = false;
                    }
                });
    }

    private void buildActiveMeeting() {
        // Get references
        TextView meetingActiveEmptyStateTextView = (TextView) findViewById(R.id.meeting_not_active);
        TextView meetingNameTextView = (TextView) findViewById(R.id.meeting_name);
        TextView meetingDescriptionTextView = (TextView) findViewById(R.id.meeting_description);
        TextView meetingLocationTimeTextView = (TextView) findViewById(R.id.meeting_location_time);
        Button acceptMeetingButton = (Button) findViewById(R.id.accept_meeting);
        Button rejectMeetingButton = (Button) findViewById(R.id.reject_meeting);
        final GridLayout usersGridLayout = (GridLayout) findViewById(R.id.users);

        // Reset UI
        meetingNameTextView.setVisibility(View.GONE);
        meetingDescriptionTextView.setVisibility(View.GONE);
        meetingLocationTimeTextView.setVisibility(View.GONE);
        acceptMeetingButton.setVisibility(View.GONE);
        rejectMeetingButton.setVisibility(View.GONE);
        meetingActiveEmptyStateTextView.setVisibility(View.GONE);
        usersGridLayout.setVisibility(View.GONE);
        usersGridLayout.removeAllViews();

        if (activeMeetingExists) {
            final List<String> activeMeetingAgreements = activeMeeting.getAgreements();

            // To be able to reject/accept a meeting:
            // 1) Must not be owner (automatically accept)
            // 2) Must not have already accepted
            boolean userHasNotAccepted = activeMeetingAgreements.indexOf(ApiUtilities.Session.getEmail(this)) == -1;
            if (userHasNotAccepted) {
                acceptMeetingButton.setVisibility(View.VISIBLE);
                rejectMeetingButton.setVisibility(View.VISIBLE);
            }

            // Re-enable some other uI StUFF
            meetingNameTextView.setVisibility(View.VISIBLE);
            meetingDescriptionTextView.setVisibility(View.VISIBLE);
            meetingLocationTimeTextView.setVisibility(View.VISIBLE);
            usersGridLayout.setVisibility(View.VISIBLE);

            meetingNameTextView.setText(activeMeeting.getName());
            meetingDescriptionTextView.setText(activeMeeting.getDescription());

            SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy h:mm a");
            String formattedDateString = formatter.format(activeMeeting.getTime());
            meetingLocationTimeTextView.setText(formattedDateString + " @ " + activeMeeting.getLocation());

            meetingNameTextView.setVisibility(View.VISIBLE);
            meetingDescriptionTextView.setVisibility(View.VISIBLE);
            meetingLocationTimeTextView.setVisibility(View.VISIBLE);

            // Set up grid
            loadingMeetingInformation = true;
            ProjectResource.getMembers(ApiUtilities.Session.getProjectId(this), this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<UserDto> users = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                UserDto user = new UserDto(
                                        jsonObject.getString("email"),
                                        "******",
                                        "-1",
                                        jsonObject.getString("name"));
                                users.add(user);
                            } catch (Exception exception) {
                            }

                            usersGridLayout.setRowCount(users.size());
                            usersGridLayout.setColumnCount(3);

                            int rowIndex = 0;
                            for (UserDto user : users) {
                                // Column 1: Icon
                                GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
                                GridLayout.Spec col = GridLayout.spec(0, 1);
                                GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, col);
                                gridLayoutParam.setGravity(Gravity.LEFT);

                                View iconCell = getLayoutInflater().inflate(R.layout.grid_cell_person_icon, null);
                                usersGridLayout.addView(iconCell, gridLayoutParam);

                                // Column 2: Name + Email
                                col = GridLayout.spec(1, 1);
                                gridLayoutParam = new GridLayout.LayoutParams(row, col);

                                TextView nameCell = (TextView) getLayoutInflater().inflate(R.layout.grid_cell_person_name, null);
                                if (user.getEmail().equals(ApiUtilities.Session.getEmail(getApplicationContext()))) {
                                    nameCell.setText(user.getName() + " (You)" + "\n" + user.getEmail());
                                } else {
                                    nameCell.setText(user.getName() + "\n" + user.getEmail());
                                }
                                usersGridLayout.addView(nameCell, gridLayoutParam);

                                // Column 3: Acceptance/Rejection Icon
                                col = GridLayout.spec(2, 1);
                                gridLayoutParam = new GridLayout.LayoutParams(row, col);
                                gridLayoutParam.setGravity(Gravity.RIGHT);

                                ImageView replyCell = (ImageView) getLayoutInflater().inflate(R.layout.grid_cell_reply_icon, null);
                                if (activeMeetingAgreements.indexOf(user.getEmail()) != -1) {
                                    replyCell.setImageResource(R.drawable.ic_accept);
                                } else {
                                    replyCell.setImageResource(R.drawable.ic_reject);
                                }
                                usersGridLayout.addView(replyCell, gridLayoutParam);

                                rowIndex++;
                            }
                        }

                        loadingMeetingInformation = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Failed to load active meeting", Toast.LENGTH_SHORT).show();
                        loadingMeetingInformation = false;
                    }
                });

        } else {
            meetingActiveEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    public void rejectMeeting(View view) {
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Suggest new time and place");

        final View dialogView = View.inflate(this, R.layout.dialog_reject_meeting, null);
        final EditText message = (EditText) dialogView.findViewById(R.id.message);
        final EditText location = (EditText) dialogView.findViewById(R.id.location);
        location.setText(activeMeeting.getLocation());
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String rejectionMessage = message.getText().toString();
                final MeetingDto newMeeting = new MeetingDto();
                newMeeting.setId(activeMeeting.getId());
                newMeeting.setLocation(location.getText().toString());

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int monthOfYear = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                newMeeting.setTime(calendar.getTimeInMillis());

                                MeetingResource.reject(newMeeting, rejectionMessage, context, new Response.Listener() {
                                       @Override
                                            public void onResponse(Object response) {
                                                Toast.makeText(getApplicationContext(), "Meeting rejected!", Toast.LENGTH_SHORT).show();
                                                loadActiveMeeting();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getApplicationContext(), "Meeting could not be rejected!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }, hour, minute, false).show();
                    }
                }, year, monthOfYear, dayOfMonth).show();
            }
        }).show();
    }

    public void acceptMeeting(View view) {
        MeetingResource.accept(activeMeeting.getId(), this, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Toast.makeText(getApplicationContext(), "Meeting accepted!", Toast.LENGTH_SHORT).show();
                loadActiveMeeting();
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Meeting could not be accepted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_add_meeting) {
            if (loadingMeetingInformation) {
                Toast.makeText(this, "Wait for meeting information to load!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (activeMeetingExists) {
                Toast.makeText(this, "Meeting already being requested", Toast.LENGTH_SHORT).show();
            } else {
                Intent addMeetingActivity = new Intent(this, AddMeetingActivity.class);
                startActivity(addMeetingActivity);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
