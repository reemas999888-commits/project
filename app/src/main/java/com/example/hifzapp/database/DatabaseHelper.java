package com.example.hifzapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "hifz.db";
    private static final int DB_VERSION = 4;

    private static final String TABLE_AYAHS =
            "CREATE TABLE ayahs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "surah_number INTEGER," +
                    "ayah_number INTEGER," +
                    "page_number INTEGER," +
                    "ayah_text TEXT," +
                    "audio_url TEXT)";

    private static final String TABLE_PROGRESS =
            "CREATE TABLE progress (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "current_surah INTEGER," +
                    "current_ayah INTEGER," +
                    "last_used TEXT)";

    private static final String TABLE_DAILY_CHALLENGE =
            "CREATE TABLE daily_challenge (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "challenge_date TEXT UNIQUE," +
                    "surah_number INTEGER," +
                    "surah_name TEXT," +
                    "goal_type TEXT," +
                    "goal_target INTEGER," +
                    "progress_count INTEGER)";
    private static final String TABLE_SAVED_HIFZ =
            "CREATE TABLE saved_hifz (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "surah_number INTEGER," +
                    "surah_name TEXT," +
                    "from_verse INTEGER," +
                    "to_verse INTEGER," +
                    "repeat_count INTEGER," +
                    "verse_count INTEGER," +
                    "saved_date TEXT)";
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_AYAHS);
        db.execSQL(TABLE_PROGRESS);
        db.execSQL(TABLE_DAILY_CHALLENGE);
        db.execSQL(TABLE_SAVED_HIFZ);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(TABLE_DAILY_CHALLENGE);
        }

        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS daily_challenge");
            db.execSQL(TABLE_DAILY_CHALLENGE);
        }

        if (oldVersion < 4) {
            db.execSQL(TABLE_SAVED_HIFZ);
        }
    }

    public void insertOrUpdateAyah(int surahNum, int ayahNum, int pageNum,
                                   String text, String audioUrl) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("surah_number", surahNum);
        cv.put("ayah_number", ayahNum);
        cv.put("page_number", pageNum);
        cv.put("ayah_text", text);
        cv.put("audio_url", audioUrl);

        int rows = db.update(
                "ayahs",
                cv,
                "surah_number=? AND ayah_number=?",
                new String[]{String.valueOf(surahNum), String.valueOf(ayahNum)}
        );

        if (rows == 0) {
            db.insert("ayahs", null, cv);
        }
    }

    public Ayah getAyah(int surahNum, int ayahNum) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(
                "ayahs",
                null,
                "surah_number=? AND ayah_number=?",
                new String[]{String.valueOf(surahNum), String.valueOf(ayahNum)},
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            Ayah ayah = new Ayah(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getInt(c.getColumnIndexOrThrow("surah_number")),
                    c.getInt(c.getColumnIndexOrThrow("ayah_number")),
                    c.getInt(c.getColumnIndexOrThrow("page_number")),
                    c.getString(c.getColumnIndexOrThrow("ayah_text")),
                    null,
                    c.getString(c.getColumnIndexOrThrow("audio_url"))
            );

            c.close();
            return ayah;
        }

        c.close();
        return null;
    }

    public void saveProgress(int surahNum, int ayahNum) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("current_surah", surahNum);
        cv.put("current_ayah", ayahNum);
        cv.put("last_used", new java.util.Date().toString());

        Cursor c = db.rawQuery("SELECT id FROM progress LIMIT 1", null);

        if (c.moveToFirst()) {
            db.update("progress", cv, null, null);
        } else {
            db.insert("progress", null, cv);
        }

        c.close();
    }

    public Progress getProgress() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM progress LIMIT 1", null);

        if (c.moveToFirst()) {
            Progress p = new Progress(
                    c.getInt(c.getColumnIndexOrThrow("current_surah")),
                    c.getInt(c.getColumnIndexOrThrow("current_ayah")),
                    c.getString(c.getColumnIndexOrThrow("last_used"))
            );

            c.close();
            return p;
        }

        c.close();
        return null;
    }

    private String getTodayDate() {
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
        return sdf.format(new java.util.Date());
    }

    public void saveTodayChallenge(int surahNumber,
                                   String surahName,
                                   String goalType,
                                   int goalTarget) {

        SQLiteDatabase db = getWritableDatabase();
        String today = getTodayDate();

        ContentValues cv = new ContentValues();
        cv.put("challenge_date", today);
        cv.put("surah_number", surahNumber);
        cv.put("surah_name", surahName);
        cv.put("goal_type", goalType);
        cv.put("goal_target", goalTarget);

        Cursor c = db.rawQuery(
                "SELECT id FROM daily_challenge WHERE challenge_date=? LIMIT 1",
                new String[]{today}
        );

        if (c.moveToFirst()) {
            cv.put("progress_count", 0);

            db.update(
                    "daily_challenge",
                    cv,
                    "challenge_date=?",
                    new String[]{today}
            );
        } else {
            cv.put("progress_count", 0);
            db.insert("daily_challenge", null, cv);
        }

        c.close();
    }

    public Challenge getTodayChallenge() {
        SQLiteDatabase db = getReadableDatabase();
        String today = getTodayDate();

        Cursor c = db.rawQuery(
                "SELECT * FROM daily_challenge WHERE challenge_date=? LIMIT 1",
                new String[]{today}
        );

        if (c.moveToFirst()) {
            Challenge challenge = new Challenge(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("challenge_date")),
                    c.getInt(c.getColumnIndexOrThrow("surah_number")),
                    c.getString(c.getColumnIndexOrThrow("surah_name")),
                    c.getString(c.getColumnIndexOrThrow("goal_type")),
                    c.getInt(c.getColumnIndexOrThrow("goal_target")),
                    c.getInt(c.getColumnIndexOrThrow("progress_count"))
            );

            c.close();
            return challenge;
        }

        c.close();
        return null;
    }

    public void addTodayChallengeProgress(int completedSurahNumber,
                                          int versesCount,
                                          int repetitionsCount) {

        Challenge challenge = getTodayChallenge();

        if (challenge == null) {
            return;
        }

        if (challenge.surahNumber != completedSurahNumber) {
            return;
        }

        int addCount;

        if (challenge.goalType.equals("repetitions")) {
            addCount = repetitionsCount;
        } else {
            addCount = versesCount;
        }

        int newProgress = challenge.progressCount + addCount;

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("progress_count", newProgress);

        db.update(
                "daily_challenge",
                cv,
                "id=?",
                new String[]{String.valueOf(challenge.id)}
        );
    }
    public void saveCompletedHifz(int surahNumber,
                                  String surahName,
                                  int fromVerse,
                                  int toVerse,
                                  int repeatCount,
                                  int verseCount) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("surah_number", surahNumber);
        cv.put("surah_name", surahName);
        cv.put("from_verse", fromVerse);
        cv.put("to_verse", toVerse);
        cv.put("repeat_count", repeatCount);
        cv.put("verse_count", verseCount);
        cv.put("saved_date", new java.util.Date().toString());

        db.insert("saved_hifz", null, cv);
    }

    public ArrayList<String> getAllSavedHifz() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM saved_hifz ORDER BY id DESC",
                null
        );

        while (c.moveToNext()) {
            String surahName = c.getString(c.getColumnIndexOrThrow("surah_name"));
            int fromVerse = c.getInt(c.getColumnIndexOrThrow("from_verse"));
            int toVerse = c.getInt(c.getColumnIndexOrThrow("to_verse"));
            int repeatCount = c.getInt(c.getColumnIndexOrThrow("repeat_count"));
            int verseCount = c.getInt(c.getColumnIndexOrThrow("verse_count"));

            String text =
                    "🌙 " + surahName + "\n" +
                            "من الآية " + fromVerse + " إلى الآية " + toVerse + "\n" +
                            "عدد الآيات: " + verseCount + "\n" +
                            "عدد التكرار: " + repeatCount + " مرات";

            list.add(text);
        }

        c.close();
        return list;
    }
}