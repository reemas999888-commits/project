package com.example.hifzapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class mood {
//كلاس مساعد للتحكم بالثيم
    public static final String PREFS_NAME = "hifz_prefs";
    public static final String KEY_LIGHT_MODE = "light_mode";

    /*
     * الوضع العادي / الأبيض
     * true = الوضع العادي شغال
     * false = الوضع الداكن شغال
     *
     * الافتراضي false يعني التطبيق يبدأ داكن
     */
    public static boolean isLightMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_LIGHT_MODE, false);
    }

    public static void setLightMode(Context context, boolean isLight) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        prefs.edit()
                .putBoolean(KEY_LIGHT_MODE, isLight)
                .apply();

        if (isLight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public static void applyTheme(Activity activity) {
        if (isLightMode(activity)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    /*
     * هذي نخليها عشان لو فيه كلاسات ثانية قديمة تستخدم isDarkMode أو setDarkMode
     * ما تطلع أخطاء.
     */
    public static boolean isDarkMode(Context context) {
        return !isLightMode(context);
    }

    public static void setDarkMode(Context context, boolean isDark) {
        setLightMode(context, !isDark);
    }
}