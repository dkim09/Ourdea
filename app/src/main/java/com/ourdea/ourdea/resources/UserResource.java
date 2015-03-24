package com.ourdea.ourdea.resources;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ourdea.ourdea.dto.UserDto;
import com.ourdea.ourdea.utilities.RequestQueueSingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UserResource {

    public static void getAll(final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (ApiUtilities.SERVER_ADDRESS + "/user/all", successResponse, errorResponse);

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }


    public static void create(UserDto user, Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("email", user.getEmail());
            params.put("name", user.getName());
            params.put("password", user.getPassword());
            params.put("gcmId", user.getGcmId());
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.POST, ApiUtilities.SERVER_ADDRESS + "/user", params, successResponse, errorResponse);

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void login(final UserDto user, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("email", user.getEmail());
            params.put("password", user.getPassword());
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

    public static void logout(final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ApiUtilities.SERVER_ADDRESS + "/logout", params, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
