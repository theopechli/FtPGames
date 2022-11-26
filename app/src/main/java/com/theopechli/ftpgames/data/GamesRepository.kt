package com.theopechli.ftpgames.data

import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.network.GamesApiService

interface GamesRepository {
    suspend fun getGames(): List<Game>
}

class DefaultGamesRepository(private val gamesApiService: GamesApiService) : GamesRepository {
    override suspend fun getGames(): List<Game> {
        return gamesApiService.getGames()
    }
}