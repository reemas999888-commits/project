package com.example.hifzapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hifzapp.adapter.SurahAdapter;
import com.example.hifzapp.data.SurahData;
import com.example.hifzapp.model.Surah;

import java.util.ArrayList;
import java.util.List;

public class SurahListActivity extends BaseActivity {//عشان تستفيد من حجم الخط
//شاشة عرض قائمة السور وفيها فلاتر للسور وتسمح بالبحثعن اسم سوره
    //meeeeeeee
    private RecyclerView rvSurahs;//قايمة عرض السور
    private SurahAdapter adapter;//يربط بيانات السور ب ريسايكلير فيو
    private EditText etSearch;
    private TextView tvFilterAll, tvFilterJuz, tvFilterShort, tvBack;

    private List<Surah> allSurahs;
    private String currentFilter = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_list);//يربطها فيها

        rvSurahs      = findViewById(R.id.rvSurahs);
        etSearch      = findViewById(R.id.etSearch);//ربط عناصر الواجهه بالكود
        tvFilterAll   = findViewById(R.id.tvFilterAll);
        tvFilterJuz   = findViewById(R.id.tvFilterJuz);
        tvFilterShort = findViewById(R.id.tvFilterShort);
        tvBack        = findViewById(R.id.tvBack);

        rvSurahs.setLayoutManager(new LinearLayoutManager(this));//عشان تظهر قايمة السور تحت بعض

        // تفعيل السكرول بعجلة الماوس في المحاكي
        rvSurahs.setOnGenericMotionListener((v, event) -> {//لتحسين التمرير بس
            if (event.getAction() == MotionEvent.ACTION_SCROLL) {
                LinearLayoutManager lm = (LinearLayoutManager) rvSurahs.getLayoutManager();
                if (lm != null) {
                    float scrollY = event.getAxisValue(MotionEvent.AXIS_VSCROLL);
                    int delta = (int) (-scrollY * 80);
                    rvSurahs.scrollBy(0, delta);
                }
                return true;
            }
            return false;
        });

        // تحميل البيانات أولاً
        allSurahs = SurahData.getAll();// عشان ياخذ كل السور من surahData

        adapter = new SurahAdapter(allSurahs, surah -> {//انشاء Adabter
            Intent intent = new Intent(SurahListActivity.this, HifzSetupActivity.class);
            intent.putExtra("surah_name",        surah.getName());
            intent.putExtra("surah_number",      surah.getNumber());
            intent.putExtra("surah_verse_count", surah.getNumberOfAyahs());
            startActivity(intent);//لما المستخدم يضغط  بينتقل
        });

        rvSurahs.setAdapter(adapter);//نربط ال Adabter بالسايكل فيو

        tvBack.setOnClickListener(v -> finish());

        TextView tvViewPage = findViewById(R.id.tvViewPage);//زر عرض صفحة المصحف
        tvViewPage.setOnClickListener(v -> {
            Intent intent = new Intent(SurahListActivity.this, pageViewActivity.class);
            intent.putExtra("page_number", 1);//تعرض اول صفحه من المصحف
            startActivity(intent);
        });

        tvFilterAll.setOnClickListener(v   -> setFilter("all"));
        tvFilterJuz.setOnClickListener(v   -> setFilter("juz"));
        tvFilterShort.setOnClickListener(v -> setFilter("short"));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters(s.toString());//التطبيق يفلتر و يعرض السوره المطابقه
            }
        });

        applyFilters("");
        updateFilterButtons();
    }

    private void setFilter(String filter) {
        currentFilter = filter;//تغير الفلتر الحالي
        updateFilterButtons();//تحدث شكل ازرار الفلتر
        applyFilters(etSearch.getText().toString());//تطبيق الفلتر مع البحث الحالي
    }

    private void updateFilterButtons() {// تغير زر الفلتر النشط  اول تكون كلها غير نشطه
        tvFilterAll.setBackgroundResource(R.drawable.bg_filter_inactive);
        tvFilterAll.setTextColor(getResources().getColor(R.color.white));

        tvFilterJuz.setBackgroundResource(R.drawable.bg_filter_inactive);
        tvFilterJuz.setTextColor(getResources().getColor(R.color.white));

        tvFilterShort.setBackgroundResource(R.drawable.bg_filter_inactive);
        tvFilterShort.setTextColor(getResources().getColor(R.color.white));

        if (currentFilter.equals("all")) {
            tvFilterAll.setBackgroundResource(R.drawable.bg_filter_active);
            tvFilterAll.setTextColor(0xFF09092B);
        } else if (currentFilter.equals("juz")) {
            tvFilterJuz.setBackgroundResource(R.drawable.bg_filter_active);
            tvFilterJuz.setTextColor(0xFF09092B);
        } else if (currentFilter.equals("short")) {
            tvFilterShort.setBackgroundResource(R.drawable.bg_filter_active);
            tvFilterShort.setTextColor(0xFF09092B);
        }
    }

    private void applyFilters(String query) {
        List<Surah> filtered = new ArrayList<>();

        for (Surah s : allSurahs) {
            boolean matchFilter = true;

            if (currentFilter.equals("juz")) {
                matchFilter = s.getNumber() >= 78;
            } else if (currentFilter.equals("short")) {
                matchFilter = s.getNumberOfAyahs() < 30;
            }

            boolean matchSearch = query.isEmpty() || s.getName().contains(query);

            if (matchFilter && matchSearch) {
                filtered.add(s);
            }
        }

        adapter.updateList(filtered);
    }
}