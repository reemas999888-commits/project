package com.example.hifzapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import com.example.hifzapp.database.DatabaseHelper;
import com.example.hifzapp.database.Progress;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {

    private BottomNavigationView bottomNav;
    private Button btnStartHifz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// عرض آخر حفظ
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Progress progress = dbHelper.getProgress();
        TextView tvLastHifz = findViewById(R.id.tvLastHifz);
        if (progress != null) {
            tvLastHifz.setText("آخر حفظ: سورة " + progress.currentSurah + " - آية " + progress.currentAyah);
            tvLastHifz.setVisibility(android.view.View.VISIBLE);
        }
        btnStartHifz = findViewById(R.id.btnStartHifz);
        bottomNav    = findViewById(R.id.bottomNav);

        // زر ابدأ الحفظ
        btnStartHifz.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SurahListActivity.class);
            startActivity(intent);
        });

        // BottomNavigationView
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_hifz) {
                // نحن هنا أصلاً
                return true;

            } else if (id == R.id.nav_achievements) {
                // Dialog يشرح التطبيق (مؤقتاً لحين إنشاء شاشة الإنجازات)
                new AlertDialog.Builder(this)
                        .setTitle("⭐ إنجازاتي")
                        .setMessage("ستظهر هنا إنجازاتك ونجومك قريباً!")
                        .setPositiveButton("حسناً", null)
                        .show();
                return true;

            } else if (id == R.id.nav_settings) {
                // فتح شاشة الإعدادات
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
