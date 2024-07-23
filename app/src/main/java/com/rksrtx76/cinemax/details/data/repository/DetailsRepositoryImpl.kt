package com.rksrtx76.cinemax.details.data.repository

import com.rksrtx76.cinemax.details.data.remote.api.ExtraDetailsApiService
import com.rksrtx76.cinemax.details.data.remote.dto.details.DetailsDto
import com.rksrtx76.cinemax.details.domain.repository.DetailsRepository
import com.rksrtx76.cinemax.main.data.local.media.MediaDataBase
import com.rksrtx76.cinemax.main.data.mappers.toMedia
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.util.Constants
import com.rksrtx76.cinemax.util.Resource
import com.rksrtx76.cinemax.util.Resource.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepositoryImpl @Inject constructor(
    private val extraDetailsApiService: ExtraDetailsApiService,
    mediaDb : MediaDataBase
): DetailsRepository {

    private val mediaDao = mediaDb.mediaDao

    override suspend fun getDetails(
        type: String,
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Media>> {
        return flow {
            emit(Loading(true))

            val mediaEntity = mediaDao.getMediaById(id)

            val isDetailsExist = !(mediaEntity.runtime == null || mediaEntity.status ==null || mediaEntity.tagline == null)

            if(!isRefresh && isDetailsExist){
                emit(
                    Success(
                        data = mediaEntity.toMedia(
                            type = mediaEntity.mediaType ?: Constants.MOVIE,
                            category = mediaEntity.category ?: Constants.POPULAR
                        )
                    )
                )
                emit(Loading(false))
                return@flow
            }

            val remoteDetails = fetchRemoteForDetails(
                type = mediaEntity.mediaType ?: Constants.MOVIE,
                id = id,
                apiKey = apiKey
            )

            if(remoteDetails ==  null){
                emit(
                    Success(
                        data = mediaEntity.toMedia(
                            type = mediaEntity.mediaType ?: Constants.MOVIE,
                            category = mediaEntity.category ?: Constants.POPULAR
                        )
                    )
                )
                emit(Loading(false))
                return@flow
            }

            remoteDetails.let { details ->
                mediaEntity.runtime = details.runtime
                mediaEntity.status = details.status
                mediaEntity.tagline = details.tagline

                mediaDao.updateMediaItem(mediaEntity)

                emit(
                    Success(
                        data = mediaEntity.toMedia(
                            type = mediaEntity.mediaType ?: Constants.MOVIE,
                            category = mediaEntity.category ?: Constants.POPULAR
                        )
                    )
                )
                emit(Loading(false))
            }
        }
    }

    private suspend fun fetchRemoteForDetails(
        type: String,
        id: Int,
        apiKey: String
    ): DetailsDto? {
        val remoteDetails = try{
            extraDetailsApiService.getDetails(
                type = type,
                id = id,
                apiKey= apiKey
            )
        }catch (e : IOException){
            e.printStackTrace()
            null
        }catch (e : HttpException){
            e.printStackTrace()
            null
        }
        return remoteDetails
    }
}