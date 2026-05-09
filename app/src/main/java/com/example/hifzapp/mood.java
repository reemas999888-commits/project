package com.example.hifzapp;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class mood extends Application {

    public static final String PREFS_NAME = "hifz_prefs";
    public static final String KEY_LIGHT_MODE = "light_mode";

    @Override
    public void onCreate() {
        super.onCreate();
        applyTheme(this);
    }

    public static void applyTheme(android.content.Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isLight = prefs.getBoolean(KEY_LIGHT_MODE, false);
        AppCompatDelegate.setDefaultNightMode(
                isLight
                        ? AppCompatDelegate.MODE_NIGHT_NO
                        : AppCompatDelegate.MODE_NIGHT_YES
        );
    }
}