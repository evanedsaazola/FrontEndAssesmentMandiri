package com.example.frontendassesmentmandiri.presentation.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.frontendassesmentmandiri.domain.model.MoviePayload
import com.example.frontendassesmentmandiri.domain.usecase.DiscoverMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverMovieUseCase: DiscoverMovieUseCase
): ViewModel() {

    fun getMovies(genreId: Int): Flow<PagingData<MoviePayload>> {
        return discoverMovieUseCase(genreId).cachedIn(viewModelScope)
    }

}