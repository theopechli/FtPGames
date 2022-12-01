package com.theopechli.ftpgames.ui.screens

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.theopechli.ftpgames.R
import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.ui.theme.FtPGamesTheme

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object FtPGames : Screen("games", R.string.games)
    object FtPGameDetails : Screen("gamedetails", R.string.game_details)
    object FtPAbout : Screen("about", R.string.about)
}

@Composable
fun GamesScreen(
    gamesViewModel: GamesViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    when (gamesViewModel.gamesUiState) {
        is GamesUiState.Loading -> LoadingScreen(modifier)
        is GamesUiState.Success -> GamesList(gamesViewModel, navController)
        else -> ErrorScreen(modifier)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.loading))
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GamesList(
    gamesViewModel: GamesViewModel,
    navController: NavController?
) {
    val refreshing by gamesViewModel.refreshing.collectAsState()
    val refreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            gamesViewModel.refresh()
        }
    )
    val games =
        remember { mutableStateOf((gamesViewModel.gamesUiState as GamesUiState.Success).games) }

    Box(Modifier.pullRefresh(refreshState)) {
        LazyColumn {
            items(
                items = games.value,
                key = { game ->
                    game.title
                    game.short_description
                    game.thumbnail
                }
            ) { game ->
                GameListItem(
                    game = game,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    navController = navController,
                    onLongClick = { },
                    onDoubleClick = { }
                )
            }
        }
        PullRefreshIndicator(refreshing, refreshState, Modifier.align(Alignment.TopCenter))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameListItem(
    game: Game,
    modifier: Modifier = Modifier,
    navController: NavController?,
    onLongClick: () -> Unit,
    onDoubleClick: () -> Unit
) {
    Card(
        elevation = 2.dp,
        modifier = modifier
            .combinedClickable(
                onClick = {
                    navController?.navigate(Screen.FtPGameDetails.route + "/${game.id}")
                },
                onLongClick = {
                    /* TODO copy game url */
                    onLongClick
                },
                onDoubleClick = {
                    /* TODO open game url in browser */
                    onDoubleClick
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp)
        ) {
            BoxWithImage(
                contentDescription = game.title,
                icon = game.thumbnail,
                boxModifier = Modifier
                    .size(128.dp)
                    .clip(RoundedCornerShape(2.dp)),
                imageModifier = Modifier.aspectRatio(1f),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(16.dp))
            SelectionContainer {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = game.title,
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.h6
                    )
                    Text(
                        text = game.short_description,
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GamePreview() {
    val game = Game(
        id = 540,
        title = "Overwatch 2",
        thumbnail = "https://www.freetogame.com/g/540/thumbnail.jpg",
        short_description = "A hero-focused first-person team shooter from Blizzard Entertainment.",
        game_url = "https://www.freetogame.com/open/overwatch-2",
        genre = "Shooter",
        platform = "PC (Windows)",
        publisher = "Activision Blizzard",
        developer = "Blizzard Entertainment",
        release_date = "2022-10-04",
        freetogame_profile_url = "https://www.freetogame.com/overwatch-2"
    )
    FtPGamesTheme {
        GameListItem(
            game = game,
            navController = null,
            onLongClick = { },
            onDoubleClick = { }
        )
    }
}

//@Preview("Light Theme")
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun GamesPreview() {
//    FtPGamesTheme() {
//        val mockData = List(10) {
//            Game(
//                id = it.toLong(),
//                title = "$it Overwatch 2",
//                thumbnail = "$it https://www.freetogame.com/g/540/thumbnail.jpg",
//                short_description = "$it A hero-focused first-person team shooter from Blizzard Entertainment.",
//                game_url = "$it https://www.freetogame.com/open/overwatch-2",
//                genre = "$it Shooter",
//                platform = "$it PC (Windows)",
//                publisher = "$it Activision Blizzard",
//                developer = "$it Blizzard Entertainment",
//                release_date = "$it 2022-10-04",
//                freetogame_profile_url = "$it https://www.freetogame.com/overwatch-2"
//            )
//        }
//        Surface(
//            color = MaterialTheme.colors.background
//        ) {
//            val gamesViewModel: GamesViewModel = viewModel(factory = GamesViewModel.Factory)
//            GamesList(gamesViewModel = gamesViewModel, navController = null)
//        }
//    }
//}