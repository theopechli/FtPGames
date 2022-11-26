package com.theopechli.ftpgames.network

import com.theopechli.ftpgames.model.Game
import retrofit2.http.GET

interface GamesApiService {
    @GET("games")
    suspend fun getGames(): List<Game>
}