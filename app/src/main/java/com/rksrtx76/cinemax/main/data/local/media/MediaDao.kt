package com.rksrtx76.cinemax.main.data.local.media

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaList(
        mediaEntities : List<MediaEntity>
    )


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaItem(
        mediaItem : MediaEntity
    )

    @Update
    suspend fun updateMediaItem(
        mediaItem: MediaEntity
    )

    @Query("DELETE FROM MediaEntity WHERE mediaType = :mediaType AND category = :category")
    suspend fun deleteMediaByTypeAndCategory(mediaType: String, category: String)


    @Query("SELECT * FROM MediaEntity WHERE id = :id")
    suspend fun getMediaById(id : Int) : MediaEntity

    @Query("SELECT * FROM MediaEntity WHERE  mediaType = :mediaType AND category = :category")
    suspend fun getMediaListByTypeAndCategory(mediaType: String, category: String): List<MediaEntity>


    @Query("DELETE FROM MediaEntity WHERE category = :category")
    suspend fun deleteTrendingMediaList(category: String)

    @Query("SELECT * FROM MediaEntity WHERE category = :category")
    suspend fun getTrendingMediaList(category: String): List<MediaEntity>

    @Query("DELETE FROM MediaEntity WHERE mediaType = :mediaType AND category = :category")
    suspend fun deleteUpcomingMovies(mediaType: String, category: String = "upcoming")

    @Query("SELECT * FROM MediaEntity WHERE mediaType = :mediaType AND category = :category")
    suspend fun getUpcomingMovies(mediaType: String, category: String = "upcoming"): List<MediaEntity>

    // Queries for upcoming TV series
    @Query("DELETE FROM MediaEntity WHERE mediaType = :mediaType AND category = :category")
    suspend fun deleteAiringTodayTvSeries(mediaType: String, category: String = "airing_today")

    @Query("SELECT * FROM MediaEntity WHERE mediaType = :mediaType AND category = :category")
    suspend fun getAiringTodayTvSeries(mediaType: String, category: String = "airing_today"): List<MediaEntity>

    // Queries for now playing movies
    @Query("DELETE FROM MediaEntity WHERE mediaType = :mediaType AND category = :category")
    suspend fun deleteNowPlayingMovies(mediaType: String, category: String = "now_playing")

    @Query("SELECT * FROM MediaEntity WHERE mediaType = :mediaType AND category = :category")
    suspend fun getNowPlayingMovies(mediaType: String, category: String = "now_playing"): List<MediaEntity>


    @Query("UPDATE MediaEntity SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmarkStatus(id: Int, isBookmarked: Boolean)

    @Query("SELECT * FROM MediaEntity WHERE isBookmarked = 1")
    suspend fun getBookmarkedMedia(): List<MediaEntity>

}