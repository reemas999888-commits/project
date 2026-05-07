package com.example.hifzapp.model;

public class Surah {

    private String name;
    private boolean juzAmma;
    private boolean isShort;

    public Surah(String name, boolean juzAmma, boolean isShort) {
        this.name = name;
        this.juzAmma = juzAmma;
        this.isShort = isShort;
    }

    public String getName() {
        return name;
    }

    public boolean isJuzAmma() {
        return juzAmma;
    }

    public boolean isShort() {
        return isShort;
    }
}
