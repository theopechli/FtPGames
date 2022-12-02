package com.theopechli.ftpgames

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import com.theopechli.ftpgames.ui.FtPGamesApp
import com.theopechli.ftpgames.ui.theme.FtPGamesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FtPGamesTheme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.apply {
                        setSystemBarsAppearance(
                            if (isSystemInDarkTheme()) 0 else APPEARANCE_LIGHT_STATUS_BARS,
                            APPEARANCE_LIGHT_STATUS_BARS
                        )
                    }
                } else {
                    @Suppress("DEPRECATION")
                    window.decorView.systemUiVisibility =
                        (if (isSystemInDarkTheme()) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
                this.window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                this.window.navigationBarColor = MaterialTheme.colors.primary.toArgb()
                FtPGamesApp()
            }
        }
    }
}