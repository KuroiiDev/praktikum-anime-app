package com.kuroii.ppb_mod2_kel13.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.jikan.moe/v4/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service: ApiService =
        retrofit.create(ApiService::class.java)
}