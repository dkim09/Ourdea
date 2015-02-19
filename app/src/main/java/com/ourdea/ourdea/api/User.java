package com.ourdea.ourdea.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ourdea.ourdea.utilities.RequestQueueSingleton;

import org.json.JSONObject;


public class User {

    public static void create(final String email, final String name, final String password, Context context, Response.Listener<JSONObject> successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("name", name);
            params.put("password", password);
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.POST, ApiUtilities.SERVER_ADDRESS + "/user", params, successResponse, errorResponse);

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void login(final String email, final String password, Context context, Response.Listener<JSONObject> successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ApiUtilities.SERVER_ADDRESS + "/login", params, successResponse, errorResponse);

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
