package com.kuroii.ppb_mod2_kel13.network

import com.kuroii.ppb_mod2_kel13.model.AnimeListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top/anime")
    suspend fun getTopAnime(): AnimeListResponse

    @GET("anime")
    suspend fun searchAnime(@Query("q") query: String? = null): AnimeListResponse
}