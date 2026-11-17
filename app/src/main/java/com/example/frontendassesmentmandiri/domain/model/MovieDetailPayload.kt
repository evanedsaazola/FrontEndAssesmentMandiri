package com.example.frontendassesmentmandiri.domain.model

data class MovieDetailPayload(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseDate: String?,
    val youtubeTrailer: String?
)
