package com.rksrtx76.cinemax.main.data.local.genres

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(mediaEntities : List<GenreEntity>)

    @Query("SELECT * FROM GenreEntity WHERE type = :mediaType")
    suspend fun getGenres(mediaType : String) : List<GenreEntity>

}