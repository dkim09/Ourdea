package com.ourdea.ourdea.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.ProjectResource;

import org.json.JSONObject;


public class DashboardActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.setActivity("Dashboard");
        super.onCreate(savedInstanceState);

        TextView dashboardTitle = (TextView) findViewById(R.id.dash_title);
        final TextView dashboardSubTitle = (TextView) findViewById(R.id.dash_subtitle);
        dashboardTitle.setText("Welcome " + ApiUtilities.Session.getUserName(DashboardActivity.this));

        ProjectResource.get(ApiUtilities.Session.getProjectId(this), this, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    dashboardSubTitle.setText("You are in project " + response.getString("name"));
                } catch (Exception exception) {}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SERVER_ERROR", "Could not get project");
            }
        });
    }

}
