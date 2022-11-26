package com.theopechli.ftpgames.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.model.GameDao
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Game::class], version = 1)
abstract class GameRoomDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: GameRoomDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context) : GameRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance: GameRoomDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    GameRoomDatabase::class.java,
                    "game_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}