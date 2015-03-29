package com.ourdea.ourdea.activities;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.dto.LocationDto;
import com.ourdea.ourdea.resources.ApiUtilities;
import com.ourdea.ourdea.resources.ProjectResource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;
    private int mProjectId;

    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private Button btn_update;

    private GoogleApiClient mGoogleApiClient;
    private Location mGoogleLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mContext = this;
        mProjectId = ApiUtilities.Session.getProjectId(getApplicationContext()).intValue();
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mMapFragment.getMapAsync(this);

        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
        buildGoogleApiClient();
    }

    private void loadMap (){
        ProjectResource.getLocations(mProjectId, this, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int length = response.length();
                    LatLng zoomLocation = null;
                    for (int i = 0; i < length; i++) {
                        LocationDto newLocation = new LocationDto(mContext, response.getJSONObject(i));
                        LatLng location = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
                        if (newLocation.getLatitude() != 0 || newLocation.getLongitude() != 0){
                            mMap.addMarker(new MarkerOptions().position(location).title(newLocation.getName()));
                            zoomLocation = location;
                        }
                    }
                    if (zoomLocation != null){
                        moveToCurrentLocation(zoomLocation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TESTING", "error: " + error.getMessage());
            }
        });
    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }

    @Override
    public void onClick(View v) {
        if (v.equals(btn_update)){
            mGoogleLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mGoogleLocation != null){
                final double latitude = mGoogleLocation.getLatitude();
                final double longitude = mGoogleLocation.getLongitude();
                ProjectResource.updateLocation(mProjectId, latitude, longitude, this, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TESTING", "location: " + mGoogleLocation.getLatitude() + ", " + mGoogleLocation.getLongitude());
                        loadMap();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TESTING", "error: " + error.getMessage());
                    }
                });
            } else {
                Toast.makeText(this, "Unable to get location data", Toast.LENGTH_LONG).show();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadMap();
    }
}
