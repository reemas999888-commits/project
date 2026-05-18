package com.example.hifzapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "hifz.db";
    private static final int    DB_VERSION = 1;

    // ── جدول الآيات ──────────────────────────────
    private static final String TABLE_AYAHS =
            "CREATE TABLE ayahs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "surah_number INTEGER," +
                    "ayah_number  INTEGER," +
                    "page_number  INTEGER," +
                    "ayah_text    TEXT," +
                    "audio_url    TEXT)";

    // ── جدول تقدم المستخدم ───────────────────────
    private static final String TABLE_PROGRESS =
            "CREATE TABLE progress (" +
                    "id            INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "current_surah INTEGER," +
                    "current_ayah  INTEGER," +
                    "last_used     TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_AYAHS);
        db.execSQL(TABLE_PROGRESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ayahs");
        db.execSQL("DROP TABLE IF EXISTS progress");
        onCreate(db);
    }

    // ── إدراج آية أو تحديثها ─────────────────────
    public void insertOrUpdateAyah(int surahNum, int ayahNum, int pageNum,
                                   String text, String audioUrl) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("surah_number", surahNum);
        cv.put("ayah_number",  ayahNum);
        cv.put("page_number",  pageNum);
        cv.put("ayah_text",    text);
        cv.put("audio_url",    audioUrl);

        int rows = db.update("ayahs", cv,
                "surah_number=? AND ayah_number=?",
                new String[]{String.valueOf(surahNum), String.valueOf(ayahNum)});

        if (rows == 0) db.insert("ayahs", null, cv);
    }

    // ── جلب آية واحدة ────────────────────────────
    public Ayah getAyah(int surahNum, int ayahNum) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("ayahs", null,
                "surah_number=? AND ayah_number=?",
                new String[]{String.valueOf(surahNum), String.valueOf(ayahNum)},
                null, null, null);

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

    // ── حفظ تقدم المستخدم ────────────────────────
    public void saveProgress(int surahNum, int ayahNum) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("current_surah", surahNum);
        cv.put("current_ayah",  ayahNum);
        cv.put("last_used", new java.util.Date().toString());

        Cursor c = db.rawQuery("SELECT id FROM progress LIMIT 1", null);
        if (c.moveToFirst()) {
            db.update("progress", cv, null, null);
        } else {
            db.insert("progress", null, cv);
        }
        c.close();
    }

    // ── استرجاع تقدم المستخدم ────────────────────
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
}