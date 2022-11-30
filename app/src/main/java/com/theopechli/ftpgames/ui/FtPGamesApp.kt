package com.theopechli.ftpgames.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.theopechli.ftpgames.R
import com.theopechli.ftpgames.ui.screens.*

@Composable
fun FtPGamesTopAppBar(
    canPop: Boolean,
    navController: NavHostController
) {
    /* TODO add about/credits info in menu */
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h5
            )
        },
        navigationIcon = if (canPop) {
            {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            }
        } else {
            null
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )
}

@Composable
fun FtPGamesApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            var canPop by remember { mutableStateOf(false) }
            navController.addOnDestinationChangedListener { controller, _, _ ->
                canPop = controller.previousBackStackEntry != null
            }
            FtPGamesTopAppBar(
                canPop = canPop,
                navController = navController
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            NavHost(
                navController = navController,
                startDestination = FtPGamesScreen.Games.name
            ) {
                composable(route = FtPGamesScreen.Games.name) {
                    val gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModel.Factory)
                    GamesScreen(
                        gamesViewModel = gamesViewModel,
                        navController = navController
                    )
                }
                composable(
                    route = FtPGamesScreen.GameDetails.name + "/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.LongType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getLong("id")
                    val gameDetailsViewModel: GameDetailsViewModel =
                        viewModel(factory = id?.let { it1 -> GameDetailsViewModel.Factory(it1) })
                    GameDetailsScreen(gameDetailsViewModel = gameDetailsViewModel)
                }
            }

        }
    }
}