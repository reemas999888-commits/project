package com.example.hifzapp;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hifzapp.database.DatabaseHelper;
import com.example.hifzapp.database.Progress;

import java.io.IOException;

public class HifzActivity extends BaseActivity {

    private final Handler autoHandler = new Handler(Looper.getMainLooper());
    private boolean isAutoPlaying = false;
    private Runnable autoRunnable;
    private MediaPlayer mediaPlayer;

    private static final String AUDIO_BASE =
            "https://cdn.islamic.network/quran/audio/128/ar.alafasy/";

    private static final int[] SURAH_OFFSET = {
            0,0,7,293,493,669,789,954,1160,1235,1364,1473,1596,1707,1750,1802,
            1901,2029,2140,2250,2348,2483,2595,2673,2791,2855,2932,3159,3252,
            3340,3409,3469,3503,3533,3606,3660,3705,3788,3970,4058,4133,4218,
            4272,4325,4414,4473,4510,4545,4583,4612,4630,4675,4735,4784,4846,
            4901,4979,5075,5104,5126,5150,5163,5177,5188,5199,5217,5229,5241,
            5271,5323,5375,5419,5447,5475,5495,5551,5591,5622,5672,5712,5758,
            5800,5829,5848,5884,5909,5931,5948,5967,5993,6023,6043,6058,6079,
            6090,6098,6106,6125,6130,6138,6146,6157,6168,6176,6179,6188,6193,
            6197,6204,6207,6213,6216,6221,6225,6230
    };

    private DatabaseHelper dbHelper;

    private TextView tvHifzSurahName, tvHifzVerseLabel;
    private TextView tvProgressVerse, tvProgressPercent;
    private ProgressBar progressHifz;
    private TextView tvCurrentVerseNum, tvVerseText;
    private TextView tvRepStar1, tvRepStar2, tvRepStar3, tvRepStar4, tvRepStar5;
    private TextView tvRepProgress;
    private FrameLayout btnPrev, btnPlayPause, btnRepeat, btnNext;
    private TextView tvPlayPauseIcon;
    private TextView tvHifzClose;

    private String surahName = "سورة الفاتحة";
    private int surahNumber = 1;
    private int fromVerse = 1;
    private int toVerse = 7;
    private int repeatCount = 5;
    private int currentVerse = 1;
    private int currentRepeat = 1;

    private static final String[] FATIHA_VERSES = {
            "بِسۡمِ ٱللَّهِ ٱلرَّحۡمَـٰنِ ٱلرَّحِیمِ",
            "ٱلۡحَمۡدُ لِلَّهِ رَبِّ ٱلۡعَـٰلَمِینَ",
            "ٱلرَّحۡمَـٰنِ ٱلرَّحِیمِ",
            "مَـٰلِكِ یَوۡمِ ٱلدِّینِ",
            "إِیَّاكَ نَعۡبُدُ وَإِیَّاكَ نَسۡتَعِینُ",
            "ٱهۡدِنَا ٱلصِّرَ ٰطَ ٱلۡمُسۡتَقِیمَ",
            "صِرَ ٰطَ ٱلَّذِینَ أَنۡعَمۡتَ عَلَیۡهِمۡ غَیۡرِ ٱلۡمَغۡضُوبِ عَلَیۡهِمۡ وَلَا ٱلضَّاۤلِّینَ"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mood.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hifz);

        dbHelper = new DatabaseHelper(this);

        surahName   = getIntent().getStringExtra("surah_name");
        surahNumber = getIntent().getIntExtra("surah_number", 1);
        fromVerse   = getIntent().getIntExtra("from_verse", 1);
        toVerse     = getIntent().getIntExtra("to_verse", 7);
        repeatCount = getIntent().getIntExtra("repeat_count", 5);

        if (surahName == null) surahName = "سورة الفاتحة";

        currentVerse  = fromVerse;
        currentRepeat = 1;

        Progress progress = dbHelper.getProgress();
        if (progress != null && progress.currentAyah >= fromVerse
                && progress.currentAyah <= toVerse) {
            currentVerse = progress.currentAyah;
        }
        dbHelper.saveProgress(surahNumber, currentVerse);

        tvHifzSurahName   = findViewById(R.id.tvHifzSurahName);
        tvHifzVerseLabel  = findViewById(R.id.tvHifzVerseLabel);
        tvProgressVerse   = findViewById(R.id.tvProgressVerse);
        tvProgressPercent = findViewById(R.id.tvProgressPercent);
        progressHifz      = findViewById(R.id.progressHifz);
        tvCurrentVerseNum = findViewById(R.id.tvCurrentVerseNum);
        tvVerseText       = findViewById(R.id.tvVerseText);
        tvRepStar1        = findViewById(R.id.tvRepStar1);
        tvRepStar2        = findViewById(R.id.tvRepStar2);
        tvRepStar3        = findViewById(R.id.tvRepStar3);
        tvRepStar4        = findViewById(R.id.tvRepStar4);
        tvRepStar5        = findViewById(R.id.tvRepStar5);
        tvRepProgress     = findViewById(R.id.tvRepProgress);
        btnPrev           = findViewById(R.id.btnPrev);
        btnPlayPause      = findViewById(R.id.btnPlayPause);
        btnRepeat         = findViewById(R.id.btnRepeat);
        btnNext           = findViewById(R.id.btnNext);
        tvPlayPauseIcon   = findViewById(R.id.tvPlayPauseIcon);
        tvHifzClose       = findViewById(R.id.tvHifzClose);

