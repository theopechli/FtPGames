package com.theopechli.ftpgames.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Game(
    @PrimaryKey
    val id: Long,
    val title: String,
    val thumbnail: String,
//    @ColumnInfo(name = "short_description")
//    @SerialName("short_description")
//    val shortDescription: String,
    val short_description: String,
//    @ColumnInfo(name = "game_url")
//    @SerialName("game_url")
//    val gameUrl: String,
    val game_url: String,
    val genre: String,
    val platform: String,
    val publisher: String,
    val developer: String,
//    @ColumnInfo(name = "release_date")
//    @SerialName("release_date")
//    val releaseDate: String,
    val release_date: String,
//    @ColumnInfo(name = "freetogame_profile_url")
//    @SerialName("freetogame_profile_url")
//    val profileUrl: String
    val freetogame_profile_url: String
)
