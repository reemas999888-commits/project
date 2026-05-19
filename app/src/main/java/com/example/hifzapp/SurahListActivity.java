package com.example.hifzapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hifzapp.adapter.SurahAdapter;
import com.example.hifzapp.model.Surah;
import com.example.hifzapp.model.SurahResponse;
import com.example.hifzapp.api.ApiService;
import com.example.hifzapp.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurahListActivity extends BaseActivity {

    private RecyclerView rvSurahs;
    private SurahAdapter adapter;
    private EditText etSearch;
    private TextView tvFilterAll, tvFilterJuz, tvFilterShort, tvBack;

    private List<Surah> allSurahs = new ArrayList<>();
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_list);

        rvSurahs = findViewById(R.id.rvSurahs);
        etSearch = findViewById(R.id.etSearch);
        tvFilterAll = findViewById(R.id.tvFilterAll);
        tvFilterJuz = findViewById(R.id.tvFilterJuz);
        tvFilterShort = findViewById(R.id.tvFilterShort);
        tvBack = findViewById(R.id.tvBack);

        rvSurahs.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SurahAdapter(allSurahs, surah -> {
            Intent intent = new Intent(SurahListActivity.this, HifzSetupActivity.class);
            intent.putExtra("surah_name", surah.getName());
            intent.putExtra("surah_number", surah.getNumber());
            intent.putExtra("surah_verse_count", surah.getNumberOfAyahs());
            startActivity(intent);
        });

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadSurahs();
    }

    private void loadSurahs() {

        ApiService apiService =
                RetrofitClient.getClient().create(ApiService.class);

        apiService.getSurahs().enqueue(new Callback<SurahResponse>() {

            @Override
            public void onResponse(Call<SurahResponse> call,
                                   Response<SurahResponse> response) {

                if (response.body() != null) {

                    allSurahs = response.body().getData();

                    adapter.updateList(allSurahs);
                }
            }

            @Override
            public void onFailure(Call<SurahResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setFilter(String filter) {
        currentFilter = filter;
        applyFilters(etSearch.getText().toString());
    }

    private void applyFilters(String query) {

        List<Surah> filtered = new ArrayList<>();

        for (Surah s : allSurahs) {

            boolean matchFilter = true;

            if (currentFilter.equals("juz")) {
                matchFilter = false; // API ما يدعم هذا حالياً
            }

            if (currentFilter.equals("short")) {
                matchFilter = false; // API ما يدعم هذا حالياً
            }

            boolean matchSearch =
                    query.isEmpty() || s.getName().contains(query);

            if (matchFilter && matchSearch) {
                filtered.add(s);
            }
        }

        adapter.updateList(filtered);
    }
}