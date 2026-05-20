package com.example.hifzapp.model;

public class Surah {

    private int number;
    private String name;
    private String englishName;
    private int numberOfAyahs;
    private String revelationType;

    // Constructor للبيانات المحلية
    public Surah(int number, String name, int numberOfAyahs, String revelationType) {
        this.number = number;
        this.name = name;
        this.numberOfAyahs = numberOfAyahs;
        this.revelationType = revelationType;
    }

    public int getNumber() { return number; }
    public String getName() { return name; }
    public String getEnglishName() { return englishName; }
    public int getNumberOfAyahs() { return numberOfAyahs; }
    public String getRevelationType() { return revelationType; }

    public String getRevelationTypeArabic() {
        if (revelationType == null) return "مكية";
        return revelationType.equalsIgnoreCase("Medinan") ? "مدنية" : "مكية";
    }
}