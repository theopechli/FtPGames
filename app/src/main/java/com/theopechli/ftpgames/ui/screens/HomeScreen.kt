package com.theopechli.ftpgames.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.theopechli.ftpgames.R
import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.ui.theme.FtPGamesTheme

@Composable
fun HomeScreen(
    gamesUiState: GamesUiState,
    modifier: Modifier = Modifier
) {
    when (gamesUiState) {
        is GamesUiState.Loading -> LoadingScreen(modifier)
        is GamesUiState.Success -> GamesList(gamesUiState.games, modifier)
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

@Composable
fun GamesList(
    games: List<Game>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(
            items = games,
            key = { game ->
                game.title
                game.shortDescription
                game.thumbnail
            }
        ) { game ->
            GameListItem(
                game = game,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                { Unit },
                { Unit },
                { Unit }
            )
        }
    }
}

@Composable
fun GameDetailsScreen(game: Game) {
    Card(
        elevation = 2.dp,
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(game.thumbnail)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "${game.title} thumbnail",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)

                )
                /* TODO onClick visit all games by the developer */
                RowWithTwoTexts(
                    key = "Developer",
                    value = game.developer
                )
                RowWithTwoTexts(
                    key = "Publisher",
                    value = game.publisher
                )
                RowWithTwoTexts(
                    key = "Released",
                    value = game.releaseDate
                )
                Text(
                    text = game.shortDescription,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun RowWithTwoTexts(
    key: String,
    value: String,
) {
    Column {
        Row {
            Text(
                text = key,
                style = MaterialTheme.typography.h5,
                color = Color.Gray,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.secondary,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameListItem(
    game: Game,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onDoubleClick: () -> Unit
) {
    Card(
        elevation = 2.dp,
        modifier = modifier
            .combinedClickable(
                onClick = {
                    /* TODO show game details */
                    onClick
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
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .clip(RoundedCornerShape(2.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(game.thumbnail)
                        .crossfade(true)
                        .build(),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "${game.title} thumbnail",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = game.title,
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = game.shortDescription,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 8.dp)
                )
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
        shortDescription = "A hero-focused first-person team shooter from Blizzard Entertainment.",
        gameUrl = "https://www.freetogame.com/open/overwatch-2",
        genre = "Shooter",
        platform = "PC (Windows)",
        publisher = "Activision Blizzard",
        developer = "Blizzard Entertainment",
        releaseDate = "2022-10-04",
        profileUrl = "https://www.freetogame.com/overwatch-2"
    )
    FtPGamesTheme {
        GameListItem(
            game = game,
            onClick = { Unit },
            onLongClick = { Unit },
            onDoubleClick = { Unit }
        )
    }
}

@Preview("Games List")
@Composable
fun GamesPreview() {
    FtPGamesTheme(darkTheme = false) {
        val mockData = List(10) {
            Game(
                id = it,
                title = "$it Overwatch 2",
                thumbnail = "$it https://www.freetogame.com/g/540/thumbnail.jpg",
                shortDescription = "$it A hero-focused first-person team shooter from Blizzard Entertainment.",
                gameUrl = "$it https://www.freetogame.com/open/overwatch-2",
                genre = "$it Shooter",
                platform = "$it PC (Windows)",
                publisher = "$it Activision Blizzard",
                developer = "$it Blizzard Entertainment",
                releaseDate = "$it 2022-10-04",
                profileUrl = "$it https://www.freetogame.com/overwatch-2"
            )
        }
        Surface(
            color = MaterialTheme.colors.background
        ) {
            GamesList(games = mockData)
        }
    }
}

@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GameDetailsPreview() {
    val game = Game(
        id = 540,
        title = "Overwatch 2",
        thumbnail = "https://www.freetogame.com/g/540/thumbnail.jpg",
        shortDescription = "A hero-focused first-person team shooter from Blizzard Entertainment.",
        gameUrl = "https://www.freetogame.com/open/overwatch-2",
        genre = "Shooter",
        platform = "PC (Windows)",
        publisher = "Activision Blizzard",
        developer = "Blizzard Entertainment",
        releaseDate = "2022-10-04",
        profileUrl = "https://www.freetogame.com/overwatch-2"
    )
    FtPGamesTheme {
        GameDetailsScreen(game = game)
    }
}