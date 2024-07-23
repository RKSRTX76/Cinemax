package com.rksrtx76.cinemax.main.domain.repository

import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.util.Resource
import kotlinx.coroutines.flow.Flow

interface MediaRepository {


    suspend fun updateItem(media : Media)

    suspend fun insertItem(media: Media)

    suspend fun getItem(
        id : Int,
        type : String,
        category: String
    ): Media


    suspend fun getMoviesAndTvSeriesList(
        // when we reopen app data should display from database not api
//        forceFetchFromRemote : Boolean,
        fetchFromRemote : Boolean,
        isRefresh : Boolean,
        type : String,
        category : String,
        page : Int,
        apiKey : String
    ): Flow<Resource<List<Media>>>

    suspend fun getTrendingList(
        fetchFromRemote : Boolean,
        isRefresh : Boolean,
        type : String,
        time : String,
        page : Int,
        apiKey : String
    ): Flow<Resource<List<Media>>>

    suspend fun getUpcomingMoviesList(
        fetchFromRemote : Boolean,
        isRefresh : Boolean,
        page : Int,
        apiKey : String
    ): Flow<Resource<List<Media>>>

    suspend fun getAiringTodayTvSeriesList(
        fetchFromRemote : Boolean,
        isRefresh : Boolean,
        page : Int,
        apiKey : String
    ): Flow<Resource<List<Media>>>

    suspend fun getNowPlayingMoviesList(
        fetchFromRemote : Boolean,
        isRefresh : Boolean,
        page : Int,
        apiKey : String
    ) : Flow<Resource<List<Media>>>

    suspend fun bookmarkMedia(id: Int, isBookmarked: Boolean)

    suspend fun getBookmarkedMedia(): List<Media>


    suspend fun updateBookmarkedMedia(media: Media)
}