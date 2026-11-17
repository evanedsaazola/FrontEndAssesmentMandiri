package com.example.frontendassesmentmandiri.domain.repository

import androidx.paging.PagingData
import com.example.frontendassesmentmandiri.domain.model.GenrePayload
import com.example.frontendassesmentmandiri.domain.model.MoviePayload
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getGenres(): Result<List<GenrePayload>>

    fun getDiscoverMovies(genreId: Int): Flow<PagingData<MoviePayload>>

}