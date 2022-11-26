package com.theopechli.ftpgames.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.theopechli.ftpgames.network.GamesApiService
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val gamesRepository: GamesRepository
}

class DefaultAppContainer : AppContainer {
    private val BASE_URL = "https://www.freetogame.com/api/"

    @ExperimentalSerializationApi
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: GamesApiService by lazy {
        retrofit.create(GamesApiService::class.java)
    }

    override val gamesRepository: GamesRepository by lazy {
        DefaultGamesRepository(retrofitService)
    }
}
