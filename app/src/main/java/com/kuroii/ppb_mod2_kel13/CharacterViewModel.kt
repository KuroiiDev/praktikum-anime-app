package com.kuroii.ppb_mod2_kel13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuroii.ppb_mod2_kel13.model.Character
import com.kuroii.ppb_mod2_kel13.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val _allCharacterList = MutableStateFlow<List<Character>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val characterList: StateFlow<List<Character>> = combine(_allCharacterList, _searchQuery) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            list.filter { character ->
                character.name.contains(query.trim(), ignoreCase = true)
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

    fun fetchCharacters() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.service.getTopCharacters()
                _allCharacterList.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCharacterById(id: Int): Character? {
        return _allCharacterList.value.find { it.mal_id == id }
    }
}

