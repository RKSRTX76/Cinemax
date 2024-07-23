package com.rksrtx76.cinemax.main.data.repository

import android.app.Application
import coil.network.HttpException
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.main.data.local.genres.GenreEntity
import com.rksrtx76.cinemax.main.data.local.genres.GenresDataBase
import com.rksrtx76.cinemax.main.data.remote.api.GenreApiService
import com.rksrtx76.cinemax.main.domain.model.Genre
import com.rksrtx76.cinemax.main.domain.repository.GenreRepository
import com.rksrtx76.cinemax.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GenreRepositoryImpl @Inject constructor(
    private val application: Application,
    private val genreApiService : GenreApiService,
    genreDb : GenresDataBase
): GenreRepository {

    private val genreDao = genreDb.genreDao

    override suspend fun getGenres(
        fetchFromRemote: Boolean,
        type: String,
        apiKey: String
    ): Flow<Resource<List<Genre>>> {
        return flow {
            emit(Resource.Loading(true))

            val genresEntity = genreDao.getGenres(type)

            if (genresEntity.isNotEmpty() && !fetchFromRemote) {
                emit(
                    Resource.Success(
                        genresEntity.map { genresEntity ->
                            Genre(
                                id = genresEntity.id,
                                name = genresEntity.name
                            )
                        }
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteGenreList = try {
                genreApiService.getGenreList(
                    type, apiKey
                ).genres
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
                emit(Resource.Loading(false))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
                emit(Resource.Loading(false))
                return@flow
            }

            remoteGenreList.let { mediaList ->
                genreDao.insertGenres(
                    remoteGenreList.map { remoteGenre ->
                        GenreEntity(
                            id = remoteGenre.id,
                            name = remoteGenre.name,
                            type = type
                        )
                    }
                )

                emit(
                    Resource.Success(remoteGenreList)
                )
                emit(Resource.Loading(false))
            }
        }

    }
}