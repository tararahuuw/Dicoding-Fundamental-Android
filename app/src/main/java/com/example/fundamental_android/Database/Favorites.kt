package com.example.fundamental_android.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorites(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val username: String,
    val urlAvatar: String
)