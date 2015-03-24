package com.ourdea.ourdea.resources;

import android.content.Context;
import android.content.SharedPreferences;

public class ApiUtilities {

    //final static public String SERVER_ADDRESS = "http://52.10.50.18:9000"; // AWS server
    final static public String SERVER_ADDRESS = "http://57187c16.ngrok.com"; // Daniel server

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

        public static void storeUserName(String name, Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", name);
            editor.commit();
        }

        public static String getUserName(Context context) {
            return context.getApplicationContext().getSharedPreferences("store", 0).getString("username", "SESSION_USER_BROKEN");
        }

        public static void storeProjectId(Long projectId, Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong("projectId", projectId);
            editor.commit();
        }

        public static Long getProjectId(Context context) {
            return context.getApplicationContext().getSharedPreferences("store", 0).getLong("projectId", -1);
        }

        public static void storePassword(String password, Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("password", password);
            editor.commit();
        }

        public static String getPassword(Context context) {
            return context.getApplicationContext().getSharedPreferences("store", 0).getString("password", "SESSION_USER_PASSWORD_BROKEN");
        }

        public static void clearSession(Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.remove("password");
            editor.remove("username");
            editor.remove("projectId");
            editor.remove("sessionId");
            editor.remove("user");
            editor.commit();
        }
    }

}
