package com.example.hifzapp;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
// مسؤوله عن عرض الايه +تشغيل الصوت +التكرار +الانتقال بين اليات +حفظ التقدم
//meeeee
import com.example.hifzapp.database.DatabaseHelper;
import com.example.hifzapp.database.Progress;

import java.io.IOException;

public class HifzActivity extends BaseActivity {

    private final Handler autoHandler = new Handler(Looper.getMainLooper());// امر الانتقال للايه الثانيه
    private boolean isAutoPlaying = false;// اذا التشغيل التلقايي شغال او متوقف
    private Runnable autoRunnable;// النتتقال للتكرار او الايه التاليه
    private MediaPlayer mediaPlayer;

    private static final String AUDIO_BASE =// رابط صوتت القارئ العفاسي
            "https://cdn.islamic.network/quran/audio/128/ar.alafasy/";

    private static final int[] SURAH_OFFSET = {// تحول رقم الايه لرقم عام
            0, 0, 7, 293, 493, 669, 789, 954, 1160, 1235, 1364, 1473,
            1596, 1707, 1750, 1802, 1901, 2029, 2140, 2250, 2348, 2483,
            2595, 2673, 2791, 2855, 2932, 3159, 3252, 3340, 3409, 3469,
            3503, 3533, 3606, 3660, 3705, 3788, 3970, 4058, 4133, 4218,
            4272, 4325, 4414, 4473, 4510, 4545, 4583, 4612, 4630, 4675,
            4735, 4784, 4846, 4901, 4979, 5075, 5104, 5126, 5150, 5163,
            5177, 5188, 5199, 5217, 5229, 5241, 5271, 5323, 5375, 5419,
            5447, 5475, 5495, 5551, 5591, 5622, 5672, 5712, 5758, 5800,
            5829, 5848, 5884, 5909, 5931, 5948, 5967, 5993, 6023, 6043,
            6058, 6079, 6090, 6098, 6106, 6125, 6130, 6138, 6146, 6157,
            6168, 6176, 6179, 6188, 6193, 6197, 6204, 6207, 6213, 6216,
            6221, 6225, 6230
    };

    private DatabaseHelper dbHelper;

    private TextView tvHifzSurahName;
    private TextView tvHifzVerseLabel;
    private TextView tvProgressVerse;
    private TextView tvProgressPercent;
    private ProgressBar progressHifz;
    private TextView tvCurrentVerseNum;
    private TextView tvVerseText;
    private TextView tvRepStar1;
    private TextView tvRepStar2;
    private TextView tvRepStar3;
    private TextView tvRepStar4;
    private TextView tvRepStar5;
    private TextView tvRepProgress;
    private FrameLayout btnPrev;
    private FrameLayout btnPlayPause;
    private FrameLayout btnRepeat;
    private FrameLayout btnNext;
    private TextView tvPlayPauseIcon;
    private TextView tvHifzClose;

    private String surahName = "سورة الفاتحة";
    private int surahNumber = 1;
    private int fromVerse = 1;
    private int toVerse = 7;
    private int repeatCount = 5;
    private int currentVerse = 1;
    private int currentRepeat = 1;
    private ImageView imgPageHifz;

