package com.theopechli.ftpgames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.theopechli.ftpgames.ui.GamesApp
import com.theopechli.ftpgames.ui.theme.FtPGamesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FtPGamesTheme {
                GamesApp()
            }
        }
    }
}