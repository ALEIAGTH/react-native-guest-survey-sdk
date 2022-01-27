package com.reactnativeguestsurveysdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import fr.it4pme.locatme.config.Config;
import fr.it4pme.locatme.internal.AppLog;

public class SaveSharedPreference {

    private static final String TAG = "SaveSharedPreference : ";
    private static final String PREF_APP_KEY= "app_Key";
    private static final String PREF_APP_NAME= "app_Name";
    private static final String PREF_USER_ID= "email";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    public static void setAppKey(Context ctx, String key)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_APP_KEY, key);
        editor.apply();
    }

    public static String getAppKey(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_APP_KEY, "");
    }


    public static void setAppName(Context ctx, String name)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_APP_NAME, name);
        editor.apply();
    }

    public static String getAppName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_APP_NAME, "");
    }

    public static void setUserID(Context ctx, String userID)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userID);
        editor.apply();
    }

    public static String getUserID(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }




    public static void clearData(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        //editor.clear(); //clear all stored data
        editor.remove(PREF_APP_KEY);
        editor.remove(PREF_APP_NAME);
        editor.remove(PREF_APP_KEY);
        editor.commit();
        System.out.println("************** *********** clear in stop collection;");
    }

}
