package com.theopechli.ftpgames.network

import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.model.GameDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface FtPGamesApiService {
    @GET("games")
    suspend fun getGames(): List<Game>

    @GET("game")
    suspend fun getGameById(@Query("id") id: Long): GameDetails
}