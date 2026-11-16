package com.example.frontendassesmentmandiri.presentation.navigation

sealed class Screen(val route: String) {
    object Genres: Screen("genres")
    object Discover: Screen("discover/{genreId}") {
        fun createRoute(genreId: Int) = "discover/$genreId"
    }
}