package com.ourdea.ourdea.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ourdea.ourdea.R;

import java.io.IOException;

public class GCMUtil {
    private static final String PROPERTY_APP_VERSION = "appVersion";
    String SENDER_ID = "595565407364";
    String TAG = "TESTING";
    
    String mGcmId;
	GoogleCloudMessaging gcm;
	Context mContext;

    GCMRegistrationListener mCallback;
    // must be implemented by container activity
    public interface GCMRegistrationListener {
        public void onRegistrationComplete(String gcmId);
    }
    
	public GCMUtil (Context context){
		mContext = context;
    	gcm = GoogleCloudMessaging.getInstance(mContext);
	}
    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public String getRegistrationId() {
    	final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(mContext.getString(R.string.PROPERTY_GCM_ID), "");
        if (registrationId.length() <= 0) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
    
    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences() {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return mContext.getSharedPreferences(mContext.getString(R.string.PROPERTY_NAME), Context.MODE_PRIVATE);
    }
    
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private int getAppVersion() {
        try {
            PackageInfo packageInfo = mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    
    public void registerInBackground(GCMRegistrationListener listener) {
        mCallback = listener;
    	new RegisterId ().execute("");
    }
    
//    /**
//     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
//     * or CCS to send messages to your app. Not needed for this demo since the
//     * device sends upstream messages to a server that echoes back the message
//     * using the 'from' address in the message.
//     */
//    private void sendRegistrationIdToBackend() {
//		//showProgressDialog( "",	mContext.getString(R.string.loading_register));
//    	final SharedPreferences prefs = getGCMPreferences();
//        String fbId = prefs.getString(mContext.getString(R.string.PROPERTY_FB_ID), "");
//        String fbName = prefs.getString(mContext.getString(R.string.PROPERTY_FB_NAME), "");
//        String fbGender = prefs.getString(mContext.getString(R.string.PROPERTY_FB_GENDER), "");
//        boolean over18 = prefs.getBoolean(mContext.getString(R.string.PROPERTY_OVER18), true);
//        Map<String, String> params = new HashMap<String, String>();
//        params.put(mContext.getString(R.string.PROPERTY_FB_ID), fbId);
//        params.put(mContext.getString(R.string.PROPERTY_FB_NAME), fbName);
//        params.put(mContext.getString(R.string.PROPERTY_FB_GENDER), fbGender);
//        params.put(mContext.getString(R.string.PROPERTY_OVER18), (over18? "1" : "0"));
//        params.put(mContext.getString(R.string.PROPERTY_GCM_ID), mGcmId);
//        JsonObjectParamsRequest jsObjRequest = new JsonObjectParamsRequest
//                (Request.Method.POST, mContext.getString(R.string.server_register), params, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.d("TESTING", "Registered on server: " + response.toString());
//                            String prop_registered = mContext.getString(R.string.PROPERTY_REGISTERED_SERVER);
//                            final SharedPreferences prefs = getGCMPreferences();
//                            SharedPreferences.Editor editor = prefs.edit();
//                            editor.putBoolean(mContext.getString(R.string.PROPERTY_REGISTERED_SERVER), true);
//                            editor.putInt(mContext.getString(R.string.PROPERTY_LOCATION_ID), response.getInt(mContext.getString(R.string.PROPERTY_LOCATION_ID)));
//                            editor.putBoolean(mContext.getString(R.string.PROPERTY_USER_IS_BANNED), (response.getInt(mContext.getString(R.string.PROPERTY_USER_IS_BANNED)) == 1));
//                            editor.putBoolean(mContext.getString(R.string.PROPERTY_USER_IS_ADMIN), (response.getInt(mContext.getString(R.string.PROPERTY_USER_IS_ADMIN)) == 1));
//                            editor.putInt(mContext.getString(R.string.PROPERTY_USER_LETTER_ID), Integer.parseInt(response.getString(mContext.getString(R.string.PROPERTY_USER_LETTER_ID))));
//                            editor.putInt(mContext.getString(R.string.PROPERTY_USER_COLOR_ID), Integer.parseInt(response.getString(mContext.getString(R.string.PROPERTY_USER_COLOR_ID))));
//                            editor.apply();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        showToast(mContext.getString(R.string.error_no_internet));
//                        error.printStackTrace();
//                        String prop_registered = mContext.getString(R.string.PROPERTY_REGISTERED_SERVER);
//                        final SharedPreferences prefs = getGCMPreferences();
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putBoolean(prop_registered, false);
//                        editor.apply();
//                    }
//                });
//        RequestQueueSingleton.getInstance(mContext).addToRequestQueue(jsObjRequest);
//    }

    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion();
        //Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mContext.getString(R.string.PROPERTY_GCM_ID), regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }
    
	private class RegisterId extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... item) {
			try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(mContext);
                }
                mGcmId = gcm.register(SENDER_ID);
                //msg = "Device registered, registration ID=" + mRegId;

                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                //sendRegistrationIdToBackend();


                // Persist the regID - no need to register again.
                storeRegistrationId(mGcmId);
            } catch (IOException ex) {
            	//Log.d("TESTING", "ERROR: "+ex.getMessage());
                //msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
			return "";
		}
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
            mCallback.onRegistrationComplete(mGcmId);
		}
	}
}