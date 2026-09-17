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

    // Favorite state — in-memory storage
    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds

    val animeList: StateFlow<List<Anime>> = combine(_allAnimeList, _searchQuery, _favoriteIds) { list, query, favorites ->
        val filtered = if (query.isBlank()) {
            list
        } else {
            list.filter { anime ->
                anime.title.contains(query.trim(), ignoreCase = true)
            }
        }
        filtered.sortedByDescending { it.mal_id in favorites }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteAnimeList: StateFlow<List<Anime>> = combine(_allAnimeList, _favoriteIds) { list, favorites ->
        list.filter { it.mal_id in favorites }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(malId: Int) {
        _favoriteIds.value = if (malId in _favoriteIds.value) {
            _favoriteIds.value - malId
        } else {
            _favoriteIds.value + malId
        }
    }

    fun isFavorite(malId: Int): Boolean {
        return malId in _favoriteIds.value
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