package com.theopechli.ftpgames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.theopechli.ftpgames.ui.FtPGamesApp
import com.theopechli.ftpgames.ui.theme.FtPGamesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FtPGamesTheme {
                this.window.statusBarColor = Color.Black.toArgb()
                FtPGamesApp()
            }
        }
    }
}