        tvHifzSurahName.setText(surahName);
        updateUI();

        tvHifzClose.setOnClickListener(v -> {
            releaseMediaPlayer();
            finish();
        });

        btnPlayPause.setOnClickListener(v -> {
            if (isAutoPlaying) stopAutoPlay();
            else startAutoPlay();
        });

        btnNext.setOnClickListener(v -> {
            stopAutoPlay();
            if (currentVerse < toVerse) {
                currentVerse++;
                currentRepeat = 1;
                updateUI();
                dbHelper.saveProgress(surahNumber, currentVerse);
            } else {
                openCompletion();
            }
        });

        btnPrev.setOnClickListener(v -> {
            stopAutoPlay();
            if (currentVerse > fromVerse) {
                currentVerse--;
                currentRepeat = 1;
                updateUI();
                dbHelper.saveProgress(surahNumber, currentVerse);
            }
        });

        btnRepeat.setOnClickListener(v -> {
            stopAutoPlay();
            playAudio(false);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAutoPlay();
        releaseMediaPlayer();
    }

    private void startAutoPlay() {
        isAutoPlaying = true;
        tvPlayPauseIcon.setText("⏸");
        playAudio(true);
    }

    private void stopAutoPlay() {
        isAutoPlaying = false;
        tvPlayPauseIcon.setText("▶");
        if (autoRunnable != null) {
            autoHandler.removeCallbacks(autoRunnable);
            autoRunnable = null;
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        showAudioIndicator(false);
    }

    private void scheduleNext() {
        if (!isAutoPlaying) return;
        autoRunnable = () -> {
            if (!isAutoPlaying) return;
            if (currentRepeat < repeatCount) {
                currentRepeat++;
            } else if (currentVerse < toVerse) {
                currentVerse++;
                currentRepeat = 1;
            } else {
                isAutoPlaying = false;
                openCompletion();
                return;
            }
            updateUI();
            dbHelper.saveProgress(surahNumber, currentVerse);
            playAudio(true);
        };
        autoHandler.postDelayed(autoRunnable, 500);
    }

    private void playAudio(boolean autoAdvance) {
        int globalAyah = (surahNumber >= 1 && surahNumber < SURAH_OFFSET.length)
                ? SURAH_OFFSET[surahNumber] + currentVerse
                : currentVerse;

        String url = AUDIO_BASE + globalAyah + ".mp3";

        com.example.hifzapp.database.Ayah dbAyah =
                dbHelper.getAyah(surahNumber, currentVerse);
        if (dbAyah != null && dbAyah.audioUrl != null && !dbAyah.audioUrl.isEmpty()) {
            url = dbAyah.audioUrl;
        }

        releaseMediaPlayer();
        showAudioIndicator(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mp -> {
                showAudioIndicator(false);
                if (autoAdvance) scheduleNext();
            });
            mediaPlayer.setOnErrorListener((mp, w, e) -> {
                showAudioIndicator(false);
                if (autoAdvance) autoHandler.postDelayed(this::scheduleNext, 1500);
                return true;
            });
        } catch (IOException e) {
            showAudioIndicator(false);
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception ignored) {}
            mediaPlayer = null;
        }
    }

    private void showAudioIndicator(boolean show) {
        View v = findViewById(R.id.llAudioIndicator);
        if (v != null) v.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void openCompletion() {
        releaseMediaPlayer();
        Intent intent = new Intent(HifzActivity.this, CompletionActivity.class);
        intent.putExtra("surah_name", surahName);
        intent.putExtra("verse_count", toVerse - fromVerse + 1);
        startActivity(intent);
        finish();
    }

    private void updateUI() {
        int totalVerses = toVerse - fromVerse + 1;
        int verseIndex  = currentVerse - fromVerse;
        int totalDone   = verseIndex * repeatCount + (currentRepeat - 1);
        int percent     = totalVerses * repeatCount > 0
                ? (totalDone * 100) / (totalVerses * repeatCount) : 0;

        tvHifzVerseLabel.setText("الآية " + currentVerse + " من " + toVerse);
        tvProgressVerse.setText((verseIndex + 1) + " / " + totalVerses + " آيات");
        tvProgressPercent.setText(percent + "% مكتمل");
        progressHifz.setProgress(percent);
        tvCurrentVerseNum.setText(String.valueOf(currentVerse));

        com.example.hifzapp.database.Ayah dbAyah =
                dbHelper.getAyah(surahNumber, currentVerse);
        if (dbAyah != null && dbAyah.ayahText != null && !dbAyah.ayahText.isEmpty()) {
            tvVerseText.setText(dbAyah.ayahText);
        } else {
            tvVerseText.setText(FATIHA_VERSES[(currentVerse - 1) % FATIHA_VERSES.length]);
        }

        updateRepetitionStars();
        tvRepProgress.setText(currentRepeat + " من " + repeatCount);
    }

    private void updateRepetitionStars() {
        TextView[] stars = {tvRepStar1, tvRepStar2, tvRepStar3, tvRepStar4, tvRepStar5};
        for (int i = 0; i < 5; i++) {
            float threshold = ((float)(i + 1) / 5f) * repeatCount;
            if (currentRepeat >= threshold) {
                stars[i].setText("★");
                stars[i].setTextColor(getResources().getColor(R.color.yellow_main));
            } else {
                stars[i].setText("☆");
                stars[i].setTextColor(0xFF555588);
            }
        }
    }
}