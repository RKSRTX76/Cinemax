package com.rksrtx76.cinemax.main.data.local.media

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rksrtx76.cinemax.main.data.local.genres.GenreEntity


@Database(entities = [MediaEntity::class], version = 1)
abstract class MediaDataBase: RoomDatabase() {
    abstract val mediaDao : MediaDao
}