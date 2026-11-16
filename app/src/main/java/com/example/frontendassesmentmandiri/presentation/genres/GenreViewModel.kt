package com.example.frontendassesmentmandiri.presentation.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendassesmentmandiri.domain.model.GenrePayload
import com.example.frontendassesmentmandiri.domain.usecase.GenresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val genresUseCase: GenresUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<GenreUiState>(GenreUiState.Loading)
    val uiState: StateFlow<GenreUiState> = _uiState.asStateFlow()

    init {
        fetchGenres()
    }

    fun fetchGenres() {
        viewModelScope.launch {
            _uiState.value = GenreUiState.Loading
            genresUseCase().fold(
                onSuccess = { genreResponse -> _uiState.value = GenreUiState.Success(genreResponse) },
                onFailure = { error -> _uiState.value = GenreUiState.Error(error.message ?: "Unknown Error!")}
            )
        }
    }
}

sealed class GenreUiState {
    object Loading: GenreUiState()
    data class Success(val genres: List<GenrePayload>): GenreUiState()
    data class Error(val message: String): GenreUiState()
}