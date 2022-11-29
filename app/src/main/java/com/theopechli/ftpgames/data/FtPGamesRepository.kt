package com.theopechli.ftpgames.data

import android.util.Log
import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.model.GameDetails
import com.theopechli.ftpgames.network.FtPGamesApiService

interface FtPGamesRepository {
    suspend fun getGames(): List<Game>

    suspend fun getGameById(id: Long): GameDetails
}

class DefaultFtPGamesRepository(private val ftpGamesApiService: FtPGamesApiService) : FtPGamesRepository {
    override suspend fun getGames(): List<Game> {
        Log.i("GAME_LIST", "Games")
        return ftpGamesApiService.getGames()
    }

    override suspend fun getGameById(id: Long): GameDetails {
        Log.i("GAME_DETAILS", "Game id: $id")
        return ftpGamesApiService.getGameById(id)
    }
}