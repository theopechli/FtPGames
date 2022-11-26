package com.theopechli.ftpgames.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    suspend fun getAll(): List<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg games: Game)
}