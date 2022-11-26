package com.theopechli.ftpgames.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val id: Int,
    val title: String,
    val thumbnail: String,
    @SerialName("short_description") val shortDescription: String,
    @SerialName("game_url") val gameUrl: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("freetogame_profile_url") val profileUrl: String
)
