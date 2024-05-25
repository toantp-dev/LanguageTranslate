package com.navercorp.android.languagetranslate.datasource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM Favorite")
    fun getAll(): Flow<List<Favorite>>

    @Insert
    suspend fun insertAll(vararg users: Favorite)

    @Delete
    suspend fun delete(user: Favorite)
}
