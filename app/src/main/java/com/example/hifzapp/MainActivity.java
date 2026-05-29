package com.example.hifzapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.recyclerview.widget.RecyclerView;
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
            String surahName = getSurahName(progress.currentSurah);

            tvLastHifz.setText(
                    "آخر حفظ لك: " + surahName + " - الآية " + progress.currentAyah
            );

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

    private String getSurahName(int surahNumber) {
        String[] surahNames = {
                "سورة الفاتحة",
                "سورة البقرة",
                "سورة آل عمران",
                "سورة النساء",
                "سورة المائدة",
                "سورة الأنعام",
                "سورة الأعراف",
                "سورة الأنفال",
                "سورة التوبة",
                "سورة يونس",
                "سورة هود",
                "سورة يوسف",
                "سورة الرعد",
                "سورة إبراهيم",
                "سورة الحجر",
                "سورة النحل",
                "سورة الإسراء",
                "سورة الكهف",
                "سورة مريم",
                "سورة طه",
                "سورة الأنبياء",
                "سورة الحج",
                "سورة المؤمنون",
                "سورة النور",
                "سورة الفرقان",
                "سورة الشعراء",
                "سورة النمل",
                "سورة القصص",
                "سورة العنكبوت",
                "سورة الروم",
                "سورة لقمان",
                "سورة السجدة",
                "سورة الأحزاب",
                "سورة سبأ",
                "سورة فاطر",
                "سورة يس",
                "سورة الصافات",
                "سورة ص",
                "سورة الزمر",
                "سورة غافر",
                "سورة فصلت",
                "سورة الشورى",
                "سورة الزخرف",
                "سورة الدخان",
                "سورة الجاثية",
                "سورة الأحقاف",
                "سورة محمد",
                "سورة الفتح",
                "سورة الحجرات",
                "سورة ق",
                "سورة الذاريات",
                "سورة الطور",
                "سورة النجم",
                "سورة القمر",
                "سورة الرحمن",
                "سورة الواقعة",
                "سورة الحديد",
                "سورة المجادلة",
                "سورة الحشر",
                "سورة الممتحنة",
                "سورة الصف",
                "سورة الجمعة",
                "سورة المنافقون",
                "سورة التغابن",
                "سورة الطلاق",
                "سورة التحريم",
                "سورة الملك",
                "سورة القلم",
                "سورة الحاقة",
                "سورة المعارج",
                "سورة نوح",
                "سورة الجن",
                "سورة المزمل",
                "سورة المدثر",
                "سورة القيامة",
                "سورة الإنسان",
                "سورة المرسلات",
                "سورة النبأ",
                "سورة النازعات",
                "سورة عبس",
                "سورة التكوير",
                "سورة الانفطار",
                "سورة المطففين",
                "سورة الانشقاق",
                "سورة البروج",
                "سورة الطارق",
                "سورة الأعلى",
                "سورة الغاشية",
                "سورة الفجر",
                "سورة البلد",
                "سورة الشمس",
                "سورة الليل",
                "سورة الضحى",
                "سورة الشرح",
                "سورة التين",
                "سورة العلق",
                "سورة القدر",
                "سورة البينة",
                "سورة الزلزلة",
                "سورة العاديات",
                "سورة القارعة",
                "سورة التكاثر",
                "سورة العصر",
                "سورة الهمزة",
                "سورة الفيل",
                "سورة قريش",
                "سورة الماعون",
                "سورة الكوثر",
                "سورة الكافرون",
                "سورة النصر",
                "سورة المسد",
                "سورة الإخلاص",
                "سورة الفلق",
                "سورة الناس"
        };

        if (surahNumber >= 1 && surahNumber <= surahNames.length) {
            return surahNames[surahNumber - 1];
        }

        return "سورة الفاتحة";

    }
}
