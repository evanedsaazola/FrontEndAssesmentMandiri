package com.example.frontendassesmentmandiri.data.repository

import com.example.frontendassesmentmandiri.BuildConfig
import com.example.frontendassesmentmandiri.data.api.TMDBApiService
import com.example.frontendassesmentmandiri.domain.model.GenrePayload
import com.example.frontendassesmentmandiri.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: TMDBApiService
) : MovieRepository {

    override suspend fun getGenres(): Result<List<GenrePayload>> {
        return try {
            val response = apiService.getGenres(apiKey = BuildConfig.TMDB_API_KEY)
            val genresResult = response.genres.map {
                GenrePayload(
                    id = it.id,
                    name = it.name
                )
            }
            Result.success(genresResult)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

}