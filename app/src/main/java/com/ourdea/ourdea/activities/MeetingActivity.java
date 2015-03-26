package com.ourdea.ourdea.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.dto.MeetingDto;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.MeetingResource;

import org.json.JSONArray;

public class MeetingActivity extends DrawerActivity {

    private GridLayout gridLayout;
    private MeetingDto meeting;

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

        MeetingResource.getAll(ApiUtilities.Session.getProjectId(getApplicationContext()), "active", this,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // We'll assume at most one meeting right now
                        try {
                            activeMeetingExists = true;
                            meeting = new MeetingDto(response.getJSONObject(0));
                            buildActiveMeeting();
                        } catch (Exception exception) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "Could not get meetings");
                    }
                });
    }

    private void buildActiveMeeting() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            if (activeMeetingExists) {
                Toast.makeText(this, "Meeting already being requested", Toast.LENGTH_SHORT);
            } else {
                Intent addMeetingActivity = new Intent(this, AddMeetingActivity.class);
                startActivity(addMeetingActivity);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
