package com.example.hifzapp;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyFontSize(); // ← أولاً حجم الخط
        mood.applyTheme(this); // ← ثم الثيم
        super.onCreate(savedInstanceState);
    }

    private void applyFontSize() {
        SharedPreferences prefs = getSharedPreferences(mood.PREFS_NAME, MODE_PRIVATE);
        int sizeIndex = prefs.getInt("font_size", 2);
        float[] scales = {0.8f, 0.9f, 1.0f, 1.2f, 1.4f};
        float scale = scales[Math.min(sizeIndex, scales.length - 1)];

        Configuration config = getResources().getConfiguration();
        config.fontScale = scale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}