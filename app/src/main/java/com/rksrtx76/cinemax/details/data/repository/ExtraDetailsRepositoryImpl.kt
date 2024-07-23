package com.rksrtx76.cinemax.details.data.repository

import android.util.Log
import com.rksrtx76.cinemax.details.data.remote.api.ExtraDetailsApiService
import com.rksrtx76.cinemax.details.domain.repository.ExtraDetailsRepository
import com.rksrtx76.cinemax.main.data.local.media.MediaDataBase
import com.rksrtx76.cinemax.main.data.local.media.MediaEntity
import com.rksrtx76.cinemax.main.data.mappers.toMedia
import com.rksrtx76.cinemax.main.data.mappers.toMediaEntity
import com.rksrtx76.cinemax.main.data.remote.dto.MediaDto
import com.rksrtx76.cinemax.main.domain.model.CastMember
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.util.Constants
import com.rksrtx76.cinemax.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExtraDetailsRepositoryImpl @Inject constructor(
    private val extraDetailsApiService: ExtraDetailsApiService,
    mediaDb : MediaDataBase
): ExtraDetailsRepository {

    private  val mediaDao = mediaDb.mediaDao

    override suspend fun getSimilarMediaList(
        isRefresh: Boolean,
        type: String,
        id: Int,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(true))
            val mediaEntity = mediaDao.getMediaById(id)

            val doesSimilarMediaListExist = (mediaEntity.similarMediaList != null && mediaEntity.similarMediaList != "-1,-2")

            if(!isRefresh && doesSimilarMediaListExist){
                try {
                    val similarMediaListIds = mediaEntity.similarMediaList?.split(",")!!.map {
                        it.toInt()
                    }
                    val similarMediaEntityList = ArrayList<MediaEntity>()
                    for ( i in similarMediaListIds.indices){
                        similarMediaEntityList.add(mediaDao.getMediaById(similarMediaListIds[i]))
                    }
                    emit(
                        Resource.Success(
                            data = similarMediaEntityList.map{
                                it.toMedia(
                                    type = it.mediaType ?: Constants.MOVIE,
                                    category = it.category ?: Constants.POPULAR
                                )
                            }
                        ))
                }catch (e : Exception){
                    emit(Resource.Error("Something went wrong"))
                }

                emit(Resource.Loading(false))
                return@flow
            }

            val remoteSimilarMediaList = fetchRemoteForSimilarMediaList(
                type = mediaEntity.mediaType ?: Constants.MOVIE,
                id = id,
                page = page,
                apiKey = apiKey
            )

            if(remoteSimilarMediaList == null){
                emit(
                    Resource.Success(
                        data = emptyList()
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            remoteSimilarMediaList.let { similarMediaList ->
                val similarMediaListIntIds = ArrayList<Int>()
                for(i in similarMediaList.indices){
                    similarMediaListIntIds.add(similarMediaList[i].id ?: -1)
                }

                mediaEntity.similarMediaList = try {
                    similarMediaListIntIds.joinToString(",")
                }catch (e: Exception){
                    "-1,-2"
                }

                val similarMediaEntityList = remoteSimilarMediaList.map {
                    it.toMediaEntity(
                        type = it.media_type ?: Constants.MOVIE,
                        category = mediaEntity.category ?: Constants.POPULAR
                    )
                }

                mediaDao.insertMediaList(similarMediaEntityList)
                mediaDao.updateMediaItem(mediaEntity)

                emit(
                    Resource.Success(
                        data = similarMediaEntityList.map{
                            it.toMedia(
                                type = it.mediaType ?: Constants.MOVIE,
                                category = it.category ?: Constants.POPULAR
                            )
                        }
                    ))

                emit(Resource.Loading(false))
            }
        }
    }

    private suspend fun fetchRemoteForSimilarMediaList(
        type: String,
        id: Int,
        page: Int,
        apiKey: String
    ): List<MediaDto>? {

        val remoteSImilarMediaList = try {
            extraDetailsApiService.getSimilarMediaList(
                type = type,
                id = id,
                page = page,
                apiKey = apiKey
            )
        }catch (e: Exception){
            e.printStackTrace()
            null
        }catch (e : HttpException){
            e.printStackTrace()
            null
        }
        return remoteSImilarMediaList?.results
    }

    override suspend fun getVideosList(
        isRefresh: Boolean,
        type: String,
        id: Int,
        apiKey: String
    ): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading())
        try {
            val videoIds = fetchRemoteForVideoIds(type, id, apiKey)
            Log.d("EmittedVideoIds", videoIds.toString()) // Add this line to log the emitted video IDs
            if (videoIds != null && videoIds.isNotEmpty()) {
                emit(Resource.Success(videoIds))
            } else {
                emit(Resource.Error("No videos found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
    private suspend fun fetchRemoteForVideoIds(
        type: String,
        id: Int,
        apiKey: String
    ): List<String>? {
        return try {
            val response = extraDetailsApiService.getVideosList(type, id, apiKey)
            Log.d("VideosResponse", response.toString())
            val videoKeys = response?.results
                ?.filter { it.site == "YouTube" && (it.type == "Trailer" || it.type == "Teaser") }
                ?.mapNotNull { it.key }
            Log.d("VideoKeys", videoKeys.toString()) // Add this line to log the extracted keys
            videoKeys
        } catch (e: Exception) {
            Log.e("VideosError", "Error fetching videos: ${e.localizedMessage}")
            null
        }
    }
    override suspend fun getCastList(
        isRefresh: Boolean,
        type: String,
        id: Int,
        apiKey: String
    ): Flow<Resource<List<CastMember>>> = flow {
        emit(Resource.Loading())
        try {
            val castList = fetchRemoteForCastList(type, id, apiKey)
            if (castList != null && castList.isNotEmpty()) {
                emit(Resource.Success(castList))
            } else {
                emit(Resource.Error("No cast details found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

    private suspend fun fetchRemoteForCastList(
        type: String,
        id: Int,
        apiKey: String
    ): List<CastMember>? {
        return try {
            val response = extraDetailsApiService.getCastList(type, id, apiKey)
            response.cast.map {
                CastMember(
                    castId = it.cast_id,
                    character = it.character,
                    name = it.name,
                    profilePath = it.profile_path
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}