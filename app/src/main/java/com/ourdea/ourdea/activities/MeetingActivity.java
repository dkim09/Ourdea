package com.ourdea.ourdea.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.adapters.MeetingListAdapter;
import com.ourdea.ourdea.dto.MeetingDto;
import com.ourdea.ourdea.resources.MeetingResource;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.List;

public class MeetingActivity extends DrawerActivity {

    private List<MeetingDto> activeMeetings;
    private List<MeetingDto> upcomingMeetings;

    private AbsListView activeMeetingsListView;
    private AbsListView upcomingMeetingsListView;

    private MeetingListAdapter activeMeetingsListAdapter;
    private MeetingListAdapter upcomingMeetingsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_meeting);
        super.onCreate(savedInstanceState);
        this.setActivity("Meetings");

        // Get references
        activeMeetingsListView = (AbsListView) findViewById(R.id.active_meetings_list);
        upcomingMeetingsListView = (AbsListView) findViewById(R.id.upcoming_meetings_list);
    }

    @Override
    public void onResume() {
        super.onResume();

        findViewById(R.id.meeting_not_active).setVisibility(View.GONE);
        findViewById(R.id.meeting_not_upcoming).setVisibility(View.GONE);
        activeMeetingsListView.setEmptyView(findViewById(R.id.loading1));
        upcomingMeetingsListView.setEmptyView(findViewById(R.id.loading2));

        // Set up of first list
        activeMeetingsListAdapter = new MeetingListAdapter(this);
        activeMeetingsListView.setAdapter(activeMeetingsListAdapter);
        activeMeetingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeetingDto clickedMeeting = activeMeetings.get(position);
                Intent activeMeetingActivity = new Intent(MeetingActivity.this, ActiveMeetingActivity.class);
                activeMeetingActivity.putExtra("meetingId", clickedMeeting.getId());
                startActivity(activeMeetingActivity);
            }
        });

        // Set up of second list
        upcomingMeetingsListAdapter = new MeetingListAdapter(this);
        upcomingMeetingsListView.setAdapter(upcomingMeetingsListAdapter);
        upcomingMeetingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MeetingDto clickedMeeting = upcomingMeetings.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MeetingActivity.this);
                builder.setTitle(clickedMeeting.getName());
                builder.setMessage("Description: " + clickedMeeting.getDescription() +
                        "\n" + "Location: " + clickedMeeting.getLocation() +
                        "\n" + "Time: " + new SimpleDateFormat("d/M/yyyy h:mm a").format(clickedMeeting.getTime()));
                builder .show();
            }
        });

        loadMeetings();
    }

    private void loadMeetings() {
        loadActiveMeetings();
        loadUpcomingMeetings();
    }

    private void loadActiveMeetings() {
        MeetingResource.getAll("active", this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        activeMeetings = MeetingDto.getAllFromJSONArray(response);
                        buildListOfActiveMeetings();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Could not get meetings");
                    }
                });
    }


    private void loadUpcomingMeetings() {
        //upcomingMeetingsSpinner.show();
        MeetingResource.getAll("upcoming", this,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    upcomingMeetings = MeetingDto.getAllFromJSONArray(response);
                    buildListOfUpcomingMeetings();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("SERVER_ERROR", "Could not get upcoming meetings");
                }
            });
    }

    private void buildListOfActiveMeetings() {
        activeMeetingsListAdapter.clear();
        activeMeetingsListAdapter.addAll(activeMeetings);
        activeMeetingsListAdapter.notifyDataSetChanged();

        if (activeMeetings.size() == 0) {
            findViewById(R.id.loading1).setVisibility(View.GONE);
            activeMeetingsListView.setEmptyView(findViewById(R.id.meeting_not_active));
        }
    }

    private void buildListOfUpcomingMeetings() {
        upcomingMeetingsListAdapter.clear();
        upcomingMeetingsListAdapter.addAll(upcomingMeetings);
        upcomingMeetingsListAdapter.notifyDataSetChanged();

        if (upcomingMeetings.size() == 0) {
            findViewById(R.id.loading2).setVisibility(View.GONE);
            upcomingMeetingsListView.setEmptyView(findViewById(R.id.meeting_not_upcoming));
        }
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
            if (activeMeetings.size() == 1) {
                Toast.makeText(this, "Only one active meeting request is supported at the moment", Toast.LENGTH_SHORT).show();
            } else {
                Intent addMeetingActivity = new Intent(this, AddMeetingActivity.class);
                startActivity(addMeetingActivity);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
