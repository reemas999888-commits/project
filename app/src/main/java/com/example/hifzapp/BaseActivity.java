package com.example.hifzapp;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public static final String KEY_FONT_SIZE = "font_size";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        applyFontSize();
        super.onCreate(savedInstanceState);
    }

    protected void applyFontSize() {
        SharedPreferences prefs = getSharedPreferences(mood.PREFS_NAME, MODE_PRIVATE);

        int sizeIndex = prefs.getInt(KEY_FONT_SIZE, 2);

        float[] scales = {
                0.8f,
                0.9f,
                1.0f,
                1.2f,
                1.4f
        };

        if (sizeIndex < 0 || sizeIndex >= scales.length) {
            sizeIndex = 2;
        }

        Configuration config = getResources().getConfiguration();
        config.fontScale = scales[sizeIndex];

        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics()
        );
    }
}