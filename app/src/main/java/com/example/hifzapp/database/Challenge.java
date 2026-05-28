package com.example.hifzapp.database;

public class Challenge {

    public int id;
    public String challengeDate;
    public int surahNumber;
    public String surahName;
    public String goalType;
    public int goalTarget;
    public int progressCount;

    public Challenge(int id,
                     String challengeDate,
                     int surahNumber,
                     String surahName,
                     String goalType,
                     int goalTarget,
                     int progressCount) {

        this.id = id;
        this.challengeDate = challengeDate;
        this.surahNumber = surahNumber;
        this.surahName = surahName;
        this.goalType = goalType;
        this.goalTarget = goalTarget;
        this.progressCount = progressCount;
    }

    public boolean isCompleted() {
        return progressCount >= goalTarget;
    }
}