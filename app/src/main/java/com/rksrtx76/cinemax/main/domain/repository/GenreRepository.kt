package com.rksrtx76.cinemax.main.domain.repository

import com.rksrtx76.cinemax.main.domain.model.Genre
import com.rksrtx76.cinemax.util.Resource
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    suspend fun getGenres(
        fetchFromRemote : Boolean,
        type : String,
        apiKey : String
    ) : Flow<Resource<List<Genre>>>
}