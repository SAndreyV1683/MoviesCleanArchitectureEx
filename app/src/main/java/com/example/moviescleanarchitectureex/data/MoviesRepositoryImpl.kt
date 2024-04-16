package com.example.moviescleanarchitectureex.data

import com.example.moviescleanarchitectureex.data.converters.MovieDbConvertor
import com.example.moviescleanarchitectureex.data.db.AppDatabase
import com.example.moviescleanarchitectureex.data.dto.MovieCastRequest
import com.example.moviescleanarchitectureex.data.dto.MovieCastResponse
import com.example.moviescleanarchitectureex.data.dto.MovieDetailsRequest
import com.example.moviescleanarchitectureex.data.dto.MovieDetailsResponse
import com.example.moviescleanarchitectureex.data.dto.MovieDto
import com.example.moviescleanarchitectureex.data.dto.MoviesSearchRequest
import com.example.moviescleanarchitectureex.data.dto.MoviesSearchResponse
import com.example.moviescleanarchitectureex.data.localstorage.LocalStorage
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.domen.models.MovieCast
import com.example.moviescleanarchitectureex.domen.models.MovieCastPerson
import com.example.moviescleanarchitectureex.domen.models.MovieDetails
import com.example.moviescleanarchitectureex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor (
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: MovieDbConvertor,
) : MoviesRepository {
    override suspend fun searchMovie(expression: String): Flow<Resource<List<Movie>>> = flow {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        when (response.resultCode) {
            -1 -> emit(Resource.Error("Проверте подключение к интернету"))

            200 -> {
                val stored = localStorage.getSavedFavorites()
                val data = Resource.Success((response as MoviesSearchResponse).results.map {
                    Movie(it.id, it.resultType, it.image, it.title, it.description, stored.contains(it.id))
                })
                saveMovie(response.results)
                emit(data)
            }

            else -> emit(Resource.Error("Ошибка сервера"))
        }
    }

    private suspend fun saveMovie(movies: List<MovieDto>) {
        val movieEntities = movies.map { movie -> movieDbConvertor.map(movie) }
        appDatabase.movieDao().insertMovies(movieEntities)
    }

    override fun addMovieToFavorites(movie: Movie) {
        localStorage.addToFavorites(movie.id)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        localStorage.removeFromFavorites(movie.id)
    }

    override suspend fun getMovieDetails(movieId: String): Flow<Resource<MovieDetails>> = flow {
        val response = networkClient.doRequest(MovieDetailsRequest(movieId))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                with(response as MovieDetailsResponse) {
                    val data = Resource.Success(
                        MovieDetails(
                            id?: "", title, imDbRating ?: "", year,
                            countries, genres, directors, writers, stars, plot
                        )
                    )
                    emit(data)
                }
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override suspend fun getMovieCast(movieId: String): Flow<Resource<MovieCast>> = flow {
        val response = networkClient.doRequest(MovieCastRequest(movieId = movieId))
        when(response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                val data = with(response as MovieCastResponse) {
                    Resource.Success(
                        data = MovieCast(
                            imdbId = this.imDbId,
                            fullTitle = this.fullTitle,
                            directors = this.directors.items.map { director ->
                                MovieCastPerson(
                                    id = director.id,
                                    name = director.name,
                                    description = director.description,
                                    image = null,
                                )
                            },
                            others = this.others.flatMap { othersResponse ->
                                othersResponse.items.map { person ->
                                    MovieCastPerson(
                                        id = person.id,
                                        name = person.name,
                                        description = "${othersResponse.job} -- ${person.description}",
                                        image = null,
                                    )
                                }
                            },
                            writers = this.writers.items.map { writer ->
                                MovieCastPerson(
                                    id = writer.id,
                                    name = writer.name,
                                    description = writer.description,
                                    image = null,
                                )
                            },
                            actors = this.actors.map { actor ->
                                MovieCastPerson(
                                    id = actor.id,
                                    name = actor.name,
                                    description = actor.asCharacter,
                                    image = actor.image,
                                )
                            }
                        )
                    )
                }
                emit(data)
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }


}