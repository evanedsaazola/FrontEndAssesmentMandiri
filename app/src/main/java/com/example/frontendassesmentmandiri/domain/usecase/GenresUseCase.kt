package com.example.frontendassesmentmandiri.domain.usecase

import com.example.frontendassesmentmandiri.domain.model.GenrePayload
import com.example.frontendassesmentmandiri.domain.repository.MovieRepository
import javax.inject.Inject

class GenresUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(): Result<List<GenrePayload>> {
        return repository.getGenres()
    }

}