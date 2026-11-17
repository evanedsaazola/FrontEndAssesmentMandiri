package com.example.frontendassesmentmandiri.domain.usecase

import androidx.paging.PagingData
import com.example.frontendassesmentmandiri.domain.model.ReviewPayload
import com.example.frontendassesmentmandiri.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieReviewUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(movieId: Int): Flow<PagingData<ReviewPayload>> {
        return repository.getMovieReviews(movieId)
    }

}