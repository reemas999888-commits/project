package com.example.hifzapp.model;

public class Surah {

    private String name;
    private boolean juzAmma;
    private boolean isShort;
    private int number;
    private int verseCount;

    public Surah(String name, boolean juzAmma, boolean isShort, int number, int verseCount) {
        this.name = name;
        this.juzAmma = juzAmma;
        this.isShort = isShort;
        this.number = number;
        this.verseCount = verseCount;
    }

    public String getName() { return name; }
    public boolean isJuzAmma() { return juzAmma; }
    public boolean isShort() { return isShort; }
    public int getNumber() { return number; }
    public int getVerseCount() { return verseCount; }
}