package com.rksrtx76.cinemax.main.data.local.genres

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [GenreEntity::class], version = 1)
abstract class GenresDataBase : RoomDatabase() {
    abstract val genreDao: GenreDao
}
