package com.theopechli.ftpgames.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Screenshot(
    @PrimaryKey val id: Long,
    val image: String
)