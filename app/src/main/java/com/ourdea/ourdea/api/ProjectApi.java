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

import java.util.List;


public class ProjectApi {

    public static void get(final String projectId, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ApiUtilities.SERVER_ADDRESS + "/project/" + projectId, null, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }


    public static void create(final String name, final String password, final List<String> members, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", name);
            params.put("password", password);
            params.put("members", members);
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ApiUtilities.SERVER_ADDRESS + "/project", params, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void join(final String projectId, final String name, final String password, final String userId, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", name);
            params.put("password", password);
            params.put("userId", userId);
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PATCH, ApiUtilities.SERVER_ADDRESS + "/task/" + projectId, params, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void invite(final String projectId, final String name, final String password, final String userId, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", name);
            params.put("password", password);
            params.put("userId", userId);
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PATCH, ApiUtilities.SERVER_ADDRESS + "/task/" + projectId, params, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void update(final String projectId, final String name, final String password, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", name);
            params.put("password", password);
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PATCH, ApiUtilities.SERVER_ADDRESS + "/task/" + projectId, params, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void delete(final String projectId, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.DELETE, ApiUtilities.SERVER_ADDRESS + "/task/" + projectId, null, successResponse, errorResponse) {
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
