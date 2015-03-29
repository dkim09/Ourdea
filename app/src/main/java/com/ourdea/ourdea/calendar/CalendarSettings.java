package com.ourdea.ourdea.calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class CalendarSettings {

    final static public String CALENDAR_KEY = "ENABLE_CALENDAR_SYNC";
    final static public String PROMPT_USER_KEY = "PROMPT_USER";

    public static void enableCalendar(Context context) {
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CALENDAR_KEY, true);
        editor.commit();
    }

    public static boolean isCalendarEnabled(Context context) {
        return context.getApplicationContext().getSharedPreferences("store", 0).getBoolean(CALENDAR_KEY, false);
    }

    public static void disableCalendar(Context context) {
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CALENDAR_KEY, false);
        editor.commit();
    }

    public static boolean askedUserToEnableCalendarBefore(Context context) {
        return context.getApplicationContext().getSharedPreferences("store", 0).getBoolean(PROMPT_USER_KEY, false);
    }

    public static void doNotAskUserToEnableCalendarAgain(Context context) {
        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("store", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PROMPT_USER_KEY, true);
        editor.commit();
    }

}
