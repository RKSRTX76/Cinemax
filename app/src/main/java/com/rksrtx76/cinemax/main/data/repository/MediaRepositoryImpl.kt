package com.rksrtx76.cinemax.main.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.rksrtx76.cinemax.main.data.local.media.MediaDataBase
import com.rksrtx76.cinemax.main.data.mappers.toMedia
import com.rksrtx76.cinemax.main.data.mappers.toMediaEntity
import com.rksrtx76.cinemax.main.data.remote.api.MediaApiService
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.main.domain.repository.MediaRepository
import com.rksrtx76.cinemax.util.Constants
import com.rksrtx76.cinemax.util.Constants.TRENDING
import com.rksrtx76.cinemax.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MediaRepositoryImpl @Inject constructor(
    private val mediaApiService: MediaApiService,
    mediaDb : MediaDataBase
): MediaRepository {

    private val mediaDao = mediaDb.mediaDao


    override suspend fun bookmarkMedia(id: Int, isBookmarked: Boolean) {
        mediaDao.updateBookmarkStatus(id, isBookmarked)
        getBookmarkedMedia()
    }

    override suspend fun getBookmarkedMedia(): List<Media> {
        return mediaDao.getBookmarkedMedia().map { it.toMedia(it.mediaType, it.category) }
    }

    override suspend fun updateBookmarkedMedia(media: Media) {
        mediaDao.updateBookmarkStatus(media.id, media.isBookmarked)
    }

    override suspend fun insertItem(media: Media) {
        val mediaEntity = media.toMediaEntity()

        mediaDao.insertMediaItem(
            mediaItem = mediaEntity
        )
    }

    override suspend fun getItem(
        id: Int,
        type: String,
        category: String
    ): Media {
        return mediaDao.getMediaById(id).toMedia(
            category = category,
            type = type
        )
    }

    override suspend fun updateItem(media: Media) {
        val mediaEntity = media.toMediaEntity()

        mediaDao.updateMediaItem(
            mediaItem = mediaEntity
        )
    }

    override suspend fun getMoviesAndTvSeriesList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        category: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMediaList = mediaDao.getMediaListByTypeAndCategory(type, category)

            val shouldLoadLocal = localMediaList.isNotEmpty() && !fetchFromRemote && !isRefresh
            if (shouldLoadLocal) {
                emit(
                    Resource.Success(
                        data = localMediaList.map { mediaEntity ->
                            mediaEntity.toMedia(type, category)
                        }
                    ))
                emit(Resource.Loading(false))
                return@flow
            }

            var searchPage = page
            if (isRefresh) {
                mediaDao.deleteMediaByTypeAndCategory(type, category)
                searchPage = 1
            }

            val remoteMediaList = try {
                mediaApiService.getMoviesAndTvSeriesList(
                    type, category, searchPage, apiKey
                ).results
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            }

            remoteMediaList.let { mediaList ->
                val media = mediaList.map {
                    it.toMedia(
                        type = type,
                        category = category
                    )
                }

                val entities = mediaList.map {
                    it.toMediaEntity(
                        type = type,
                        category = category
                    )
                }

                mediaDao.insertMediaList(entities)

                emit(
                    Resource.Success(data = media)
                )

                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getTrendingList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        time: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMediaList = mediaDao.getTrendingMediaList(TRENDING)

            val shouldLoadLocal = localMediaList.isNotEmpty() && !fetchFromRemote

            if (shouldLoadLocal) {
                emit(
                    Resource.Success(
                        data = localMediaList.map { mediaEntity ->
                            mediaEntity.toMedia(
                                type = mediaEntity.mediaType ?: Constants.unavailable,
                                category = TRENDING
                            )
                        }
                    ))
                emit(Resource.Loading(false))
                return@flow
            }

            var searchPage = page
            if (isRefresh) {
                mediaDao.deleteTrendingMediaList(TRENDING)
                searchPage = 1
            }

            val remoteMediaList = try {
                mediaApiService.getTrendingList(
                    type, time, searchPage, apiKey
                ).results
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            }

            remoteMediaList.let { mediaList ->
                val media = mediaList.map {
                    it.toMedia(
                        type = it.media_type ?: Constants.unavailable,
                        category = TRENDING
                    )
                }

                val entities = mediaList.map {
                    it.toMediaEntity(
                        type = it.media_type ?: Constants.unavailable,
                        category = TRENDING
                    )
                }

                mediaDao.insertMediaList(entities)

                emit(
                    Resource.Success(data = media)
                )

                emit(Resource.Loading(false))
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getUpcomingMoviesList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(true))

            var searchPage = page
            if (isRefresh) {
                mediaDao.deleteUpcomingMovies(mediaType = "movie", category = Constants.UPCOMING)
                searchPage = 1
            }

            try {
                val remoteMediaList = mediaApiService.getUpcomingMoviesList(searchPage, apiKey).results
                remoteMediaList.forEach{
                    Log.d("UpcomingMoviesList", "Movie: ${it.title}, Release Date: ${it.release_date}")
                }


                val currentDate = LocalDate.now()

                // Filter out movies with past release dates
                val upcomingMovies = remoteMediaList.filter {
                    val releaseDate = it.release_date
                    releaseDate != null && LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).isAfter(currentDate)
                }

                val media = remoteMediaList.map {
                    it.toMedia(
                        type = it.media_type ?: Constants.unavailable,
                        category = Constants.UPCOMING
                    )
                }

                val entities = remoteMediaList.map {
                    it.toMediaEntity(
                        type = it.media_type ?: Constants.unavailable,
                        category = Constants.UPCOMING
                    )
                }

                mediaDao.insertMediaList(entities)

                emit(Resource.Success(data = media))
            } catch (e: IOException) {
                Log.e("UpcomingMoviesList", "IOException occurred while fetching upcoming movies list: ${e.message}")
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                Log.e("UpcomingMoviesList", "HttpException occurred while fetching upcoming movies list: ${e.message}")
                emit(Resource.Error("Couldn't load data"))
            }

            emit(Resource.Loading(false))
        }
    }


    override suspend fun getAiringTodayTvSeriesList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(true))

            var searchPage = page
            if (isRefresh) {
                mediaDao.deleteAiringTodayTvSeries(mediaType = "tv", category = Constants.AIRING)
                searchPage = 1
            }

            try {
                Log.d("UpcomingTvSeriesList", "Fetching upcoming TvSeries list from API (page: $searchPage)")
                val remoteMediaList = mediaApiService.getAiringTodayTvSeriesList(searchPage, apiKey).results

                val media = remoteMediaList.map {
                    it.toMedia(
                        type = it.media_type ?: Constants.unavailable,
                        category = Constants.UPCOMING
                    )
                }

                val entities = remoteMediaList.map {
                    it.toMediaEntity(
                        type = it.media_type ?: Constants.unavailable,
                        category = Constants.UPCOMING
                    )
                }

                mediaDao.insertMediaList(entities)

                emit(Resource.Success(data = media))
            } catch (e: IOException) {
                Log.e("AiringTvSeriesList", "IOException occurred while fetching Airing Series list: ${e.message}")
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                Log.e("AiringTvSeriesList", "HttpException occurred while fetching Airing Series list: ${e.message}")
                emit(Resource.Error("Couldn't load data"))
            }

            emit(Resource.Loading(false))
        }
    }

    override suspend fun getNowPlayingMoviesList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMediaList = mediaDao.getNowPlayingMovies(mediaType = "movie", category = Constants.NOW_PLAYING)

            val shouldLoadLocal = localMediaList.isNotEmpty() && !fetchFromRemote

            if (shouldLoadLocal) {
                emit(
                    Resource.Success(
                        data = localMediaList.map { mediaEntity ->
                            mediaEntity.toMedia(
                                type = mediaEntity.mediaType ?: Constants.unavailable,
                                category = Constants.NOW_PLAYING
                            )
                        }
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            var searchPage = page
            if (isRefresh) {
                mediaDao.deleteNowPlayingMovies(mediaType = "movie", category = Constants.NOW_PLAYING)
                searchPage = 1
            }

            val remoteMediaList = try {
                mediaApiService.getNowPlayingMoviesList(
                    searchPage, apiKey
                ).results
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            }

            val media = remoteMediaList.map {
                it.toMedia(
                    type = it.media_type ?: Constants.unavailable,
                    category = Constants.NOW_PLAYING
                )
            }

            val entities = remoteMediaList.map {
                it.toMediaEntity(
                    type = it.media_type ?: Constants.unavailable,
                    category = Constants.NOW_PLAYING
                )
            }

            mediaDao.insertMediaList(entities)

            emit(Resource.Success(data = media))
            emit(Resource.Loading(false))
        }
    }
}