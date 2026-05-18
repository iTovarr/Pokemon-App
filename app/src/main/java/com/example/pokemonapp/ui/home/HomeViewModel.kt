package com.example.pokemonapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonapp.data.model.PokemonNamedResult
import com.example.pokemonapp.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val pokemon: List<PokemonNamedResult>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchPokemon()
    }

    fun fetchPokemon() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val response = RetrofitClient.apiService.getPokemonList()
                _uiState.value = HomeUiState.Success(response.results)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Error de conexión")
            }
        }
    }
}