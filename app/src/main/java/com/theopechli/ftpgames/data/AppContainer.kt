package com.theopechli.ftpgames.data

import android.content.Context
import com.theopechli.ftpgames.network.FtPGamesApiService
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val ftpGamesRepository: FtPGamesRepository
    val gameDatabase: GameDatabase
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val BASE_URL = "https://www.freetogame.com/api/"

    @ExperimentalSerializationApi
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: FtPGamesApiService by lazy {
        retrofit.create(FtPGamesApiService::class.java)
    }

    override val ftpGamesRepository: FtPGamesRepository by lazy {
        DefaultFtPGamesRepository(retrofitService)
    }

    override val gameDatabase: GameDatabase by lazy {
        GameDatabase.getDatabase(context)
    }
}
