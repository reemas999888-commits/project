package com.example.hifzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // نفس اسم الـ prefs في mood
        SharedPreferences prefs = getSharedPreferences(mood.PREFS_NAME, MODE_PRIVATE);

        // السويتش - الوضع الليلي
        Switch switchDarkMode = findViewById(R.id.switchDarkMode);
        boolean isLight = prefs.getBoolean(mood.KEY_LIGHT_MODE, false);
        switchDarkMode.setChecked(isLight);
        switchDarkMode.setOnCheckedChangeListener((btn, checked) -> {
            prefs.edit().putBoolean(mood.KEY_LIGHT_MODE, checked).apply();
            // أعد تشغيل التطبيق من الأول
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // حجم الخط
        SeekBar seekFontSize = findViewById(R.id.seekFontSize);
        seekFontSize.setProgress(prefs.getInt("font_size", 2));
        seekFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prefs.edit().putInt("font_size", progress).apply();
                recreate();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // حول التطبيق
        TextView tvAbout = findViewById(R.id.tvAboutLink);
        tvAbout.setOnClickListener(v ->
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class))
        );

        // زر الرجوع
        TextView tvBack = findViewById(R.id.tvSettingsBack);
        tvBack.setOnClickListener(v -> finish());
    }
}