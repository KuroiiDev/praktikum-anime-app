package com.kuroii.ppb_mod2_kel13

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.kuroii.ppb_mod2_kel13.ui.theme.PPB_Mod2_Kel13Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PPB_Mod2_Kel13Theme {
                AnimeApp()
            }
        }
    }
}

@Composable
fun AnimeApp() {
    val navController = rememberNavController()
    val viewModel: AnimeViewModel = viewModel()
    val characterViewModel: CharacterViewModel = viewModel()
    val items = listOf(Screen.Anime, Screen.Characters, Screen.Favorite, Screen.About)

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val isDetailScreen = currentRoute?.startsWith("detail/") == true ||
            currentRoute?.startsWith("character_detail/") == true

    Scaffold(
        bottomBar = {
            if (!isDetailScreen) {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                when (screen) {
                                    Screen.Anime -> Icon(
                                        Icons.Default.Movie,
                                        contentDescription = "Anime"
                                    )
                                    Screen.Characters -> Icon(
                                        Icons.Default.Person,
                                        contentDescription = "Characters"
                                    )
                                    Screen.Favorite -> Icon(
                                        Icons.Default.Favorite,
                                        contentDescription = "Favorite"
                                    )
                                    Screen.About -> Icon(
                                        Icons.Default.Info,
                                        contentDescription = "About"
                                    )
                                    else -> {}
                                }
                            },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Anime.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Anime.route) {
                AnimeListScreen(
                    viewModel = viewModel,
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.Detail.createRoute(animeId))
                    }
                )
            }
            composable(Screen.Characters.route) {
                CharacterListScreen(
                    viewModel = characterViewModel,
                    onCharacterClick = { characterId ->
                        navController.navigate(Screen.CharacterDetail.createRoute(characterId))
                    }
                )
            }
            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    viewModel = viewModel,
                    onAnimeClick = { animeId ->
                        navController.navigate(Screen.Detail.createRoute(animeId))
                    }
                )
            }
            composable(Screen.About.route) {
                AboutScreen()
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("animeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
                DetailScreen(
                    animeId = animeId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.CharacterDetail.route,
                arguments = listOf(navArgument("characterId") { type = NavType.IntType })
            ) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
                CharacterDetailScreen(
                    characterId = characterId,
                    viewModel = characterViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    } // Penutup Scaffold
} // Penutup fungsi AnimeApp