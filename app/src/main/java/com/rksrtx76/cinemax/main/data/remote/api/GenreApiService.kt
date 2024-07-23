package com.rksrtx76.cinemax.main.data.remote.api

import com.rksrtx76.cinemax.main.data.remote.dto.GenreListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GenreApiService {
    @GET("genre/{type}/list")
    suspend fun getGenreList(
        @Path("type")
        genre : String,
        @Query("api_key")
        apiKey : String
    ): GenreListDto
}