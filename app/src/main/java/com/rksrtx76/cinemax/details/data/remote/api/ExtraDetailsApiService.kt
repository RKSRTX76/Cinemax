package com.rksrtx76.cinemax.details.data.remote.api

import com.rksrtx76.cinemax.details.data.remote.dto.cast.CastDto
import com.rksrtx76.cinemax.details.data.remote.dto.details.DetailsDto
import com.rksrtx76.cinemax.details.data.remote.dto.videos.VideosDto
import com.rksrtx76.cinemax.main.data.remote.dto.MediaListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ExtraDetailsApiService {
    @GET("{type}/{id}")
    suspend fun getDetails(
        @Path("type")
        type : String,
        @Path("id")
        id : Int,
        @Query("api_key")
        apiKey : String
    ): DetailsDto

    @GET("{type}/{id}/similar")
    suspend fun getSimilarMediaList(
        @Path("type")
        type : String,
        @Path("id")
        id : Int,
        @Query("page")
        page : Int,
        @Query("api_key")
        apiKey : String
    ): MediaListDto

    @GET("{type}/{id}/videos")
    suspend fun getVideosList(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): VideosDto?

    @GET("{type}/{id}/credits")
    suspend fun getCastList(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): CastDto

}