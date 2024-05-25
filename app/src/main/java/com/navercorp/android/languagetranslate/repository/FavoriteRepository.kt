package com.navercorp.android.languagetranslate.repository

import com.navercorp.android.languagetranslate.GlobalDatabase
import com.navercorp.android.languagetranslate.datasource.AppDatabase
import com.navercorp.android.languagetranslate.datasource.Favorite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface FavoriteRepository {

    fun getAllFavorites() : Flow<List<Favorite>>

    suspend fun addFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)
}

class FavoriteRepositoryImpl(
    private val database: AppDatabase = GlobalDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FavoriteRepository {

    private val favoriteDao = database.favoriteDao()

    override fun getAllFavorites() : Flow<List<Favorite>> = favoriteDao.getAll()

    override suspend fun addFavorite(favorite: Favorite) = withContext(dispatcher) {
        favoriteDao.insertAll(favorite)
    }

    override suspend fun deleteFavorite(favorite: Favorite) = withContext(dispatcher) {
        favoriteDao.delete(favorite)
    }

}