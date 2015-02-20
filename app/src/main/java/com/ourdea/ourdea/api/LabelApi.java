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

public class LabelApi {

    public static void getAll(final Context context, Response.Listener successResponse, Response.ErrorListener errorResponse) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (ApiUtilities.SERVER_ADDRESS + "/label/all", successResponse, errorResponse) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<>();
                params.put("Cookie", ApiUtilities.Session.getSession(context));

                return params;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

}
