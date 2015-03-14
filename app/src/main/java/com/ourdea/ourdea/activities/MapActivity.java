package com.ourdea.ourdea.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.ProjectResource;

import org.json.JSONArray;

public class MapActivity extends FragmentActivity {

    private int mProjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mProjectId = ApiUtilities.Session.getProjectId(getApplicationContext()).intValue();

        loadMap();
    }

    private void loadMap (){
        ProjectResource.getLocations(mProjectId, this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // got response
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TESTING", "error: " + error.getMessage());
            }
        });
    }
}
