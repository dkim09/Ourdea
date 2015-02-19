package com.ourdea.ourdea.api;

import android.content.Context;
import android.content.SharedPreferences;

public class ApiUtilities {

    final static public String SERVER_ADDRESS = "http://192.168.1.73:9000";

    public static class Session {

        public static void storeSession(String sessionId, Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("sessionId", sessionId);
            editor.commit();
        }

        public static String getSession(Context context) {
            return context.getApplicationContext().getSharedPreferences("store", 0).getString("sessionId", "SESSION_ID_BROKEN");
        }

    }

}
