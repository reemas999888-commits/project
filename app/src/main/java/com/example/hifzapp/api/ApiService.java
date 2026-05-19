package com.example.hifzapp.api;

import com.example.hifzapp.model.SurahResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("surah")
    Call<SurahResponse> getSurahs();
}