package com.rksrtx76.cinemax.details.domain.repository

import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.util.Resource
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {
    suspend fun getDetails(
        type : String,
        isRefresh : Boolean,
        id : Int,
        apiKey : String
    ): Flow<Resource<Media>>

}