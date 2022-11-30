package com.theopechli.ftpgames.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.model.GameDao
import com.theopechli.ftpgames.model.GameDetails
import com.theopechli.ftpgames.model.GameDetailsDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [(Game::class), (GameDetails::class)], version = 1, exportSchema = false)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    abstract fun gameDetailsDao(): GameDetailsDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance: GameDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}