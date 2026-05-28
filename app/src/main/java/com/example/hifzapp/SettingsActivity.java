package com.example.hifzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity {

    private Switch switchDarkMode;
    private SeekBar seekFontSize;
    private TextView tvAboutLink;
    private TextView tvSettingsBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences(mood.PREFS_NAME, MODE_PRIVATE);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        seekFontSize = findViewById(R.id.seekFontSize);
        tvAboutLink = findViewById(R.id.tvAboutLink);
        tvSettingsBack = findViewById(R.id.tvSettingsBack);

        /*
         * السويتش في الواجهة مكتوب عليه: الوضع العادي
         *
         * إذا السويتش شغال = الوضع العادي / الأبيض
         * إذا السويتش طافي = الوضع الداكن
         *
         * الافتراضي: طافي، يعني التطبيق داكن.
         */
        switchDarkMode.setChecked(mood.isLightMode(this));

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mood.setLightMode(this, isChecked);
            recreate();
        });

        int savedFontSize = prefs.getInt(BaseActivity.KEY_FONT_SIZE, 2);
        seekFontSize.setProgress(savedFontSize);

        seekFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    prefs.edit()
                            .putInt(BaseActivity.KEY_FONT_SIZE, progress)
                            .apply();

                    recreate();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        tvAboutLink.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        tvSettingsBack.setOnClickListener(v -> finish());
    }
}