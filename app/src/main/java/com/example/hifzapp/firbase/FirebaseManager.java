package com.example.hifzapp.firbase;



import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    private static FirebaseManager instance;
    private DatabaseReference database;

    private FirebaseManager() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    // جلب رابط صورة الصفحة من API مجاني
    public String getPageImageUrl(int pageNumber) {
        return "https://www.searchtruth.com/quran/images/large/page-"
                + String.format("%03d", pageNumber) + ".jpg";
    }

    // جلب رابط صوت الآية من API مجاني
    public String getAyahAudioUrl(int ayahNumberInQuran) {
        return "https://cdn.islamic.network/quran/audio/128/ar.alafasy/"
                + ayahNumberInQuran + ".mp3";
    }

    // حفظ تقدم المستخدم على Firebase Realtime Database
    public void saveUserProgress(int surahNumber, int ayahNumber) {
        database.child("progress").child("current_surah").setValue(surahNumber);
        database.child("progress").child("current_ayah").setValue(ayahNumber);
    }

    // جلب تقدم المستخدم من Firebase
    public void getUserProgress(OnProgressFetchedListener listener) {
        database.child("progress").get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        int surah = snapshot.child("current_surah").getValue(Integer.class);
                        int ayah = snapshot.child("current_ayah").getValue(Integer.class);
                        listener.onSuccess(surah, ayah);
                    }
                })
                .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public interface OnUrlFetchedListener {
        void onSuccess(String url);
        void onFailure(String error);
    }

    public interface OnProgressFetchedListener {
        void onSuccess(int surahNumber, int ayahNumber);
        void onFailure(String error);
    }
}