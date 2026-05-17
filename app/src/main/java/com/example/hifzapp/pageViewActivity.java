package com.example.hifzapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

public class pageViewActivity extends BaseActivity {

    private ImageView imgPage;
    private TextView tvPageTitle, tvPageBack;
    private Button btnNextPage, btnPrevPage;

    private int currentPage = 1;
    private static final int TOTAL_PAGES = 604;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_view);

        currentPage = getIntent().getIntExtra("page_number", 1);

        imgPage     = findViewById(R.id.imgPage);
        tvPageTitle = findViewById(R.id.tvPageTitle);
        tvPageBack  = findViewById(R.id.tvPageBack);
        btnNextPage = findViewById(R.id.btnNextPage);
        btnPrevPage = findViewById(R.id.btnPrevPage);

        loadPage();

        tvPageBack.setOnClickListener(v -> finish());

        btnNextPage.setOnClickListener(v -> {
            if (currentPage < TOTAL_PAGES) {
                currentPage++;
                loadPage();
            } else {
                Toast.makeText(this, "آخر صفحة", Toast.LENGTH_SHORT).show();
            }
        });

        btnPrevPage.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadPage();
            } else {
                Toast.makeText(this, "أول صفحة", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPage() {
        tvPageTitle.setText("صفحة " + currentPage);

        String imageUrl = "https://cdn.islamic.network/quran/images/high-resolution/page"
                + String.format("%03d", currentPage) + ".png";

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(imgPage);
    }
}
