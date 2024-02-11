package com.example.moviescleanarchitectureex.data

import com.example.moviescleanarchitectureex.data.dto.MoviesSearchRequest
import com.example.moviescleanarchitectureex.data.dto.MoviesSearchResponse
import com.example.moviescleanarchitectureex.data.localstorage.LocalStorage
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.util.Resource
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor (
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage
) : MoviesRepository {
    override fun searchMovie(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> Resource.Error("Проверте подключение к интернету")

            200 -> {
                val stored = localStorage.getSavedFavorites()
                Resource.Success((response as MoviesSearchResponse).results.map {
                    Movie(it.id, it.resultType, it.image, it.title, it.description, stored.contains(it.id))
                })
            }

            else -> Resource.Error("Ошибка сервера")
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        localStorage.addToFavorites(movie.id)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        localStorage.removeFromFavorites(movie.id)
    }
}