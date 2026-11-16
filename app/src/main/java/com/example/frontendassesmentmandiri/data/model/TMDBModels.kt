package com.example.frontendassesmentmandiri.data.model

import com.squareup.moshi.Json

data class GenreResponse(
    @field:Json(name = "genres") val genres: List<Genre>
)

data class Genre(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "name") val name: String
)

data class MovieResponse(
    @field:Json(name = "page") val page: Int,
    @field:Json(name = "results") val results: List<Movie>,
    @field:Json(name = "total_pages") val totalPages: Int
)

data class Movie(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "overview") val overview: String,
    @field:Json(name = "poster_path") val posterPath: String?,
    @field:Json(name = "release_date") val releaseDate: String?
)

data class ReviewResponse(
    @field:Json(name = "results") val results: List<Review>
)

data class Review(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "author") val author: String,
    @field:Json(name = "content") val content: String
)

data class VideoResponse(
    @field:Json(name = "results") val results: List<Video>
)

data class Video(
    @field:Json(name = "key") val key: String,
    @field:Json(name = "site") val site: String,
    @field:Json(name = "type") val type: String
)
