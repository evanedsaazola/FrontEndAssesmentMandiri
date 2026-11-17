package com.example.frontendassesmentmandiri.domain.usecase

import androidx.paging.PagingData
import com.example.frontendassesmentmandiri.domain.model.MoviePayload
import com.example.frontendassesmentmandiri.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiscoverMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(genreId: Int): Flow<PagingData<MoviePayload>> {
        return repository.getDiscoverMovies(genreId)
    }

}