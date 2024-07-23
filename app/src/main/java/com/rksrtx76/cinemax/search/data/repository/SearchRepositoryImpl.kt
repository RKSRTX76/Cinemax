package com.rksrtx76.cinemax.search.data.repository

import com.rksrtx76.cinemax.main.data.mappers.toMedia
import com.rksrtx76.cinemax.main.data.remote.api.MediaApiService
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.search.domain.repository.SearchRepository
import com.rksrtx76.cinemax.util.Constants
import com.rksrtx76.cinemax.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val mediaApiService: MediaApiService,
): SearchRepository {

    override suspend fun getSearchList(
        fetchFromRemote: Boolean,
        query: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>> {

        return flow{
            emit(Resource.Loading(true))

            val remoteMediaList = try{
                mediaApiService.getSearchList(query, page, apiKey).results.map { media->
                    media.toMedia(
                        type = media.media_type ?: Constants.unavailable,
                        category = media.category ?: Constants.unavailable
                    )
                }
            }catch (e : IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            }catch (e : HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Success(remoteMediaList))

            emit(Resource.Loading(false))
        }
    }
}