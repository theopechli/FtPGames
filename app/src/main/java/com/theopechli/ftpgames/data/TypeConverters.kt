package com.theopechli.ftpgames.data

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.theopechli.ftpgames.model.Screenshot
import com.theopechli.ftpgames.model.SystemInfo

object TypeConverters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val systemInfoAdapter by lazy {
        return@lazy moshi.adapter(SystemInfo::class.java)
    }

    private val screenshotListAdapter by lazy {
        val screenshotList = Types.newParameterizedType(List::class.java, Screenshot::class.java)
        return@lazy moshi.adapter<List<Screenshot>>(screenshotList)
    }

    @TypeConverter
    @JvmStatic
    fun fromSystemInfo(systemInfo: SystemInfo): String {
        return systemInfoAdapter.toJson(systemInfo)
    }

    @TypeConverter
    @JvmStatic
    fun toSystemInfo(systemInfo: String): SystemInfo? {
        return systemInfoAdapter.fromJson(systemInfo)
    }

    @TypeConverter
    @JvmStatic
    fun fromScreenshotList(screenshots: List<Screenshot>): String {
        return screenshotListAdapter.toJson(screenshots)
    }

    @TypeConverter
    @JvmStatic
    fun toScreenshotList(screenshots: String): List<Screenshot>? {
        return screenshotListAdapter.fromJson(screenshots)
    }
}