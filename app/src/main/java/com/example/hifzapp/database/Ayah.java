package com.example.hifzapp.database;

public class Ayah {

    public int id;
    public int surahNumber;
    public int ayahNumber;
    public int pageNumber;
    public String text;
    public String imageUrl;
    public String audioUrl;

    // Constructor فارغ مطلوب للـ Retrofit/Gson
    public Ayah() {
    }

    public Ayah(int id,
                int surahNumber,
                int ayahNumber,
                int pageNumber,
                String text,
                String imageUrl,
                String audioUrl) {

        this.id = id;
        this.surahNumber = surahNumber;
        this.ayahNumber = ayahNumber;
        this.pageNumber = pageNumber;
        this.text = text;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
    }

    public int getId() {
        return id;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public int getAyahNumber() {
        return ayahNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }
}