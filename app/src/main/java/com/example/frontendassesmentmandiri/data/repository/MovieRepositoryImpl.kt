package com.example.frontendassesmentmandiri.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.frontendassesmentmandiri.BuildConfig
import com.example.frontendassesmentmandiri.data.api.TMDBApiService
import com.example.frontendassesmentmandiri.data.remote.MoviePagingSource
import com.example.frontendassesmentmandiri.domain.model.GenrePayload
import com.example.frontendassesmentmandiri.domain.model.MoviePayload
import com.example.frontendassesmentmandiri.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getDiscoverMovies(genreId: Int): Flow<PagingData<MoviePayload>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(apiService, BuildConfig.TMDB_API_KEY, genreId)
            }
        ).flow.map { pagingData ->
            pagingData.map { movieData ->
                MoviePayload (
                    id = movieData.id,
                    title = movieData.title,
                    posterUrl = movieData.posterPath?.let { path ->
                        "https://image.tmdb.org/t/p/w500$path" },
                    releaseDate = movieData.releaseDate
                )
            }
        }
    }
}