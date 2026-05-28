package com.example.hifzapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.hifzapp.database.DatabaseHelper;
import com.example.hifzapp.database.Progress;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {

    private BottomNavigationView bottomNav;
    private Button btnStartHifz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Progress progress = dbHelper.getProgress();

        TextView tvLastHifz = findViewById(R.id.tvLastHifz);

        if (progress != null) {
            tvLastHifz.setText("آخر حفظ: سورة " + progress.currentSurah + " - آية " + progress.currentAyah);
            tvLastHifz.setVisibility(android.view.View.VISIBLE);
        }

        btnStartHifz = findViewById(R.id.btnStartHifz);
        bottomNav = findViewById(R.id.bottomNav);

        btnStartHifz.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SurahListActivity.class);
            startActivity(intent);
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_hifz) {
                return true;

            } else if (id == R.id.nav_achievements) {
                Intent intent = new Intent(MainActivity.this, ChallengeActivity.class);
                startActivity(intent);
                return true;

            } else if (id == R.id.nav_settings) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}