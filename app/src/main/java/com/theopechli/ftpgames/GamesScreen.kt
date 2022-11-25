package com.theopechli.ftpgames

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.model.GamesRepository
import com.theopechli.ftpgames.ui.theme.FtPGamesTheme

@Composable
fun GamesList(
    games: List<Game>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        itemsIndexed(games) { _, game ->
            GameListItem(
                game = game,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun GameListItem(
    game: Game,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 2.dp,
        modifier = modifier
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
                Image(
                    painter = painterResource(id = game.thumbnail),
                    contentDescription = "Game thumbnail",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .aspectRatio(1f)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = game.title),
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = stringResource(id = game.short_description),
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
        title = R.string.title,
        short_description = R.string.short_description,
        thumbnail = R.drawable.thumbnail
    )
    FtPGamesTheme {
        GameListItem(game = game)
    }
}

@Preview("Games List")
@Composable
fun GamesPreview() {
    FtPGamesTheme(darkTheme = false) {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            GamesList(games = GamesRepository.games)
        }
    }
}