package com.example.fundamental_android.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorites (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0,

    @ColumnInfo(name = "name")
    var name : String = "",

    @ColumnInfo(name = "username")
    var username : String = "",

    @ColumnInfo(name = "location")
    var location : String = "",

    @ColumnInfo(name = "repository")
    var repository : String = "",

    @ColumnInfo(name = "company")
    var company : String = "",

    @ColumnInfo(name = "followers")
    var followers : String = "",

    @ColumnInfo(name = "following")
    var following : String = "",

    @ColumnInfo(name = "photo")
    var photo : String = ""
)