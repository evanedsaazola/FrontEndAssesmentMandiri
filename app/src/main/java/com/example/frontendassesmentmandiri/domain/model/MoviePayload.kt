package com.example.frontendassesmentmandiri.domain.model

data class MoviePayload (
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val releaseDate: String?
)
