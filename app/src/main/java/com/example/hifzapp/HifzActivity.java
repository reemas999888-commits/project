package com.example.hifzapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hifzapp.database.DatabaseHelper;
import com.example.hifzapp.database.Progress;

public class HifzActivity extends BaseActivity {

    private Handler autoHandler = new Handler(Looper.getMainLooper());
    private boolean isAutoPlaying = false;
    private Runnable autoRunnable;

    private DatabaseHelper dbHelper;

    private TextView tvHifzSurahName, tvHifzVerseLabel;
    private TextView tvProgressVerse, tvProgressPercent;
    private ProgressBar progressHifz;
    private TextView tvCurrentVerseNum, tvVerseText;
    private TextView tvRepStar1, tvRepStar2, tvRepStar3, tvRepStar4, tvRepStar5;
    private TextView tvRepProgress;
    private FrameLayout btnPrev, btnPlayPause, btnRepeat, btnNext;
    private TextView tvPlayPauseIcon;
    private TextView tvHifzClose;

    private String surahName = "سورة الفاتحة";
    private int fromVerse = 1;
    private int toVerse = 7;
    private int repeatCount = 5;

    private int currentVerse = 1;
    private int currentRepeat = 1;

    private String[] sampleVerses = {
            "بِسۡمِ ٱللَّهِ ٱلرَّحۡمَـٰنِ ٱلرَّحِیمِ",
            "ٱلۡحَمۡدُ لِلَّهِ رَبِّ ٱلۡعَـٰلَمِینَ",
            "ٱلرَّحۡمَـٰنِ ٱلرَّحِیمِ",
            "مَـٰلِكِ یَوۡمِ ٱلدِّینِ",
            "إِیَّاكَ نَعۡبُدُ وَإِیَّاكَ نَسۡتَعِینُ",
            "ٱهۡدِنَا ٱلصِّرَ ٰطَ ٱلۡمُسۡتَقِیمَ",
            "صِرَ ٰطَ ٱلَّذِینَ أَنۡعَمۡتَ عَلَیۡهِمۡ غَیۡرِ ٱلۡمَغۡضُوبِ عَلَیۡهِمۡ وَلَا ٱلضَّاۤلِّینَ"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hifz);

        dbHelper = new DatabaseHelper(this);

        surahName = getIntent().getStringExtra("surah_name");
        fromVerse = getIntent().getIntExtra("from_verse", 1);
        toVerse = getIntent().getIntExtra("to_verse", 7);
        repeatCount = getIntent().getIntExtra("repeat_count", 5);

        if (surahName == null) {
            surahName = "سورة الفاتحة";
        }

        currentVerse = fromVerse;
        currentRepeat = 1;

        Progress progress = dbHelper.getProgress();

        if (progress != null) {
            currentVerse = progress.currentAyah;
        }

        dbHelper.saveProgress(1, currentVerse);

        tvHifzSurahName = findViewById(R.id.tvHifzSurahName);
        tvHifzVerseLabel = findViewById(R.id.tvHifzVerseLabel);
        tvProgressVerse = findViewById(R.id.tvProgressVerse);
        tvProgressPercent = findViewById(R.id.tvProgressPercent);
        progressHifz = findViewById(R.id.progressHifz);
        tvCurrentVerseNum = findViewById(R.id.tvCurrentVerseNum);
        tvVerseText = findViewById(R.id.tvVerseText);
        tvRepStar1 = findViewById(R.id.tvRepStar1);
        tvRepStar2 = findViewById(R.id.tvRepStar2);
        tvRepStar3 = findViewById(R.id.tvRepStar3);
        tvRepStar4 = findViewById(R.id.tvRepStar4);
        tvRepStar5 = findViewById(R.id.tvRepStar5);
        tvRepProgress = findViewById(R.id.tvRepProgress);
        btnPrev = findViewById(R.id.btnPrev);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnNext = findViewById(R.id.btnNext);
        tvPlayPauseIcon = findViewById(R.id.tvPlayPauseIcon);
        tvHifzClose = findViewById(R.id.tvHifzClose);

