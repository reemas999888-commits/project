package com.example.hifzapp.api;

import com.example.hifzapp.model.SurahResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
//يحدد الطلبات اللي نرسلها لل API
    @GET("surah")
    Call<SurahResponse> getSurahs();
}