package com.ourdea.ourdea.utilities;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class RequestQueueSingleton {
    private static RequestQueueSingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;
    private RetryPolicy mRetryPolicy;

    private RequestQueueSingleton(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();
        mRetryPolicy = new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQueueSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(mRetryPolicy);
        getRequestQueue().add(req);
    }
}
