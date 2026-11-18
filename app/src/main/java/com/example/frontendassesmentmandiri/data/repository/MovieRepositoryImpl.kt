package com.example.frontendassesmentmandiri.data.repository

import androidx.compose.ui.res.stringResource
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.frontendassesmentmandiri.BuildConfig
import com.example.frontendassesmentmandiri.R
import com.example.frontendassesmentmandiri.data.api.TMDBApiService
import com.example.frontendassesmentmandiri.data.remote.MoviePagingSource
import com.example.frontendassesmentmandiri.data.remote.MovieReviewPagingSource
import com.example.frontendassesmentmandiri.domain.model.GenrePayload
import com.example.frontendassesmentmandiri.domain.model.MovieDetailPayload
import com.example.frontendassesmentmandiri.domain.model.MoviePayload
import com.example.frontendassesmentmandiri.domain.model.ReviewPayload
import com.example.frontendassesmentmandiri.domain.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
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
            Result.failure(errorMapping(error))
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

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailPayload> {
        return try {
            coroutineScope {
                val asyncDetails = async {
                    apiService.getMovieDetails(
                        movieId = movieId,
                        apiKey = BuildConfig.TMDB_API_KEY
                    )
                }
                val asyncVideos = async {
                    apiService.getMovieVideos(
                        movieId = movieId,
                        apiKey = BuildConfig.TMDB_API_KEY
                    )
                }

                val movieDetails = asyncDetails.await()
                val movieVideos = asyncVideos.await()

                val trailer =
                    movieVideos.results.firstOrNull {
                        it.site == "YouTube" && it.type == "Trailer"
                    }?.key ?:
                    movieVideos.results.firstOrNull {
                        it.site == "YouTube"
                    }?.key

                Result.success(
                    MovieDetailPayload(
                        id = movieDetails.id,
                        title = movieDetails.title,
                        overview = movieDetails.overview,
                        posterUrl = movieDetails.posterPath?.let { path ->
                            "https://image.tmdb.org/t/p/w500$path"
                        },
                        releaseDate = movieDetails.releaseDate,
                        youtubeTrailer = trailer
                    )
                )
            }
        } catch (error: Exception) {
            Result.failure(errorMapping(error))
        }
    }

    override fun getMovieReviews(movieId: Int): Flow<PagingData<ReviewPayload>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MovieReviewPagingSource(
                    apiService = apiService,
                    apiKey = BuildConfig.TMDB_API_KEY,
                    movieId = movieId
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { reviewData ->
                ReviewPayload(
                    id = reviewData.id,
                    author = reviewData.author,
                    content = reviewData.content
                )
            }
        }
    }

    private fun errorMapping(error: Exception): Exception {
        val errorMessage = when (error) {
            is IOException -> "Please check your internet connection and try again."
            is HttpException -> {
                when (error.code()) {
                    401 -> "Invalid API Key. Please contact support."
                    404 -> "Movie data not found."
                    in 500..599 -> "TMDB Server error. PLease try again later."
                    else -> "An unexpected network error"
                }
            }
            else -> "An unexpected error happened: ${error.localizedMessage}"
        }
        return Exception(errorMessage)
    }
}