package com.theopechli.ftpgames.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Game(
    @StringRes val title: Int,
    @StringRes val short_description: Int,
    @DrawableRes val thumbnail: Int
)
