package com.ourdea.ourdea.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ourdea.ourdea.utilities.RequestQueueSingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TaskApi {

    public static void getAll(String type, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (ApiUtilities.SERVER_ADDRESS + "/task/all/" + type, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void create(final String name, final String description, final String label, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("description", description);
            params.put("name", name);
            params.put("label", label);
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ApiUtilities.SERVER_ADDRESS + "/task", params, successResponse, errorResponse) {
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
