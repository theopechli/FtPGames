package com.theopechli.ftpgames.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
            Row(
                horizontalArrangement = if (!canPop) Arrangement.Center else Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = if (canPop) {
            {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Navigate back icon"
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
fun FtpGamesBottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home icon") },
            label = { Text(stringResource(Screen.FtPGames.resourceId)) },
            selected = currentDestination?.hierarchy
                ?.any {
                    (it.route == Screen.FtPGames.route
                            || it.route == Screen.FtPGameDetails.route + "/{id}")
                } == true,
            onClick = {
                navController.navigate(Screen.FtPGames.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    painterResource(R.drawable.ic_baseline_info_24),
                    contentDescription = "Information icon"
                )
            },
            label = { Text(stringResource(Screen.FtPAbout.resourceId)) },
            selected = currentDestination?.hierarchy
                ?.any { it.route == Screen.FtPAbout.route } == true,
            onClick = {
                navController.navigate(Screen.FtPAbout.route) {
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

@Composable
fun NavigationGraph(navController: NavHostController, id: Long?) {
    NavHost(
        navController = navController,
        startDestination = Screen.FtPGames.route
    ) {
        composable(route = Screen.FtPGames.route) {
            val gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModel.Factory)
            GamesScreen(
                gamesViewModel = gamesViewModel,
                navController = navController
            )
        }
        composable(
            route = Screen.FtPGameDetails.route + "/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {
            val gameDetailsViewModel: GameDetailsViewModel =
                viewModel(factory = id?.let { it1 -> GameDetailsViewModel.Factory(it1) })
            GameDetailsScreen(gameDetailsViewModel = gameDetailsViewModel)
        }
        composable(route = Screen.FtPAbout.route) {
            AboutScreen()
        }
    }
}

@Composable
fun FtPGamesApp(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val id = navBackStackEntry?.arguments?.getLong("id")

    var canPop by remember { mutableStateOf(false) }
    navController.addOnDestinationChangedListener { controller, _, _ ->
        canPop = (controller.previousBackStackEntry != null)
                && (controller.currentDestination?.hierarchy
            ?.any { it.route == Screen.FtPAbout.route } == false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FtPGamesTopAppBar(
                canPop = canPop,
                navController = navController
            )
        },
        bottomBar = {
            FtpGamesBottomNavBar(
                navController = navController,
                currentDestination = currentDestination
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            NavigationGraph(
                navController = navController,
                id = id
            )
        }
    }
}