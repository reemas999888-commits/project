package com.example.hifzapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hifzapp.adapter.SurahAdapter;
import com.example.hifzapp.data.SurahData;
import com.example.hifzapp.model.Surah;

import java.util.ArrayList;
import java.util.List;

public class SurahListActivity extends BaseActivity  {

    private RecyclerView rvSurahs;
    private SurahAdapter adapter;
    private EditText etSearch;
    private TextView tvFilterAll, tvFilterJuz, tvFilterShort, tvBack;

    private List<Surah> allSurahs;
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_list);

        allSurahs = SurahData.getAllSurahs();

        rvSurahs = findViewById(R.id.rvSurahs);
        etSearch = findViewById(R.id.etSearch);
        tvFilterAll = findViewById(R.id.tvFilterAll);
        tvFilterJuz = findViewById(R.id.tvFilterJuz);
        tvFilterShort = findViewById(R.id.tvFilterShort);
        tvBack = findViewById(R.id.tvBack);

        adapter = new SurahAdapter(allSurahs, new SurahAdapter.OnSurahClickListener() {
            @Override
            public void onSurahClick(Surah surah) {
                Intent intent = new Intent(SurahListActivity.this, HifzSetupActivity.class);
                intent.putExtra("surah_name", surah.getName());
                intent.putExtra("surah_number", surah.getNumber());
                intent.putExtra("surah_verse_count", surah.getVerseCount());
                startActivity(intent);
            }
        });

        rvSurahs.setLayoutManager(new LinearLayoutManager(this));
        rvSurahs.setAdapter(adapter);

        tvBack.setOnClickListener(v -> finish());
        TextView tvViewPage = findViewById(R.id.tvViewPage);
        tvViewPage.setOnClickListener(v -> {
            Intent intent = new Intent(SurahListActivity.this, pageViewActivity.class);
            intent.putExtra("page_number", 1);
            startActivity(intent);
        });
        tvFilterAll.setOnClickListener(v -> setFilter("all"));
        tvFilterJuz.setOnClickListener(v -> setFilter("juz"));
        tvFilterShort.setOnClickListener(v -> setFilter("short"));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {
            }

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                applyFilters(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setFilter(String filter) {
        currentFilter = filter;

        tvFilterAll.setBackgroundResource(R.drawable.bg_filter_inactive);
        tvFilterAll.setTextColor(getResources().getColor(R.color.white));

        tvFilterJuz.setBackgroundResource(R.drawable.bg_filter_inactive);
        tvFilterJuz.setTextColor(getResources().getColor(R.color.white));

        tvFilterShort.setBackgroundResource(R.drawable.bg_filter_inactive);
        tvFilterShort.setTextColor(getResources().getColor(R.color.white));

        TextView active = filter.equals("all")
                ? tvFilterAll
                : (filter.equals("juz") ? tvFilterJuz : tvFilterShort);

        active.setBackgroundResource(R.drawable.bg_filter_active);
        active.setTextColor(getResources().getColor(R.color.bg_dark));

        applyFilters(etSearch.getText().toString());
    }

    private void applyFilters(String query) {
        List<Surah> filtered = new ArrayList<>();

        for (Surah s : allSurahs) {
            boolean matchFilter = true;

            if (currentFilter.equals("juz")) {
                matchFilter = s.isJuzAmma();
            }

            if (currentFilter.equals("short")) {
                matchFilter = s.isShort();
            }

            boolean matchSearch = query.isEmpty() || s.getName().contains(query);

            if (matchFilter && matchSearch) {
                filtered.add(s);
            }
        }

        adapter.updateList(filtered);
    }
}