package com.sonali.portertestapp.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sonali.portertestapp.MyApplication;

/**
 * Created by Sonali
 */

public class PreferenceManager {

    private static final String SHARED_PREFS = "App_Sh_Prefs";
    private static SharedPreferences mSharedPrefs;
    private static Editor mEditor;
    public static String RECENT_PICKUP_POINT_SEARCH = "RECENT_PICKUP_POINT_SEARCH";
    public static String RECENT_DROP_POINT_SEARCH = "RECENT_DROP_POINT_SEARCH";

    public static void init() {
        if (mSharedPrefs == null) {
            mSharedPrefs = MyApplication.getApplicationInstance().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        }

        if (mEditor == null) {
            mEditor = mSharedPrefs.edit();
        }
    }

    public String getString(String key, String defString) {
        return mSharedPrefs.getString(key, defString);
    }

    public void setString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

}
