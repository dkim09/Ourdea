package com.ourdea.ourdea.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ourdea.ourdea.R;
import com.ourdea.ourdea.api.UserApi;
import com.ourdea.ourdea.gcm.GCMUtil;

import org.json.JSONObject;


public class ActivityMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerGcm();
    }

    private void registerGcm (){
        GCMUtil gcmUtil = new GCMUtil(this);
        String gcmId = gcmUtil.getRegistrationId();
        if (gcmId.equals("")) {
            Log.d("TESTING", "no GCM");
            gcmUtil.registerInBackground(new GCMUtil.GCMRegistrationListener() {
                @Override
                public void onRegistrationComplete(String gcmId) {
                    createUserAndLogin("bob@email.com", "Bob", "test1234", gcmId);
                }
            });
        } else {
            createUserAndLogin("bob@email.com", "Bob", "test1234", gcmId);
        }
    }

    private void createUserAndLogin(String email, String name, String password, String gcmId) {
        Log.d("TESTING", "gcm: " + gcmId);
        final Context ctx = this;
        UserApi.create(email, name, password, gcmId,
                ctx,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SERVER_SUCCESS", "User created");
                        login();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "User could not be created");

                        // We try to login anyway in case that user exists already
                        login();
                    }
                });
    }

    private void login() {
        final Context ctx = this;
        UserApi.login("bob@email.com", "test1234", this,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SERVER_SUCCESS", "User logged in");
                        Intent intent = new Intent(ctx, TaskActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SERVER_ERROR", "User could not login");
                    }
                });
    }
}
