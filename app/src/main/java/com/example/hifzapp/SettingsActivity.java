package com.example.hifzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences(mood.PREFS_NAME, MODE_PRIVATE);

        Switch switchDarkMode = findViewById(R.id.switchDarkMode);

        // اقرأ الحالة المحفوظة واضبط السويتش
        boolean isLight = prefs.getBoolean(mood.KEY_LIGHT_MODE, false);
        switchDarkMode.setChecked(isLight);

        // لما يغير المستخدم السويتش
        switchDarkMode.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean(mood.KEY_LIGHT_MODE, checked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    checked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
            // أعد تشغيل التطبيق من الأول
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // رابط حول التطبيق
        TextView tvAbout = findViewById(R.id.tvAboutLink);
        tvAbout.setOnClickListener(v ->
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class))
        );

        // زر الرجوع
        TextView tvBack = findViewById(R.id.tvSettingsBack);
        tvBack.setOnClickListener(v -> finish());
    }
}