package com.example.hifzapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.hifzapp.database.DatabaseHelper;
import com.example.hifzapp.database.Progress;

public class HifzSetupActivity extends BaseActivity {

    private TextView tvSetupSurahName, tvInfoSurahName, tvInfoVerseCount;
    private TextView tvFromVerse, tvToVerse, tvRepeatCount, tvWillMemorize;
    private FrameLayout btnFromPlus, btnFromMinus, btnToPlus, btnToMinus;
    private FrameLayout btnOpenSurahPages;
    private FrameLayout btnRepeatPlus, btnRepeatMinus;
    private TextView btnQuick3, btnQuick5, btnQuick7, btnQuick10;
    private Button btnStartHifzNow;
    private TextView tvSetupBack;

    private String surahName = "سورة الفاتحة";
    private int surahNumber = 1;
    private int totalVerses = 7;
    private int fromVerse = 1;
    private int toVerse = 7;
    private int repeatCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hifz_setup);

        surahName   = getIntent().getStringExtra("surah_name");
        surahNumber = getIntent().getIntExtra("surah_number", 1);
        totalVerses = getIntent().getIntExtra("surah_verse_count", 7);

        if (surahName == null) {
            surahName = "سورة الفاتحة";
        }

        toVerse = totalVerses;

        tvSetupSurahName = findViewById(R.id.tvSetupSurahName);
        tvInfoSurahName = findViewById(R.id.tvInfoSurahName);
        tvInfoVerseCount = findViewById(R.id.tvInfoVerseCount);
        tvFromVerse = findViewById(R.id.tvFromVerse);
        tvToVerse = findViewById(R.id.tvToVerse);
        tvRepeatCount = findViewById(R.id.tvRepeatCount);
        tvWillMemorize = findViewById(R.id.tvWillMemorize);

        btnFromPlus = findViewById(R.id.btnFromPlus);
        btnFromMinus = findViewById(R.id.btnFromMinus);
        btnToPlus = findViewById(R.id.btnToPlus);
        btnToMinus = findViewById(R.id.btnToMinus);
        btnRepeatPlus = findViewById(R.id.btnRepeatPlus);
        btnRepeatMinus = findViewById(R.id.btnRepeatMinus);
        btnOpenSurahPages = findViewById(R.id.btnOpenSurahPages);
        btnQuick3 = findViewById(R.id.btnQuick3);
        btnQuick5 = findViewById(R.id.btnQuick5);
        btnQuick7 = findViewById(R.id.btnQuick7);
        btnQuick10 = findViewById(R.id.btnQuick10);

        btnStartHifzNow = findViewById(R.id.btnStartHifzNow);
        tvSetupBack = findViewById(R.id.tvSetupBack);

        tvSetupSurahName.setText(surahName);
        tvInfoSurahName.setText(surahName);
        tvInfoVerseCount.setText(totalVerses + " آية");

        updateUI();
        updateRepeatUI();

        tvSetupBack.setOnClickListener(v -> finish());
        btnOpenSurahPages.setOnClickListener(v -> {
            int startPage = getSurahStartPage(surahNumber);
            int endPage = getSurahEndPage(surahNumber);

            Intent intent = new Intent(HifzSetupActivity.this, pageViewActivity.class);
            intent.putExtra("page_number", startPage);
            intent.putExtra("start_page", startPage);
            intent.putExtra("end_page", endPage);
            intent.putExtra("surah_name", surahName);

            startActivity(intent);
        });
        btnFromPlus.setOnClickListener(v -> {
            if (fromVerse < toVerse) {
                fromVerse++;
                updateUI();
            }
        });

        btnFromMinus.setOnClickListener(v -> {
            if (fromVerse > 1) {
                fromVerse--;
                updateUI();
            }
        });

        btnToPlus.setOnClickListener(v -> {
            if (toVerse < totalVerses) {
                toVerse++;
                updateUI();
            }
        });

        btnToMinus.setOnClickListener(v -> {
            if (toVerse > fromVerse) {
                toVerse--;
                updateUI();
            }
        });

        btnRepeatPlus.setOnClickListener(v -> {
            if (repeatCount < 20) {
                repeatCount++;
                updateRepeatUI();
            }
        });

        btnRepeatMinus.setOnClickListener(v -> {
            if (repeatCount > 1) {
                repeatCount--;
                updateRepeatUI();
            }
        });

        btnQuick3.setOnClickListener(v -> {
            repeatCount = 3;
            updateRepeatUI();
        });

        btnQuick5.setOnClickListener(v -> {
            repeatCount = 5;
            updateRepeatUI();
        });

        btnQuick7.setOnClickListener(v -> {
            repeatCount = 7;
            updateRepeatUI();
        });

        btnQuick10.setOnClickListener(v -> {
            repeatCount = 10;
            updateRepeatUI();
        });

        btnStartHifzNow.setOnClickListener(v -> {

            Intent intent = new Intent(HifzSetupActivity.this, HifzActivity.class);

            intent.putExtra("surah_name", surahName);
            intent.putExtra("surah_number", surahNumber);
            intent.putExtra("from_verse", fromVerse);
            intent.putExtra("to_verse", toVerse);
            intent.putExtra("repeat_count", repeatCount);

            startActivity(intent);
        });
    }

    private void updateUI() {
        tvFromVerse.setText(String.valueOf(fromVerse));
        tvToVerse.setText(String.valueOf(toVerse));

        int count = toVerse - fromVerse + 1;
        tvWillMemorize.setText("⭐  ستحفظ " + count + " آيات");
    }

    private void updateRepeatUI() {
        tvRepeatCount.setText(String.valueOf(repeatCount));

        resetQuickButtons();

        if (repeatCount == 3) {
            highlightQuickBtn(btnQuick3);
        } else if (repeatCount == 5) {
            highlightQuickBtn(btnQuick5);
        } else if (repeatCount == 7) {
            highlightQuickBtn(btnQuick7);
        } else if (repeatCount == 10) {
            highlightQuickBtn(btnQuick10);
        }
    }

    private void resetQuickButtons() {
        btnQuick3.setBackgroundResource(R.drawable.bg_quick_btn_inactive);
        btnQuick3.setTextColor(getResources().getColor(R.color.white));

        btnQuick5.setBackgroundResource(R.drawable.bg_quick_btn_inactive);
        btnQuick5.setTextColor(getResources().getColor(R.color.white));

        btnQuick7.setBackgroundResource(R.drawable.bg_quick_btn_inactive);
        btnQuick7.setTextColor(getResources().getColor(R.color.white));

        btnQuick10.setBackgroundResource(R.drawable.bg_quick_btn_inactive);
        btnQuick10.setTextColor(getResources().getColor(R.color.white));
    }

    private void highlightQuickBtn(TextView btn) {
        btn.setBackgroundResource(R.drawable.bg_quick_btn_active);
        btn.setTextColor(getResources().getColor(R.color.bg_dark));
    }
    private int getSurahStartPage(int surahNumber) {
        int[] surahStartPages = {
                1, 2, 50, 77, 106, 128, 151, 177, 187, 208,
                221, 235, 249, 255, 262, 267, 282, 293, 305, 312,
                322, 332, 342, 350, 359, 367, 377, 385, 396, 404,
                411, 415, 418, 428, 434, 440, 446, 453, 458, 467,
                477, 483, 489, 496, 499, 502, 507, 511, 515, 518,
                520, 523, 526, 528, 531, 534, 537, 542, 545, 549,
                551, 553, 554, 556, 558, 560, 562, 564, 566, 568,
                570, 572, 574, 575, 577, 578, 580, 582, 583, 585,
                586, 587, 587, 589, 590, 591, 591, 592, 593, 594,
                595, 595, 596, 596, 597, 597, 598, 598, 599, 599,
                600, 600, 601, 601, 601, 602, 602, 602, 603, 603,
                603, 604, 604, 604
        };

        if (surahNumber < 1 || surahNumber > 114) {
            return 1;
        }

        return surahStartPages[surahNumber - 1];
    }

    private int getSurahEndPage(int surahNumber) {
        if (surahNumber < 1 || surahNumber > 114) {
            return 604;
        }

        if (surahNumber == 114) {
            return 604;
        }

        return getSurahStartPage(surahNumber + 1);
    }
}