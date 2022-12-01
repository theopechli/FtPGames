package com.theopechli.ftpgames.data

import android.content.Context
import com.theopechli.ftpgames.network.FtPGamesApiService
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Thread.sleep

interface AppContainer {
    val ftpGamesRepository: FtPGamesRepository
    val gameDatabase: GameDatabase
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val BASE_URL = "https://www.freetogame.com/api/"

    private val dispatcher: Dispatcher = Dispatcher().apply {
        maxRequests = 4
        maxRequestsPerHost = 4
    }

    private val okHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .addInterceptor(OkHttpInterceptor())

    @ExperimentalSerializationApi
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient.build())
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

    class OkHttpInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            sleep(500)
            return chain.proceed(chain.request())
        }
    }
}
