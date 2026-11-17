package com.example.frontendassesmentmandiri.domain.usecase


import com.example.frontendassesmentmandiri.domain.model.MovieDetailPayload
import com.example.frontendassesmentmandiri.domain.repository.MovieRepository
import javax.inject.Inject

class DetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(movieId: Int): Result<MovieDetailPayload> {
        return repository.getMovieDetails(movieId)
    }

}