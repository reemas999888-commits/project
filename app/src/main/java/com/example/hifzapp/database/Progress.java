package com.example.hifzapp.database;

public class Progress {
    public int currentSurah;
    public int currentAyah;
    public String lastUsed;

    public Progress(int currentSurah, int currentAyah, String lastUsed) {
        this.currentSurah = currentSurah;
        this.currentAyah = currentAyah;
        this.lastUsed = lastUsed;
    }
}
