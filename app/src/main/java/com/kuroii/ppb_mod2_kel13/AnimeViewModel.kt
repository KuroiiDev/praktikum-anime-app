package com.kuroii.ppb_mod2_kel13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuroii.ppb_mod2_kel13.model.Anime
import com.kuroii.ppb_mod2_kel13.model.AnimeListResponse
import com.kuroii.ppb_mod2_kel13.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AnimeViewModel : ViewModel() {
    private val _allAnimeList = MutableStateFlow<List<Anime>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val animeList: StateFlow<List<Anime>> = combine(_allAnimeList, _searchQuery) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            list.filter { anime ->
                anime.title.contains(query.trim(), ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun fetchTopAnime() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response: AnimeListResponse = ApiClient.service.getTopAnime()
                _allAnimeList.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAnimeById(id: Int): Anime? {
        return _allAnimeList.value.find { it.mal_id == id }
    }
}