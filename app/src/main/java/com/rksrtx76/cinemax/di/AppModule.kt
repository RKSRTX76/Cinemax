package com.rksrtx76.cinemax.di

import android.app.Application
import androidx.room.Room
import com.rksrtx76.cinemax.details.data.remote.api.ExtraDetailsApiService
import com.rksrtx76.cinemax.main.data.local.genres.GenresDataBase
import com.rksrtx76.cinemax.main.data.local.media.MediaDataBase
import com.rksrtx76.cinemax.main.data.remote.api.GenreApiService
import com.rksrtx76.cinemax.main.data.remote.api.MediaApiService
import com.rksrtx76.cinemax.main.data.remote.api.MediaApiService.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }



    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideGenreDataBase(app : Application): GenresDataBase{
        return Room.databaseBuilder(
            app,
            GenresDataBase::class.java,
            "genres.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMediaDatabase(app : Application): MediaDataBase{
        return Room.databaseBuilder(
            app,
            MediaDataBase::class.java,
            "media_database.db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideMediaApi() : MediaApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(MediaApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGenreApi() : GenreApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(GenreApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideExtraDetailsApi() : ExtraDetailsApiService{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(ExtraDetailsApiService::class.java)
    }

}