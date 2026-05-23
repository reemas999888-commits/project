package com.example.hifzapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hifz_database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_AYAHS = "ayahs";
    private static final String TABLE_PROGRESS = "progress";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createAyahsTable = "CREATE TABLE " + TABLE_AYAHS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "surah_number INTEGER, " +
                "ayah_number INTEGER, " +
                "page_number INTEGER, " +
                "ayah_text TEXT, " +
                "image_url TEXT, " +
                "audio_url TEXT)";

        String createProgressTable = "CREATE TABLE " + TABLE_PROGRESS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "current_surah INTEGER, " +
                "current_ayah INTEGER)";

        db.execSQL(createAyahsTable);
        db.execSQL(createProgressTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AYAHS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);
        onCreate(db);
    }

    // إضافة آية
    public boolean addAyah(int surahNumber, int ayahNumber, int pageNumber,
                           String ayahText, String imageUrl, String audioUrl) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("surah_number", surahNumber);
        values.put("ayah_number", ayahNumber);
        values.put("page_number", pageNumber);
        values.put("ayah_text", ayahText);
        values.put("image_url", imageUrl);
        values.put("audio_url", audioUrl);

        long result = db.insert(TABLE_AYAHS, null, values);
        db.close();

        return result != -1;
    }

    // قراءة كل الآيات
    public ArrayList<Ayah> getAllAyahs() {
        ArrayList<Ayah> ayahList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AYAHS, null);

        if (cursor.moveToFirst()) {
            do {
                Ayah ayah = new Ayah(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("surah_number")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ayah_number")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("page_number")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ayah_text")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
                        cursor.getString(cursor.getColumnIndexOrThrow("audio_url"))
                );

                ayahList.add(ayah);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return ayahList;
    }

    // قراءة آية محددة
    public Ayah getAyah(int surahNumber, int ayahNumber) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_AYAHS +
                        " WHERE surah_number = ? AND ayah_number = ?",
                new String[]{String.valueOf(surahNumber), String.valueOf(ayahNumber)}
        );

        Ayah ayah = null;

        if (cursor.moveToFirst()) {
            ayah = new Ayah(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("surah_number")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("ayah_number")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("page_number")),
                    cursor.getString(cursor.getColumnIndexOrThrow("ayah_text")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
                    cursor.getString(cursor.getColumnIndexOrThrow("audio_url"))
            );
        }

        cursor.close();
        db.close();

        return ayah;
    }

    // تعديل آية
    public boolean updateAyah(int id, int surahNumber, int ayahNumber, int pageNumber,
                              String ayahText, String imageUrl, String audioUrl) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("surah_number", surahNumber);
        values.put("ayah_number", ayahNumber);
        values.put("page_number", pageNumber);
        values.put("ayah_text", ayahText);
        values.put("image_url", imageUrl);
        values.put("audio_url", audioUrl);

        int result = db.update(TABLE_AYAHS, values, "id = ?",
                new String[]{String.valueOf(id)});

        db.close();

        return result > 0;
    }

    // حذف آية
    public boolean deleteAyah(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_AYAHS, "id = ?",
                new String[]{String.valueOf(id)});

        db.close();

        return result > 0;
    }

    // حفظ آخر موضع للحفظ
    public void saveProgress(int surahNumber, int ayahNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_PROGRESS, null, null);

        ContentValues values = new ContentValues();
        values.put("current_surah", surahNumber);
        values.put("current_ayah", ayahNumber);

        db.insert(TABLE_PROGRESS, null, values);
        db.close();
    }

    // قراءة آخر موضع للحفظ
    public Progress getProgress() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PROGRESS + " LIMIT 1", null);

        Progress progress = null;

        if (cursor.moveToFirst()) {
            progress = new Progress(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("current_surah")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("current_ayah"))
            );
        }

        cursor.close();
        db.close();

        return progress;
    }
}