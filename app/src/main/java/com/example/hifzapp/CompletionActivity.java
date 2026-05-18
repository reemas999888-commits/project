package com.example.hifzapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CompletionActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completion);

        String surahName  = getIntent().getStringExtra("surah_name");
        int    verseCount = getIntent().getIntExtra("verse_count", 0);
        if (surahName == null) surahName = "السورة";

        TextView tvMsg  = findViewById(R.id.tvCompletionMsg);
        Button   btnDone= findViewById(R.id.btnDone);
        Button   btnRep = findViewById(R.id.btnRepeatHifz);

        tvMsg.setText("لقد أتممت حفظ " + verseCount + " آيات من " + surahName);

        btnDone.setOnClickListener(v -> {
            Intent intent = new Intent(CompletionActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        btnRep.setOnClickListener(v -> {
            finish(); // يرجع لشاشة الإعداد
        });
    }
}