    private static final String[] FATIHA_VERSES = { // لو فشل جلب النص تحطها
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
        imgPageHifz = findViewById(R.id.imgPageHifz);// صورة صفحة المصحف
        dbHelper = new DatabaseHelper(this);// يجهز قاعدة البيانات

        surahName = getIntent().getStringExtra("surah_name");// يستقبل البيانات اللي اختارها المستخدم من سيت اب
        surahNumber = getIntent().getIntExtra("surah_number", 1);
        fromVerse = getIntent().getIntExtra("from_verse", 1);
        toVerse = getIntent().getIntExtra("to_verse", 7);
        repeatCount = getIntent().getIntExtra("repeat_count", 5);

        if (surahName == null) {
            surahName = "سورة الفاتحة";
        }

        currentVerse = fromVerse;
        currentRepeat = 1;// بداية التكرار

        Progress progress = dbHelper.getProgress();// اذا في تقدم حفظ من قبل تقراها من SQLite

        if (progress != null &&
                progress.currentAyah >= fromVerse &&
                progress.currentAyah <= toVerse) {
            currentVerse = progress.currentAyah;
        }

        dbHelper.saveProgress(surahNumber, currentVerse);

        tvHifzSurahName = findViewById(R.id.tvHifzSurahName);
        tvHifzVerseLabel = findViewById(R.id.tvHifzVerseLabel);
        tvProgressVerse = findViewById(R.id.tvProgressVerse);
        tvProgressPercent = findViewById(R.id.tvProgressPercent);
        progressHifz = findViewById(R.id.progressHifz);
        tvCurrentVerseNum = findViewById(R.id.tvCurrentVerseNum);
        tvVerseText = findViewById(R.id.tvVerseText);// نص الايه
        tvRepStar1 = findViewById(R.id.tvRepStar1);
        tvRepStar2 = findViewById(R.id.tvRepStar2);
        tvRepStar3 = findViewById(R.id.tvRepStar3);
        tvRepStar4 = findViewById(R.id.tvRepStar4);
        tvRepStar5 = findViewById(R.id.tvRepStar5);
        tvRepProgress = findViewById(R.id.tvRepProgress);
        btnPrev = findViewById(R.id.btnPrev);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnNext = findViewById(R.id.btnNext);
        tvPlayPauseIcon = findViewById(R.id.tvPlayPauseIcon);
        tvHifzClose = findViewById(R.id.tvHifzClose);

        tvHifzSurahName.setText(surahName);
        updateUI();

        tvHifzClose.setOnClickListener(v -> {// زر الاغلاق يوقف الصوت
            releaseMediaPlayer();
            finish();
        });

        btnPlayPause.setOnClickListener(v -> {// زر التشغيل و الايقاف
            if (isAutoPlaying) {
                stopAutoPlay();
            } else {
                startAutoPlay();
            }
        });

        btnNext.setOnClickListener(v -> {//  زر التالي
            stopAutoPlay();

            if (currentVerse < toVerse) {
                currentVerse++;// اذا م وصل اخر ايه يزيد ايه و يرجع التكرار 1
                currentRepeat = 1;
                updateUI();
                dbHelper.saveProgress(surahNumber, currentVerse);
            } else {
                openCompletion();// اذا وصل اخر ايه يجفظ التقدم
            }
        });

        btnPrev.setOnClickListener(v -> {//  زر الايه السابقه بشرط م يرجع قبل ايه البدايه
            stopAutoPlay();

            if (currentVerse > fromVerse) {
                currentVerse--;
                currentRepeat = 1;
                updateUI();
                dbHelper.saveProgress(surahNumber, currentVerse);
            }
        });

        btnRepeat.setOnClickListener(v -> {// زر اعادة الايه
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

    private void startAutoPlay() {// تشغل الصوت مع انتقال تلقائي
        isAutoPlaying = true;
        tvPlayPauseIcon.setText("⏸");
        playAudio(true);
    }

    private void stopAutoPlay() {
        isAutoPlaying = false;
        tvPlayPauseIcon.setText("▶");

        if (autoRunnable != null) {// تلغي اي تشغيل مؤجل عشان م ينتقل بالغلط
            autoHandler.removeCallbacks(autoRunnable);
            autoRunnable = null;
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

        showAudioIndicator(false);//  تخفي مؤشر الصوت
    }

    private void scheduleNext() {// بعد م يوقف الصوت ينتظر نص ثانيه قبل م يقرر
        if (!isAutoPlaying) {
            return;
        }

        autoRunnable = () -> {
            if (!isAutoPlaying) {
                return;
            }

            if (currentRepeat < repeatCount) {
                currentRepeat++;//  اذا م تكررت بالعدد المطلوب يعيد
            } else if (currentVerse < toVerse) {//  يروح الايه اللي بعد
                currentVerse++;
                currentRepeat = 1;
            } else {
                isAutoPlaying = false;
                openCompletion();// اذا انتهى من اخر ايه يفتح شاشة الاكمال
                return;
            }

            updateUI();
            dbHelper.saveProgress(surahNumber, currentVerse);
            playAudio(true);
        };

        autoHandler.postDelayed(autoRunnable, 500);
    }

    private void playAudio(boolean autoAdvance) { // تشغل صوت الايه حسب الرقم العام
        int globalAyah;

        if (surahNumber >= 1 && surahNumber < SURAH_OFFSET.length) {
            globalAyah = SURAH_OFFSET[surahNumber] + currentVerse;
        } else {
            globalAyah = currentVerse;
        }

        String url = AUDIO_BASE + globalAyah + ".mp3";// بناء رابط الصوت

        com.example.hifzapp.database.Ayah dbAyah =
                dbHelper.getAyah(surahNumber, currentVerse);// لو في قاعدة بيانات فيها رابط الصوت يستخدمه هو

        if (dbAyah != null && dbAyah.getAudioUrl() != null && !dbAyah.getAudioUrl().isEmpty()) {
            url = dbAyah.getAudioUrl();
        }

        releaseMediaPlayer();// تشغيل الصوت
        showAudioIndicator(true);//  يظهر مؤشر الصوت

        mediaPlayer = new MediaPlayer();// ينشئ ميديا بلاير

        mediaPlayer.setAudioAttributes(//  يعطيه رابط الصوت
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(MediaPlayer::start);

            mediaPlayer.setOnCompletionListener(mp -> {// اذا انتهى الصوت
                showAudioIndicator(false);//  لو ترو يستدعي SCHEDUALENEXT عشان يكرر او ينتقل

                if (autoAdvance) {
                    scheduleNext();
                }
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                showAudioIndicator(false);// لو صار خطا بالصوت يخفي حق الصوت

                if (autoAdvance) {//  لو التشغيل تلقائي  ينتظر ثانيه ونص و يحاول يكمل الخطوه الثانيه
                    autoHandler.postDelayed(this::scheduleNext, 1500);
                }

                return true;
            });

        } catch (IOException e) {
            showAudioIndicator(false);
        }
    }

    private void releaseMediaPlayer() { //  تمنع اكثر من صوت يشتغلون سوا او يصير استهلاك للذاكره
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                mediaPlayer.release();

            } catch (Exception ignored) {
            }

            mediaPlayer = null;
        }
    }

    private void showAudioIndicator(boolean show) {//  تظهر او تخفي مؤشر الصوت
        View v = findViewById(R.id.llAudioIndicator);

        if (v != null) {
            v.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void openCompletion() { // تشتغل لما المستخدم يحلص اخر ايه
        releaseMediaPlayer();

        int completedVerses = toVerse - fromVerse + 1; //  تحسب عدد التكرارات  والايات
        int completedRepetitions = completedVerses * repeatCount;
        dbHelper.saveCompletedHifz( // تضيف التقدم
                surahNumber,
                surahName,
                fromVerse,
                toVerse,
                repeatCount,
                completedVerses
        );
        dbHelper.addTodayChallengeProgress(
                surahNumber,
                completedVerses,
                completedRepetitions
        );
  // تفتح شاشة الكمال
        Intent intent = new Intent(HifzActivity.this, CompletionActivity.class);
        intent.putExtra("surah_name", surahName);
        intent.putExtra("verse_count", completedVerses);
        startActivity(intent);
        finish();
    }

    private void updateUI() {// تحدث كل شي ظاهر بالشاشه
        int totalVerses = toVerse - fromVerse + 1; // عدد الايات المختاره
        int verseIndex = currentVerse - fromVerse;
        int totalDone = verseIndex * repeatCount + (currentRepeat - 1);// كم تكرار تم انجازه

        int percent;// نسبة الانجاز

        if (totalVerses * repeatCount > 0) {
            percent = (totalDone * 100) / (totalVerses * repeatCount);
        } else {
            percent = 0;
        }

        tvHifzVerseLabel.setText("الآية " + currentVerse + " من " + toVerse);
        tvProgressVerse.setText((verseIndex + 1) + " / " + totalVerses + " آيات");
        tvProgressPercent.setText(percent + "% مكتمل");
        progressHifz.setProgress(percent);
        tvCurrentVerseNum.setText(String.valueOf(currentVerse));

        com.example.hifzapp.database.Ayah dbAyah =
                dbHelper.getAyah(surahNumber, currentVerse);
// جلب النص من sqlITE
        if (dbAyah != null && dbAyah.getText() != null && !dbAyah.getText().isEmpty()) {
            tvVerseText.setText(dbAyah.getText());
            //    تحميل صورة الصفحة باستخدام GLIDE
            int pageNum = (dbAyah != null && dbAyah.pageNumber > 0) ? dbAyah.pageNumber : 1;
            String pageImageUrl = "https://cdn.islamic.network/quran/images/high-resolution/page"
                    + String.format("%03d", pageNum) + ".png";
            imgPageHifz.setVisibility(android.view.View.VISIBLE);
            Glide.with(this)
                    .load(pageImageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(imgPageHifz);
        } else {
            //    جلب النص من AlQuran Cloud API اذا م لقيها ب SQL
            int absoluteAyah = SURAH_OFFSET[surahNumber] + currentVerse;
            String apiUrl = "https://api.alquran.cloud/v1/ayah/" + absoluteAyah + "/quran-uthmani";

            new Thread(() -> {
                try {
                    java.net.URL url = new java.net.URL(apiUrl);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    java.io.BufferedReader reader = new java.io.BufferedReader(
                            new java.io.InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    org.json.JSONObject json = new org.json.JSONObject(sb.toString());
                    String text = json.getJSONObject("data").getString("text");

                    runOnUiThread(() -> tvVerseText.setText(text));
                } catch (Exception e) {
                    runOnUiThread(() -> tvVerseText.setText(
                            FATIHA_VERSES[(currentVerse - 1) % FATIHA_VERSES.length]));
                }
            }).start();
        }

        updateRepetitionStars();
        tvRepProgress.setText(currentRepeat + " من " + repeatCount);
    }

    private void updateRepetitionStars() { // تعرض شكل التقدم على شكل نجوم
        TextView[] stars = {
                tvRepStar1,
                tvRepStar2,
                tvRepStar3,
                tvRepStar4,
                tvRepStar5
        };

        for (int i = 0; i < 5; i++) {
            float threshold = ((float) (i + 1) / 5f) * repeatCount;

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