package com.example.hifzapp;

import android.app.Activity;
import android.content.SharedPreferences;

public class mood {
    public static final String PREFS_NAME = "hifz_prefs";
    public static final String KEY_LIGHT_MODE = "light_mode";

    public static void applyTheme(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        boolean isLight = prefs.getBoolean(KEY_LIGHT_MODE, false);
        if (isLight) {
            activity.setTheme(R.style.Theme_Light);
        } else {
            activity.setTheme(R.style.Theme_Dark);
        }
    }
}