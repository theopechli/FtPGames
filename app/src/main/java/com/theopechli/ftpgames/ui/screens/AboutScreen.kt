package com.theopechli.ftpgames.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.theopechli.ftpgames.BuildConfig
import com.theopechli.ftpgames.R
import com.theopechli.ftpgames.ui.theme.FtPGamesTheme

@Composable
fun AboutScreen() {
    Column {
        InfoScreen()
        RowWithButtons()
        CreditsScreen()
    }
}

@Composable
fun RowWithButtons() {
    val uriHandler = LocalUriHandler.current
    val sourceCodeUrl = stringResource(R.string.source_code_url)
    val bugReportUrl = stringResource(R.string.bug_report_url)

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Button(
            onClick = {
                uriHandler.openUri(sourceCodeUrl)
            }
        ) {
            ColumnWithImageAndText(
                painterResource(R.drawable.ic_baseline_code_24),
                stringResource(R.string.code_icon),
                stringResource(R.string.source_code)
            )
        }
        Button(
            onClick = {
                uriHandler.openUri(bugReportUrl)
            }
        ) {
            ColumnWithImageAndText(
                painterResource(R.drawable.ic_baseline_bug_report_24),
                stringResource(R.string.bug_report_icon),
                stringResource(R.string.bug_report)
            )
        }
    }
}

@Composable
fun ColumnWithImageAndText(image: Painter, contentDescription: String, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = image,
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Text(
            text = text,
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
fun InfoScreen() {
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BoxWithImage(
                        contentDescription = stringResource(R.string.app_name),
                        icon = R.drawable.ic_launcher_foreground.toString(),
                        boxModifier = Modifier.size(100.dp),
                        imageModifier = Modifier.aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = BuildConfig.VERSION_NAME,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.app_description),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.copyright),
                    style = MaterialTheme.typography.body2,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}

@Composable
fun CreditsScreen() {
    val uriHandler = LocalUriHandler.current
    val websiteUrl = stringResource(R.string.website_url)

    Card(
        elevation = 2.dp,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.credits),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.free_to_game),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.free_to_game_credits),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        uriHandler.openUri(websiteUrl)
                    }
                ) {
                    ColumnWithImageAndText(
                        painterResource(R.drawable.ic_baseline_web_24),
                        stringResource(R.string.website_icon),
                        stringResource(R.string.website)
                    )
                }
            }
        }
    }
}

@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AboutPreview() {
    FtPGamesTheme {
        AboutScreen()
    }
}