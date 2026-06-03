package com.example.hifzapp;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hifzapp.database.DatabaseHelper;

import java.util.ArrayList;

public class HifzHistoryActivity extends BaseActivity {
// شاشة سجل الحفظ تعرض الالجلسات اللي تم حفظها سابقا باستخدام البيانات المحفوظه في SQLite
    private LinearLayout llSavedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hifz_history);

        TextView tvBack = findViewById(R.id.tvSavedBack);
        llSavedList = findViewById(R.id.llSavedList);

        tvBack.setOnClickListener(v -> finish());

        loadSavedHifz();
    }

    private void loadSavedHifz() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<String> savedList = dbHelper.getAllSavedHifz();

        llSavedList.removeAllViews();

        if (savedList.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("لا توجد انجازات بعد 🌙\nابدأ وستظر انجازاتك هنا عند اتمام الحفظ");
            emptyText.setTextColor(getResources().getColor(R.color.white));
            emptyText.setTextSize(18);
            emptyText.setGravity(Gravity.CENTER);
            emptyText.setPadding(24, 60, 24, 24);

            llSavedList.addView(emptyText);
            return;
        }

        for (String item : savedList) {
            TextView card = new TextView(this);
            card.setText(item);
            card.setTextColor(getResources().getColor(R.color.white));
            card.setTextSize(17);
            card.setGravity(Gravity.RIGHT);
            card.setLineSpacing(8, 1);
            card.setPadding(28, 24, 28, 24);
            card.setBackgroundResource(R.drawable.bg_rounded_card);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0, 0, 0, 18);
            card.setLayoutParams(params);

            llSavedList.addView(card);
        }
    }
}