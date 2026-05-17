package com.example.hifzapp.data;

import com.example.hifzapp.model.Surah;
import java.util.ArrayList;
import java.util.List;

public class SurahData {

    public static List<Surah> getAllSurahs() {
        List<Surah> list = new ArrayList<>();

        list.add(new Surah("الفاتحة",  false, true,  1,   7));
        list.add(new Surah("البقرة",   false, false, 2, 286));
        list.add(new Surah("آل عمران", false, false, 3, 200));
        list.add(new Surah("النساء",   false, false, 4, 176));
        list.add(new Surah("المائدة",  false, false, 5, 120));
        list.add(new Surah("الأنعام",  false, false, 6, 165));
        list.add(new Surah("الأعراف",  false, false, 7, 206));

        list.add(new Surah("النبأ",    true,  true,  78, 40));
        list.add(new Surah("النازعات", true,  true,  79, 46));
        list.add(new Surah("عبس",      true,  true,  80, 42));
        list.add(new Surah("التكوير",  true,  true,  81, 29));

        return list;
    }
}