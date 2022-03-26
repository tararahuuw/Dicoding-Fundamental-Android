package com.example.fundamental_android.Database

import androidx.room.*

@Dao
interface FavoritesDao {
    @Insert
    suspend fun addFavorites(favorites: Favorites)

    @Query("SELECT * FROM favorites ORDER BY id DESC")
    suspend fun getFavorites() : List<Favorites>

    @Query("SELECT * FROM favorites WHERE username LIKE :search")
    fun findUserWithName(search: String): List<Favorites>

    @Query("DELETE FROM favorites WHERE username = :usernameInput")
    fun deleteByUsername(usernameInput: String)
}