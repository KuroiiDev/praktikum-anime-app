package com.kuroii.ppb_mod2_kel13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuroii.ppb_mod2_kel13.model.AnimeListResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.kuroii.ppb_mod2_kel13.model.Anime
import com.kuroii.ppb_mod2_kel13.network.ApiClient

class AnimeViewModel : ViewModel() {
    private val _animeList =
        MutableStateFlow<List<Anime>>(emptyList())
    val animeList: StateFlow<List<Anime>> = _animeList
    fun fetchTopAnime() {

        viewModelScope.launch {
            try {
                val response: AnimeListResponse =
                    ApiClient.service.getTopAnime()
                _animeList.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}