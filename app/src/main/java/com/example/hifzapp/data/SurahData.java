package com.example.hifzapp.data;

import com.example.hifzapp.model.Surah;
import java.util.ArrayList;
import java.util.List;

public class SurahData {

    public static List<Surah> getAllSurahs() {
        List<Surah> list = new ArrayList<>();

        list.add(new Surah("الفاتحة", false, true));
        list.add(new Surah("البقرة", false, false));
        list.add(new Surah("آل عمران", false, false));
        list.add(new Surah("النساء", false, false));
        list.add(new Surah("المائدة", false, false));
        list.add(new Surah("الأنعام", false, false));
        list.add(new Surah("الأعراف", false, false));

        list.add(new Surah("النبأ", true, true));
        list.add(new Surah("النازعات", true, true));
        list.add(new Surah("عبس", true, true));
        list.add(new Surah("التكوير", true, true));

        return list;
    }
}