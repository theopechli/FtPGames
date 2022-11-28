package com.theopechli.ftpgames.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.theopechli.ftpgames.R
import com.theopechli.ftpgames.ui.screens.*

@Composable
fun FtPGamesApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            NavHost(
                navController = navController,
                startDestination = FtPGamesScreen.Games.name
            ) {
                composable(route = FtPGamesScreen.Games.name) {
                    val gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModel.Factory)
                    GamesScreen(
                        gamesUiState = gamesViewModel.gamesUiState,
                        navController
                    )
                }
                composable(route = FtPGamesScreen.GameDetails.name + "/{id}") { navBackStack ->
                    val id = navBackStack.arguments?.getString("id")?.toLong()
                    val gameDetailsViewModel: GameDetailsViewModel =
                        viewModel(factory = id?.let { it1 -> GameDetailsViewModel.Factory(it1) })
                    GameDetailsScreen(gameDetailsUiState = gameDetailsViewModel.gameDetailsUiState)
                }
            }

        }
    }
}