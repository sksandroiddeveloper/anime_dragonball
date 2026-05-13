package com.dragonball.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dragonball.app.ui.screens.detail.CharacterDetailScreen
import com.dragonball.app.ui.screens.detail.PlanetDetailScreen
import com.dragonball.app.ui.screens.home.HomeScreen
import com.dragonball.app.ui.screens.planets.PlanetsScreen

// ─── Screen Routes ────────────────────────────────────────────────────────────

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CharacterDetail : Screen("character/{id}") {
        fun createRoute(id: Int) = "character/$id"
    }
    object Planets : Screen("planets")
    object PlanetDetail : Screen("planet/{id}") {
        fun createRoute(id: Int) = "planet/$id"
    }
}

// ─── NavGraph ─────────────────────────────────────────────────────────────────

@Composable
fun DragonBallNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            HomeScreen(
                onCharacterClick = { id ->
                    navController.navigate(Screen.CharacterDetail.createRoute(id))
                },
                onPlanetsClick = {
                    navController.navigate(Screen.Planets.route)
                }
            )
        }

        composable(
            route = Screen.CharacterDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(350)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(350)
                )
            }
        ) { backStack ->
            val id = backStack.arguments?.getInt("id") ?: return@composable
            CharacterDetailScreen(
                characterId = id,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Planets.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(350)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(350)
                )
            }
        ) {
            PlanetsScreen(
                onBack = { navController.popBackStack() },
                onPlanetClick = { id ->
                    navController.navigate(Screen.PlanetDetail.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.PlanetDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(350)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(350)
                )
            }
        ) { backStack ->
            val id = backStack.arguments?.getInt("id") ?: return@composable
            PlanetDetailScreen(
                planetId = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
