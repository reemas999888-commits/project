package com.example.hifzapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hifzapp.database.Challenge;
import com.example.hifzapp.database.DatabaseHelper;

public class ChallengeActivity extends BaseActivity {
// شاشة التحدي اليومي يحددهدف يومي مثل عدد ايات ويتم حفظ ومتابعة تقدمه
    private DatabaseHelper dbHelper;

    private TextView tvChallengeBack;
    private Spinner spinnerSurah;
    private TextView tvGoalNumber;
    private TextView tvGoalType;
    private TextView tvChallengeStatus;
    private TextView tvGoalAyat;
    private TextView tvGoalRepetitions;

    private FrameLayout btnGoalPlus;
    private FrameLayout btnGoalMinus;

    private Button btnSaveChallenge;

    private String goalType = "ayahs";
    private int goalTarget = 5;

    private int selectedSurahNumber = 1;
    private String selectedSurahName = "سورة الفاتحة";

    private final String[] surahNames = {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        dbHelper = new DatabaseHelper(this);

        tvChallengeBack = findViewById(R.id.tvChallengeBack);
        spinnerSurah = findViewById(R.id.spinnerSurah);
        tvGoalNumber = findViewById(R.id.tvGoalNumber);
        tvGoalType = findViewById(R.id.tvGoalType);
        tvChallengeStatus = findViewById(R.id.tvChallengeStatus);
        tvGoalAyat = findViewById(R.id.tvGoalAyat);
        tvGoalRepetitions = findViewById(R.id.tvGoalRepetitions);

        btnGoalPlus = findViewById(R.id.btnGoalPlus);
        btnGoalMinus = findViewById(R.id.btnGoalMinus);

        btnSaveChallenge = findViewById(R.id.btnSaveChallenge);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                surahNames
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSurah.setAdapter(adapter);

        Challenge todayChallenge = dbHelper.getTodayChallenge();

        if (todayChallenge != null) {
            selectedSurahNumber = todayChallenge.surahNumber;
            selectedSurahName = todayChallenge.surahName;
            goalType = todayChallenge.goalType;
            goalTarget = todayChallenge.goalTarget;

            if (selectedSurahNumber >= 1 && selectedSurahNumber <= surahNames.length) {
                spinnerSurah.setSelection(selectedSurahNumber - 1);
            }
        }

        updateGoalUI();
        updateChallengeStatus();

        tvChallengeBack.setOnClickListener(v -> finish());

        tvGoalAyat.setOnClickListener(v -> {
            goalType = "ayahs";
            updateGoalUI();
        });

        tvGoalRepetitions.setOnClickListener(v -> {
            goalType = "repetitions";
            updateGoalUI();
        });

        btnGoalPlus.setOnClickListener(v -> {
            if (goalTarget < 100) {
                goalTarget++;
                updateGoalUI();
            }
        });

        btnGoalMinus.setOnClickListener(v -> {
            if (goalTarget > 1) {
                goalTarget--;
                updateGoalUI();
            }
        });

        btnSaveChallenge.setOnClickListener(v -> {
            selectedSurahNumber = spinnerSurah.getSelectedItemPosition() + 1;
            selectedSurahName = surahNames[selectedSurahNumber - 1];

            dbHelper.saveTodayChallenge(
                    selectedSurahNumber,
                    selectedSurahName,
                    goalType,
                    goalTarget
            );

            Toast.makeText(this, "تم حفظ تحدي اليوم", Toast.LENGTH_SHORT).show();
            updateChallengeStatus();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateChallengeStatus();
    }

    private void updateGoalUI() {
        tvGoalNumber.setText(String.valueOf(goalTarget));

        if (goalType.equals("repetitions")) {
            tvGoalType.setText("تكرارات");

            tvGoalRepetitions.setBackgroundResource(R.drawable.bg_quick_btn_active);
            tvGoalAyat.setBackgroundResource(R.drawable.bg_quick_btn_inactive);

            tvGoalRepetitions.setTextColor(getResources().getColor(R.color.bg_dark));
            tvGoalAyat.setTextColor(getResources().getColor(R.color.white));

        } else {
            tvGoalType.setText("آيات");

            tvGoalAyat.setBackgroundResource(R.drawable.bg_quick_btn_active);
            tvGoalRepetitions.setBackgroundResource(R.drawable.bg_quick_btn_inactive);

            tvGoalAyat.setTextColor(getResources().getColor(R.color.bg_dark));
            tvGoalRepetitions.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void updateChallengeStatus() {
        Challenge challenge = dbHelper.getTodayChallenge();

        if (challenge == null) {
            tvChallengeStatus.setText("لا يوجد تحدي محفوظ لليوم");
            return;
        }

        String typeText;

        if (challenge.goalType.equals("repetitions")) {
            typeText = "تكرارات";
        } else {
            typeText = "آيات";
        }

        if (challenge.isCompleted()) {
            tvChallengeStatus.setText(
                    "✅ تم إنجاز تحدي اليوم\n" +
                            "السورة: " + challenge.surahName + "\n" +
                            "الهدف: " + challenge.goalTarget + " " + typeText + "\n" +
                            "المنجز: " + challenge.progressCount + " " + typeText
            );
        } else {
            tvChallengeStatus.setText(
                    "⏳ لم يتم إنجاز التحدي بعد\n" +
                            "السورة: " + challenge.surahName + "\n" +
                            "الهدف: " + challenge.goalTarget + " " + typeText + "\n" +
                            "المنجز: " + challenge.progressCount + " " + typeText
            );
        }
    }
}