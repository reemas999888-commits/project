package com.example.hifzapp.database;

public class Progress {
    public int id;
    public int currentSurah;
    public int currentAyah;

    public Progress(int id, int currentSurah, int currentAyah) {
        this.id = id;
        this.currentSurah = currentSurah;
        this.currentAyah = currentAyah;
    }
}
