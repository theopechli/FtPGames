package com.theopechli.ftpgames.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.theopechli.ftpgames.ui.screens.HomeScreen
import com.theopechli.ftpgames.R
import com.theopechli.ftpgames.ui.screens.GamesViewModel

@Composable
fun FtPGamesApp() {
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
            val gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModel.Factory)
            HomeScreen(gamesUiState = gamesViewModel.gamesUiState)
        }
    }
}