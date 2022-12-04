package com.theopechli.ftpgames.data

import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.model.GameDetails
import com.theopechli.ftpgames.network.FtPGamesApiService

interface FtPGamesRepository {
    suspend fun getGames(): List<Game>
    suspend fun getGameById(id: Long): GameDetails
}

class DefaultFtPGamesRepository(private val ftpGamesApiService: FtPGamesApiService) :
    FtPGamesRepository {

    override suspend fun getGames(): List<Game> {
        return ftpGamesApiService.getGames()
    }

    override suspend fun getGameById(id: Long): GameDetails {
        return ftpGamesApiService.getGameById(id)
    }
}