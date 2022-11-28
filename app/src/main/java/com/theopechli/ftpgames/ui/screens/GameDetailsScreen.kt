package com.theopechli.ftpgames.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.theopechli.ftpgames.R
import com.theopechli.ftpgames.model.GameDetails

@Composable
fun GameDetailsScreen(
    gameDetailsUiState: GameDetailsUiState,
    modifier: Modifier = Modifier
) {
    when (gameDetailsUiState) {
        is GameDetailsUiState.Loading -> LoadingScreen(modifier)
        is GameDetailsUiState.Success -> GameDetails(gameDetailsUiState.game, modifier)
        else -> ErrorScreen(modifier)
    }
}

@Composable
fun GameDetails(
    game: GameDetails,
    modifier: Modifier = Modifier
) {
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
                    value = game.release_date
                )
                Text(
                    text = game.short_description,
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

//@Preview("Light Theme")
//@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun GameDetailsPreview() {
//    val game = Game(
//        id = 540,
//        title = "Overwatch 2",
//        thumbnail = "https://www.freetogame.com/g/540/thumbnail.jpg",
//        shortDescription = "A hero-focused first-person team shooter from Blizzard Entertainment.",
//        gameUrl = "https://www.freetogame.com/open/overwatch-2",
//        genre = "Shooter",
//        platform = "PC (Windows)",
//        publisher = "Activision Blizzard",
//        developer = "Blizzard Entertainment",
//        releaseDate = "2022-10-04",
//        profileUrl = "https://www.freetogame.com/overwatch-2"
//    )
//    FtPGamesTheme {
//        GameDetails(game = game)
//    }
//}