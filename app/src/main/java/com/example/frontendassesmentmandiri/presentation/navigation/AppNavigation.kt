package com.example.frontendassesmentmandiri.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.frontendassesmentmandiri.presentation.discover.DiscoverMovieScreen
import com.example.frontendassesmentmandiri.presentation.genres.GenreScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Genres.route
    ) {
        composable(
            route = Screen.Genres.route
        ) {
            GenreScreen(
                onGenreClick = { genreId ->
                    navController.navigate(Screen.Discover.createRoute(genreId))
                }
            )
        }

        composable(
            route = Screen.Discover.route,
            arguments = listOf(
                navArgument("genreId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val genreId = backStackEntry.arguments?.getInt("genreId") ?: return@composable

            DiscoverMovieScreen(
                genreId = genreId,
                onMovieClick = { movieId -> }
            )
        }
    }

}