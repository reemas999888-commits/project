package com.example.hifzapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HifzSetupActivity extends AppCompatActivity {

    private TextView tvSetupSurahName, tvInfoSurahName, tvInfoVerseCount;
    private TextView tvFromVerse, tvToVerse, tvRepeatCount, tvWillMemorize;
    private FrameLayout btnFromPlus, btnFromMinus, btnToPlus, btnToMinus;
    private FrameLayout btnRepeatPlus, btnRepeatMinus;
    private TextView btnQuick3, btnQuick5, btnQuick7, btnQuick10;
    private Button btnStartHifzNow;
    private TextView tvSetupBack;

    private String surahName = "سورة الفاتحة";
    private int totalVerses = 7;
    private int fromVerse = 1;
    private int toVerse = 7;
    private int repeatCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hifz_setup);

        // Get intent data
        surahName   = getIntent().getStringExtra("surah_name");
        totalVerses = getIntent().getIntExtra("surah_verse_count", 7);
        if (surahName == null) surahName = "سورة الفاتحة";
        toVerse = totalVerses;

        // Bind views
        tvSetupSurahName  = findViewById(R.id.tvSetupSurahName);
        tvInfoSurahName   = findViewById(R.id.tvInfoSurahName);
        tvInfoVerseCount  = findViewById(R.id.tvInfoVerseCount);
        tvFromVerse       = findViewById(R.id.tvFromVerse);
        tvToVerse         = findViewById(R.id.tvToVerse);
        tvRepeatCount     = findViewById(R.id.tvRepeatCount);
        tvWillMemorize    = findViewById(R.id.tvWillMemorize);
        btnFromPlus       = findViewById(R.id.btnFromPlus);
        btnFromMinus      = findViewById(R.id.btnFromMinus);
        btnToPlus         = findViewById(R.id.btnToPlus);
        btnToMinus        = findViewById(R.id.btnToMinus);
        btnRepeatPlus     = findViewById(R.id.btnRepeatPlus);
        btnRepeatMinus    = findViewById(R.id.btnRepeatMinus);
        btnQuick3         = findViewById(R.id.btnQuick3);
        btnQuick5         = findViewById(R.id.btnQuick5);
        btnQuick7         = findViewById(R.id.btnQuick7);
        btnQuick10        = findViewById(R.id.btnQuick10);
        btnStartHifzNow   = findViewById(R.id.btnStartHifzNow);
        tvSetupBack       = findViewById(R.id.tvSetupBack);

        // Set initial values
        tvSetupSurahName.setText(surahName);
        tvInfoSurahName.setText(surahName);
        tvInfoVerseCount.setText(totalVerses + " آية");
        updateUI();

        // Back
        tvSetupBack.setOnClickListener(v -> finish());

        // From verse controls
        btnFromPlus.setOnClickListener(v -> {
            if (fromVerse < toVerse) { fromVerse++; updateUI(); }
        });
        btnFromMinus.setOnClickListener(v -> {
            if (fromVerse > 1) { fromVerse--; updateUI(); }
        });

        // To verse controls
        btnToPlus.setOnClickListener(v -> {
            if (toVerse < totalVerses) { toVerse++; updateUI(); }
        });
        btnToMinus.setOnClickListener(v -> {
            if (toVerse > fromVerse) { toVerse--; updateUI(); }
        });

        // Repeat count controls
        btnRepeatPlus.setOnClickListener(v -> {
            if (repeatCount < 20) { repeatCount++; updateRepeatUI(); }
        });
        btnRepeatMinus.setOnClickListener(v -> {
            if (repeatCount > 1) { repeatCount--; updateRepeatUI(); }
        });

        // Quick repeat buttons
        btnQuick3.setOnClickListener(v  -> { repeatCount = 3;  updateRepeatUI(); });
        btnQuick5.setOnClickListener(v  -> { repeatCount = 5;  updateRepeatUI(); });
        btnQuick7.setOnClickListener(v  -> { repeatCount = 7;  updateRepeatUI(); });
        btnQuick10.setOnClickListener(v -> { repeatCount = 10; updateRepeatUI(); });

        // Start
        btnStartHifzNow.setOnClickListener(v -> {
            Intent intent = new Intent(HifzSetupActivity.this, HifzActivity.class);
            intent.putExtra("surah_name",   surahName);
            intent.putExtra("from_verse",   fromVerse);
            intent.putExtra("to_verse",     toVerse);
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
        // Update quick button highlight
        resetQuickButtons();
        if (repeatCount == 3)  highlightQuickBtn(btnQuick3);
        else if (repeatCount == 5)  highlightQuickBtn(btnQuick5);
        else if (repeatCount == 7)  highlightQuickBtn(btnQuick7);
        else if (repeatCount == 10) highlightQuickBtn(btnQuick10);
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
}