        tvHifzSurahName.setText(surahName);
        updateUI();

        tvHifzClose.setOnClickListener(v -> finish());

        btnPlayPause.setOnClickListener(v -> {
            if (isAutoPlaying) {
                stopAutoLoop();
            } else {
                startAutoLoop();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentVerse < toVerse) {
                currentVerse++;
                currentRepeat = 1;
                updateUI();
                dbHelper.saveProgress(1, currentVerse);
            } else {
                openCompletion();
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (currentVerse > fromVerse) {
                currentVerse--;
                currentRepeat = 1;
                updateUI();
                dbHelper.saveProgress(1, currentVerse);
            }
        });

        btnRepeat.setOnClickListener(v -> {
            if (currentRepeat < repeatCount) {
                currentRepeat++;
            } else {
                if (currentVerse < toVerse) {
                    currentVerse++;
                    currentRepeat = 1;
                } else {
                    openCompletion();
                    return;
                }
            }

            updateUI();
            dbHelper.saveProgress(1, currentVerse);
        });
    }

    private void startAutoLoop() {
        isAutoPlaying = true;
        tvPlayPauseIcon.setText("⏸");

        autoRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAutoPlaying) return;

                if (currentRepeat < repeatCount) {
                    currentRepeat++;
                    updateUI();
                    dbHelper.saveProgress(1, currentVerse);
                    autoHandler.postDelayed(this, 2000);

                } else if (currentVerse < toVerse) {
                    currentVerse++;
                    currentRepeat = 1;
                    updateUI();
                    dbHelper.saveProgress(1, currentVerse);
                    autoHandler.postDelayed(this, 2000);

                } else {
                    isAutoPlaying = false;
                    openCompletion();
                }
            }
        };

        autoHandler.postDelayed(autoRunnable, 2000);
    }

    private void stopAutoLoop() {
        isAutoPlaying = false;
        tvPlayPauseIcon.setText("▶");

        if (autoRunnable != null) {
            autoHandler.removeCallbacks(autoRunnable);
        }
    }

    private void updateUI() {
        int totalVerses = toVerse - fromVerse + 1;
        int verseIndex = currentVerse - fromVerse;
        int totalDone = verseIndex * repeatCount + (currentRepeat - 1);
        int totalAll = totalVerses * repeatCount;
        int percent = totalAll > 0 ? (totalDone * 100) / totalAll : 0;

        tvHifzVerseLabel.setText("الآية " + currentVerse + " من " + (fromVerse + totalVerses - 1));
        tvProgressVerse.setText((verseIndex + 1) + " / " + totalVerses + " آيات");
        tvProgressPercent.setText(percent + "% مكتمل");
        progressHifz.setProgress(percent);
        tvCurrentVerseNum.setText(String.valueOf(currentVerse));

        int sampleIdx = (currentVerse - 1) % sampleVerses.length;
        tvVerseText.setText(sampleVerses[sampleIdx]);

        updateRepetitionStars();
        tvRepProgress.setText(currentRepeat + " من " + repeatCount);
    }

    private void updateRepetitionStars() {
        TextView[] stars = {tvRepStar1, tvRepStar2, tvRepStar3, tvRepStar4, tvRepStar5};

        for (int i = 0; i < 5; i++) {
            float threshold = ((float) (i + 1) / 5f) * repeatCount;

            if (currentRepeat >= threshold) {
                stars[i].setText("★");
                stars[i].setTextColor(getResources().getColor(R.color.yellow_main));
            } else {
                stars[i].setText("☆");
                stars[i].setTextColor(0xFF555588);
            }
        }
    }

    private void openCompletion() {
        int totalVerses = toVerse - fromVerse + 1;

        Intent intent = new Intent(HifzActivity.this, CompletionActivity.class);
        intent.putExtra("surah_name", surahName);
        intent.putExtra("verse_count", totalVerses);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAutoLoop();
    }
}