package com.example.hifzapp.database;

public class Ayah {
    public int id;
    public int surahNumber;
    public int ayahNumber;
    public int pageNumber;
    public String ayahText;
    public String imageUrl;
    public String audioUrl;

    public Ayah(int id, int surahNumber, int ayahNumber, int pageNumber,
                String ayahText, String imageUrl, String audioUrl) {
        this.id = id;
        this.surahNumber = surahNumber;
        this.ayahNumber = ayahNumber;
        this.pageNumber = pageNumber;
        this.ayahText = ayahText;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
    }
}