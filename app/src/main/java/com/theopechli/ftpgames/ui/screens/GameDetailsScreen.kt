package com.theopechli.ftpgames.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.theopechli.ftpgames.R
import com.theopechli.ftpgames.model.GameDetails
import com.theopechli.ftpgames.model.Screenshot
import com.theopechli.ftpgames.model.SystemInfo
import com.theopechli.ftpgames.ui.theme.FtPGamesTheme

@Composable
fun GameDetailsScreen(
    gameDetailsViewModel: GameDetailsViewModel,
    modifier: Modifier = Modifier
) {
    when (gameDetailsViewModel.gameDetailsUiState) {
        is GameDetailsUiState.Loading -> LoadingScreen(modifier)
        is GameDetailsUiState.Success -> GameDetailsColumn(gameDetailsViewModel)
        else -> ErrorScreen(modifier)
    }
}

@Composable
fun BoxWithImage(
    contentDescription: String,
    icon: String,
    boxModifier: Modifier,
    imageModifier: Modifier,
    contentScale: ContentScale
) {
    Box(
        modifier = boxModifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(icon)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_launcher_foreground),
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.icon, contentDescription),
            contentScale = contentScale,
            modifier = imageModifier
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameDetailsColumn(
    gameDetailsViewModel: GameDetailsViewModel
) {
    val gameDetails =
        remember {
            mutableStateOf(
                (gameDetailsViewModel.gameDetailsUiState as GameDetailsUiState.Success)
                    .gameDetails
            )
        }

    val refreshing by gameDetailsViewModel.refreshing.collectAsState()
    val refreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            gameDetailsViewModel.refresh(gameDetails.value.id)
        }
    )

    Box(Modifier.pullRefresh(refreshState)) {
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .pullRefresh(refreshState)
            ) {
                BoxWithImage(
                    gameDetails.value.title,
                    gameDetails.value.thumbnail,
                    boxModifier = Modifier
                        .clip(RoundedCornerShape(4.dp)),
                    imageModifier = Modifier
                        .aspectRatio(16f / 9f)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = gameDetails.value.title,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)

                )
                /* TODO onClick visit all games by the developer */
                RowWithTwoTexts(
                    key = "Developer",
                    value = gameDetails.value.developer,
                    color = MaterialTheme.colors.secondary
                )
                RowWithTwoTexts(
                    key = "Publisher",
                    value = gameDetails.value.publisher,
                    color = MaterialTheme.colors.secondary
                )
                RowWithTwoTexts(
                    key = "Released",
                    value = gameDetails.value.release_date,
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = gameDetails.value.short_description,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                GenreText(genre = gameDetails.value.genre)
                Spacer(modifier = Modifier.height(32.dp))
                DescriptionText(description = gameDetails.value.description)
            }
        }
        PullRefreshIndicator(refreshing, refreshState, Modifier.align(Alignment.TopCenter))
    }
}

@Composable
fun RowWithTwoTexts(
    key: String,
    value: String,
    color: Color
) {
    Row {
        Text(
            text = key,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primaryVariant,
            textAlign = TextAlign.Start,
            modifier = Modifier.width(128.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.h5,
            color = color,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun GenreText(genre: String) {
    Text(
        text = stringResource(id = R.string.genre),
        color = MaterialTheme.colors.primaryVariant,
        style = MaterialTheme.typography.h5
    )
    Text(
        text = genre,
        color = MaterialTheme.colors.onPrimary,
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .padding(top = 8.dp)
            .background(MaterialTheme.colors.primary)
    )
}

@Composable
fun DescriptionText(description: String) {
    Text(
        text = stringResource(id = R.string.game_description),
        color = MaterialTheme.colors.secondary,
        style = MaterialTheme.typography.h5
    )
    Text(
        text = description,
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BoxWithImagePreview() {
    FtPGamesTheme {
        BoxWithImage(
            contentDescription = "Call Of Duty: Warzone",
            icon = "https://www.freetogame.com/g/452/thumbnail.jpg",
            boxModifier = Modifier
                .clip(RoundedCornerShape(4.dp)),
            imageModifier = Modifier
                .aspectRatio(16f / 9f)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RowWithTwoTextsPreview() {
    FtPGamesTheme {
        RowWithTwoTexts(
            key = "Publisher",
            value = "Activision",
            color = MaterialTheme.colors.primary
        )
    }
}

@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GameDetailsPreview() {
    val gameDetails = GameDetails(
        id = 452,
        title = "Call Of Duty: Warzone",
        thumbnail = "https://www.freetogame.com/g/452/thumbnail.jpg",
        status = "Live",
        short_description = "A standalone free-to-play battle royale and modes accessible via Call of Duty: Modern Warfare.",
        description = "Call of Duty: Warzone is both a standalone free-to-play battle royale and modes accessible via Call of Duty: Modern Warfare. Warzone features two modes — the general 150-player battle royle, and “Plunder”. The latter mode is described as a “race to deposit the most Cash”. In both modes players can both earn and loot cash to be used when purchasing in-match equipment, field upgrades, and more. Both cash and XP are earned in a variety of ways, including completing contracts.\r\n\r\nAn interesting feature of the game is one that allows players who have been killed in a match to rejoin it by winning a 1v1 match against other felled players in the Gulag.\r\n\r\nOf course, being a battle royale, the game does offer a battle pass. The pass offers players new weapons, playable characters, Call of Duty points, blueprints, and more. Players can also earn plenty of new items by completing objectives offered with the pass.",
        game_url = "https://www.freetogame.com/open/call-of-duty-warzone",
        genre = "Shooter",
        platform = "Windows",
        publisher = "Activision",
        developer = "Infinity Ward",
        release_date = "2020-03-10",
        freetogame_profile_url = "https://www.freetogame.com/call-of-duty-warzone",
        minimum_system_requirements = SystemInfo(
            id = 1,
            os = "Windows 7 64-Bit (SP1) or Windows 10 64-Bit",
            processor = "Intel Core i3-4340 or AMD FX-6300",
            memory = "8GB RAM",
            graphics = "NVIDIA GeForce GTX 670 / GeForce GTX 1650 or Radeon HD 7950",
            storage = "175GB HD space"
        ),
        screenshots = listOf(
            Screenshot(
                id = 1124,
                image = "https://www.freetogame.com/g/452/Call-of-Duty-Warzone-1.jpg"
            ),
            Screenshot(
                id = 1125,
                image = "https://www.freetogame.com/g/452/Call-of-Duty-Warzone-2.jpg"
            ),
            Screenshot(
                id = 1126,
                image = "https://www.freetogame.com/g/452/Call-of-Duty-Warzone-3.jpg"
            ),
            Screenshot(
                id = 1127,
                image = "https://www.freetogame.com/g/452/Call-of-Duty-Warzone-4.jpg"
            )
        )
    )
    FtPGamesTheme {
        val gameDetailsViewModel: GameDetailsViewModel =
            viewModel(factory = gameDetails.id.let { it1 -> GameDetailsViewModel.Factory(it1) })
        GameDetailsScreen(gameDetailsViewModel = gameDetailsViewModel)
    }
}