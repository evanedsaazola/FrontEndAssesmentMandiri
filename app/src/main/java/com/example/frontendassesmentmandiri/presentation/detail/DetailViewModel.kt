package com.example.frontendassesmentmandiri.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.frontendassesmentmandiri.domain.model.MovieDetailPayload
import com.example.frontendassesmentmandiri.domain.model.ReviewPayload
import com.example.frontendassesmentmandiri.domain.usecase.DetailsUseCase
import com.example.frontendassesmentmandiri.domain.usecase.MovieReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val movieDetailsUseCase: DetailsUseCase,
    private val movieReviewUseCase: MovieReviewUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            movieDetailsUseCase(movieId).fold(
                onSuccess = { detailResponse -> _uiState.value = DetailsUiState.Success(detailResponse) },
                onFailure = { error -> _uiState.value = DetailsUiState.Error(error.message ?: "Unknown Error") }
            )
        }
    }

    fun fetchMovieReviews(movieId: Int): Flow<PagingData<ReviewPayload>> {
        return movieReviewUseCase(movieId).cachedIn(viewModelScope)
    }
}

sealed class DetailsUiState {
    object Loading: DetailsUiState()
    data class Success(val details: MovieDetailPayload): DetailsUiState()
    data class Error(val message: String): DetailsUiState()
}