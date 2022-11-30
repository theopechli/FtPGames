package com.theopechli.ftpgames.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDetailsDao {
    @Query("SELECT * FROM gameDetails WHERE id = :id LIMIT 1")
    suspend fun getGameDetails(id: Long): GameDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameDetails(gameDetails: GameDetails)
}