package com.theopechli.ftpgames.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.model.GameDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Game::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context) : GameDatabase {
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