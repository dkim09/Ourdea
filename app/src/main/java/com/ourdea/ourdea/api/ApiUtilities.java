package com.ourdea.ourdea.api;

import android.content.Context;
import android.content.SharedPreferences;

public class ApiUtilities {

    final static public String SERVER_ADDRESS = "http://318af125.ngrok.com"; // AWS server

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

        public static void storeUser(String email, Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("user", email);
            editor.commit();
        }

        public static String getUser(Context context) {
            return context.getApplicationContext().getSharedPreferences("store", 0).getString("user", "SESSION_USER_BROKEN");
        }
    }

}
