package com.theopechli.ftpgames.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class SystemInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val os: String,
    val processor: String,
    val memory: String,
    val graphics: String,
    val storage: String
)