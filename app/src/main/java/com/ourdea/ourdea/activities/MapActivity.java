package com.ourdea.ourdea.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.ProjectResource;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapActivity extends FragmentActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private int mProjectId;

    private Button btn_update;

    private GoogleApiClient mGoogleApiClient;
    private Location mGoogleLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mProjectId = ApiUtilities.Session.getProjectId(getApplicationContext()).intValue();
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
        buildGoogleApiClient();
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

    @Override
    public void onClick(View v) {
        if (v.equals(btn_update)){
            mGoogleLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mGoogleLocation != null){
                ProjectResource.updateLocation(mProjectId, mGoogleLocation.getLatitude(), mGoogleLocation.getLongitude(), this, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TESTING", "location: " + mGoogleLocation.getLatitude() + ", " + mGoogleLocation.getLongitude());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TESTING", "error: " + error.getMessage());
                    }
                });
            } else {
                Log.d("TESTING", "error: no location data");
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        loadMap();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.d("TESTING", "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
