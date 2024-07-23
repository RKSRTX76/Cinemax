package com.rksrtx76.cinemax.search.domain.repository

import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getSearchList(
        fetchFromRemote: Boolean,
        query: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>>
}