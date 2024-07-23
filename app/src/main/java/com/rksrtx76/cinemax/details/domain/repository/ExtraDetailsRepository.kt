package com.rksrtx76.cinemax.details.domain.repository

import com.rksrtx76.cinemax.main.domain.model.CastMember
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.util.Resource
import kotlinx.coroutines.flow.Flow

interface ExtraDetailsRepository {
    suspend fun getSimilarMediaList(
        isRefresh : Boolean,
        type : String,
        id : Int,
        page : Int,
        apiKey : String
    ): Flow<Resource<List<Media>>>


    suspend fun getVideosList(
        isRefresh: Boolean,
        type: String,
        id : Int,
        apiKey: String
    ): Flow<Resource<List<String>>>

    suspend fun getCastList(
        isRefresh: Boolean,
        type: String,
        id: Int,
        apiKey: String
    ): Flow<Resource<List<CastMember>>>
}