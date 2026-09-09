package com.kuroii.ppb_mod2_kel13.network

import com.kuroii.ppb_mod2_kel13.model.AnimeListResponse
import retrofit2.http.GET
interface ApiService {
    // Anime by id
    @GET("top/anime")
    suspend fun getTopAnime(): AnimeListResponse
}