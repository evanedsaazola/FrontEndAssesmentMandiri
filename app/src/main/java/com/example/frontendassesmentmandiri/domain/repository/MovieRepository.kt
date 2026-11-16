package com.example.frontendassesmentmandiri.domain.repository

import com.example.frontendassesmentmandiri.domain.model.GenrePayload

interface MovieRepository {

    suspend fun getGenres(): Result<List<GenrePayload>>

}