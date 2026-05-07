package com.example.hifzapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // داخل onCreate بعد tvBack
        TextView tvAbout = findViewById(R.id.tvAboutLink);
        tvAbout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
            startActivity(intent);
        });
        // زر الرجوع
        TextView tvBack = findViewById(R.id.tvSettingsBack);
        tvBack.setOnClickListener(v -> finish());
    }
}