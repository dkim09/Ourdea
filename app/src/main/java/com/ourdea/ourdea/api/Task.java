package com.ourdea.ourdea.api;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ourdea.ourdea.utilities.RequestQueueSingleton;

import java.util.HashMap;
import java.util.Map;


public class Task {

    public static void getAll(String type, final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (ApiUtilities.SERVER_ADDRESS + "/task/all/" + type, successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                String sessionId = context.getApplicationContext().getSharedPreferences("store", 0).getString("sessionId", "");
                params.put("Cookie", sessionId);

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

}
