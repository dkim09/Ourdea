package com.ourdea.ourdea.resources;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ourdea.ourdea.dto.TaskDto;
import com.ourdea.ourdea.utilities.RequestQueueSingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TaskResource {

    public static void get(final String id, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, ApiUtilities.SERVER_ADDRESS + "/task/" + id, null, successResponse, errorResponse) {
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String>  params = new HashMap<>();
            params.put("Cookie", ApiUtilities.Session.getSession(context));

            return params;
        }
    };

    RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
}

    public static void getAll(String type, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (ApiUtilities.SERVER_ADDRESS + "/project/" + ApiUtilities.Session.getProjectId(context) + "/task/all/" + type, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public static void create(TaskDto task, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("description", task.getDescription());
            params.put("name", task.getName());
            params.put("label", task.getLabel());
            params.put("assignedTo", task.getAssignedTo());
            params.put("status", task.getStatus());
            params.put("dueDate", task.getDueDate());
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ApiUtilities.SERVER_ADDRESS + "/project/" + ApiUtilities.Session.getProjectId(context) + "/task", params, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void update(final String taskId, TaskDto task, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JSONObject params = new JSONObject();
        try {
            params.put("description", task.getDescription());
            params.put("name", task.getName());
            params.put("label", task.getLabel());
            params.put("assignedTo", task.getAssignedTo());
            params.put("dueDate", task.getDueDate());
        } catch (Exception exception) { }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PATCH, ApiUtilities.SERVER_ADDRESS + "/task/" + taskId, params, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void updateStatus(final String taskId, final String newStatus, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ApiUtilities.SERVER_ADDRESS + "/task/" + taskId + "/" + newStatus, null, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    };

    public static void delete(final String taskId, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.DELETE, ApiUtilities.SERVER_ADDRESS + "/task/" + taskId, null, successResponse, errorResponse) {
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
