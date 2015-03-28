package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.ourdea.ourdea.utilities.LoadingSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActiveMeetingActivity extends Activity {

    private MeetingDto activeMeeting;

    private LoadingSpinner activeMeetingLoadingSpinner;

    private TextView meetingNameTextView;
    private TextView meetingDescriptionTextView;
    private TextView meetingLocationTimeTextView;
    private GridLayout usersGridLayout;
    private Menu menu;

    private ArrayList<UserDto> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_meeting);

        // Get references
        meetingNameTextView = (TextView) findViewById(R.id.meeting_name);
        meetingDescriptionTextView = (TextView) findViewById(R.id.meeting_description);
        meetingLocationTimeTextView = (TextView) findViewById(R.id.meeting_location_time);
        usersGridLayout = (GridLayout) findViewById(R.id.users);

        // Set up
        activeMeetingLoadingSpinner = new LoadingSpinner(findViewById(R.id.loading));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadActiveMeeting();
    }

    private void loadActiveMeeting() {
        Long meetingId = this.getIntent().getLongExtra("meetingId", -1);
        if (meetingId != -1) {
            activeMeetingLoadingSpinner.show();
            MeetingResource.get(meetingId, this, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    activeMeeting = new MeetingDto(response);
                    buildActiveMeeting();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    activeMeetingLoadingSpinner.hide();
                    Toast.makeText(ActiveMeetingActivity.this, "Could not load meeting", Toast.LENGTH_SHORT).show();
                    Log.d("SERVER_ERROR", "Could not load meeting");
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    public void buildActiveMeeting() {
        // Reset UI
        meetingNameTextView.setVisibility(View.GONE);
        meetingDescriptionTextView.setVisibility(View.GONE);
        meetingLocationTimeTextView.setVisibility(View.GONE);
        menu.findItem(R.id.action_accept_meeting).setVisible(false);
        menu.findItem(R.id.action_reject_meeting).setVisible(false);
        usersGridLayout.setVisibility(View.GONE);
        usersGridLayout.removeAllViews();

        final List<String> activeMeetingAgreements = activeMeeting.getAgreements();

        // To be able to reject/accept a meeting:
        // 1) Must not be owner (automatically accept)
        // 2) Must not have already accepted
        boolean userHasNotAccepted = activeMeetingAgreements.indexOf(ApiUtilities.Session.getEmail(this)) == -1;
        if (userHasNotAccepted) {
            menu.findItem(R.id.action_accept_meeting).setVisible(true);
            menu.findItem(R.id.action_reject_meeting).setVisible(true);
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
        ProjectResource.getMembers(ApiUtilities.Session.getProjectId(this), this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        users = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                UserDto user = new UserDto(
                                        jsonObject.getString("email"),
                                        "******",
                                        "-1",
                                        jsonObject.getString("name"));
                                users.add(user);
                            } catch (JSONException exception) {
                                Log.d("JSON_ERROR", "Could not parse user json to dto");
                            }

                            buildUsersGridLayout(users);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activeMeetingLoadingSpinner.hide();
                        Toast.makeText(getApplicationContext(), "Failed to load active meeting", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void buildUsersGridLayout(List<UserDto> users) {
        usersGridLayout.removeAllViews();
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
            if (activeMeeting.getAgreements().indexOf(user.getEmail()) != -1) {
                replyCell.setImageResource(R.drawable.ic_accept);
            } else {
                replyCell.setImageResource(R.drawable.ic_action_help);
            }
            usersGridLayout.addView(replyCell, gridLayoutParam);

            rowIndex++;
            activeMeetingLoadingSpinner.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_active_meeting, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_accept_meeting) {
            acceptMeeting();
            return true;
        } else if (id == R.id.action_reject_meeting) {
            rejectMeeting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void rejectMeeting() {
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

                                final ProgressDialog progressDialog = ProgressDialog.show(ActiveMeetingActivity.this, "", "Rejecting meeting...", false, false);

                                MeetingResource.reject(newMeeting, rejectionMessage, context, new Response.Listener() {
                                            @Override
                                            public void onResponse(Object response) {
                                                Toast.makeText(getApplicationContext(), "Meeting rejected!", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Meeting could not be rejected!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                            }
                        }, hour, minute, false).show();
                    }
                }, year, monthOfYear, dayOfMonth).show();
            }
        }).show();
    }

    public void acceptMeeting() {
        final ProgressDialog progressDialog = ProgressDialog.show(ActiveMeetingActivity.this, "", "Accepting meeting...", false, false);

        MeetingResource.accept(activeMeeting.getId(), this, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Meeting accepted!", Toast.LENGTH_SHORT).show();
                        menu.findItem(R.id.action_accept_meeting).setVisible(false);
                        menu.findItem(R.id.action_reject_meeting).setVisible(false);
                        acceptMeetingInGrid();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Meeting could not be accepted!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void acceptMeetingInGrid() {
        activeMeeting.getAgreements().add(ApiUtilities.Session.getEmail(this));
        buildUsersGridLayout(users);
    }
}
