package com.example.frontendassesmentmandiri.domain.repository

import androidx.paging.PagingData
import com.example.frontendassesmentmandiri.domain.model.GenrePayload
import com.example.frontendassesmentmandiri.domain.model.MovieDetailPayload
import com.example.frontendassesmentmandiri.domain.model.MoviePayload
import com.example.frontendassesmentmandiri.domain.model.ReviewPayload
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getGenres(): Result<List<GenrePayload>>

    fun getDiscoverMovies(genreId: Int): Flow<PagingData<MoviePayload>>

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailPayload>

    fun getMovieReviews(movieId: Int): Flow<PagingData<ReviewPayload>>

}