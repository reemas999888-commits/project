package com.example.hifzapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import android.graphics.drawable.Drawable;
import java.io.InputStream;
public class pageViewActivity extends BaseActivity {
//شاشة عرض صفحة المصحف من ملفات assest
    //meeeeee
    private ImageView imgPage;
    private TextView tvPageTitle, tvPageBack;
    private Button btnNextPage, btnPrevPage;

    private int currentPage = 1;
    private int startPage = 1;
    private int endPage = 604;
    private String surahName = "";
    private static final int TOTAL_PAGES = 604;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_view);

        currentPage = getIntent().getIntExtra("page_number", 1);
        startPage = getIntent().getIntExtra("start_page", 1);
        endPage = getIntent().getIntExtra("end_page", TOTAL_PAGES);
        surahName = getIntent().getStringExtra("surah_name");

        if (surahName == null) {
            surahName = "";
        }

        imgPage     = findViewById(R.id.imgPage);
        tvPageTitle = findViewById(R.id.tvPageTitle);
        tvPageBack  = findViewById(R.id.tvPageBack);
        btnNextPage = findViewById(R.id.btnNextPage);
        btnPrevPage = findViewById(R.id.btnPrevPage);

        loadPage();//  اول م يفتح يستدعيها عشان تعرض الصفحه الحاليه

        tvPageBack.setOnClickListener(v -> finish());

        btnNextPage.setOnClickListener(v -> {
            if (currentPage < endPage)  {
                currentPage++;
                loadPage();
            } else {
                Toast.makeText(this, "هذه آخر صفحة في " + surahName, Toast.LENGTH_SHORT).show();
            }
        });

        btnPrevPage.setOnClickListener(v -> {
            if (currentPage > startPage) {
                currentPage--;
                loadPage();
            } else {
                Toast.makeText(this, "هذه أول صفحة في " + surahName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPage() {//  تفتح صورة الصفحه و تحطها بامجفيو و تتحكم بالتالي و السابق

        if (!surahName.isEmpty()) {//  لو اسم السوره موجود
            tvPageTitle.setText(surahName + " - صفحة " + currentPage);
        } else {
            tvPageTitle.setText("صفحة " + currentPage);
        }

        String fileName = "quran-pages-main/quran_pages/" + currentPage + ".png";

        try {
            InputStream is = getAssets().open(fileName); //  يفتح ملف الصوره
            Drawable drawable = Drawable.createFromStream(is, null);//  يحولها ل DROWABLE
            imgPage.setImageDrawable(drawable);
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "تعذر تحميل الصفحة " + currentPage, Toast.LENGTH_SHORT).show();
            imgPage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        btnPrevPage.setEnabled(currentPage > startPage); // باول صفحه زر السابق يتعطل والعكس
        btnNextPage.setEnabled(currentPage < endPage);
    }}