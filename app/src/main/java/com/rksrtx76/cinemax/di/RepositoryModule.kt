package com.rksrtx76.cinemax.di

import com.rksrtx76.cinemax.details.data.repository.DetailsRepositoryImpl
import com.rksrtx76.cinemax.details.data.repository.ExtraDetailsRepositoryImpl
import com.rksrtx76.cinemax.details.domain.repository.DetailsRepository
import com.rksrtx76.cinemax.details.domain.repository.ExtraDetailsRepository
import com.rksrtx76.cinemax.main.data.repository.GenreRepositoryImpl
import com.rksrtx76.cinemax.main.data.repository.MediaRepositoryImpl
import com.rksrtx76.cinemax.main.domain.repository.GenreRepository
import com.rksrtx76.cinemax.main.domain.repository.MediaRepository
import com.rksrtx76.cinemax.search.data.repository.SearchRepositoryImpl
import com.rksrtx76.cinemax.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMediaListRepository(
        mediaRepositoryImpl: MediaRepositoryImpl
    ):MediaRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindsGenreRepository(
        genreRepositoryImpl: GenreRepositoryImpl
    ): GenreRepository

    @Binds
    @Singleton
    abstract fun bindDetailsRepository(
        detailsRepositoryImpl: DetailsRepositoryImpl
    ): DetailsRepository

    @Binds
    @Singleton
    abstract fun bindExtraDetailsRepository(
        extraDetailsRepositoryImpl: ExtraDetailsRepositoryImpl
    ): ExtraDetailsRepository

}