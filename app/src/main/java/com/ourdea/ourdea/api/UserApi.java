package com.ourdea.ourdea.api;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ourdea.ourdea.utilities.RequestQueueSingleton;

import org.json.JSONObject;


public class UserApi {

    public static void getAll(final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (ApiUtilities.SERVER_ADDRESS + "/user/all", successResponse, errorResponse);

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }


    public static void create(final String email, final String name, final String password, final String gcmId, Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("name", name);
            params.put("password", password);
            params.put("gcmId", gcmId);
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.POST, ApiUtilities.SERVER_ADDRESS + "/user", params, successResponse, errorResponse);

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void login(final String email, final String password, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ApiUtilities.SERVER_ADDRESS + "/login", params, successResponse, errorResponse) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                // Store session id for future API call
                String sessionId = response.headers.get("Set-Cookie");
                ApiUtilities.Session.storeSession(sessionId, context);

                return super.parseNetworkResponse(response);
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}
