package com.ourdea.ourdea.resources;

import android.content.Context;
import android.content.SharedPreferences;

public class ApiUtilities {

    //final static public String SERVER_ADDRESS = "http://52.10.50.18:9000"; // AWS server
    final static public String SERVER_ADDRESS = "http://192.168.1.73:9000"; // Personal server

    public static class Session {

        final static public String PASSWORD_KEY = "password";
        final static public String EMAIL_KEY = "email";
        final static public String NAME_KEY = "name";
        final static public String SESSION_ID_KEY = "sessionId";
        final static public String PROJECT_ID_KEY = "projectId";

        final static public String PASSWORD_MISSING = "SESSION_USER_PASSWORD_ERROR";
        final static public String EMAIL_MISSING = "SESSION_USER_EMAIL_ERROR";
        final static public String NAME_MISSING = "SESSION_USER_NAME_ERROR";
        final static public String SESSION_ID_MISSING = "SESSION_ID_ERROR";
        final static public int PROJECT_ID_MISSING = -1;

        public static void storeSession(String sessionId, Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SESSION_ID_KEY, sessionId);
            editor.commit();
        }

        public static String getSession(Context context) {
            return context.getApplicationContext().getSharedPreferences("store", 0).getString(SESSION_ID_KEY, SESSION_ID_MISSING);
        }

        public static void storeName(String name, Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(NAME_KEY, name);
            editor.commit();
        }

        public static String getName(Context context) {
            return context.getApplicationContext().getSharedPreferences("store", 0).getString(NAME_KEY, NAME_MISSING);
        }

        public static void storeEmail(String email, Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(EMAIL_KEY, email);
            editor.commit();
        }

        public static String getEmail(Context context) {
            return context.getApplicationContext().getSharedPreferences("store", 0).getString(EMAIL_KEY, EMAIL_MISSING);
        }

        public static void storeProjectId(Long projectId, Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(PROJECT_ID_KEY, projectId);
            editor.commit();
        }

        public static Long getProjectId(Context context) {
            return context.getApplicationContext().getSharedPreferences("store", 0).getLong(PROJECT_ID_KEY, PROJECT_ID_MISSING);
        }

        public static void storePassword(String password, Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(PASSWORD_KEY, password);
            editor.commit();
        }

        public static String getPassword(Context context) {
            return context.getApplicationContext().getSharedPreferences("store", 0).getString(PASSWORD_KEY, PASSWORD_MISSING);
        }

        public static void clearSession(Context context) {
            SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.remove(PASSWORD_KEY);
            editor.remove(NAME_KEY);
            editor.remove(PROJECT_ID_KEY);
            editor.remove(SESSION_ID_KEY);
            editor.remove(EMAIL_KEY);
            editor.commit();
        }
    }

}
