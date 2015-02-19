package com.ourdea.ourdea.utilities;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonObjectParamsRequest extends Request<JSONObject> {

    private Response.Listener<JSONObject> mListener;
    private Map<String, String> mParams;

    public JsonObjectParamsRequest(String url, Map<String, String> params,
                                   Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mListener = responseListener;
        mParams = params;
    }

    public JsonObjectParamsRequest(int method, String url, Map<String, String> params,
                                   Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = responseListener;
        mParams = params;
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return mParams;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d("TESTING", "response: " + jsonString);
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}
