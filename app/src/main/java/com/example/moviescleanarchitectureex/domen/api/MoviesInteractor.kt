package com.example.moviescleanarchitectureex.domen.api

import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.domen.models.MovieCast
import com.example.moviescleanarchitectureex.domen.models.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesInteractor {
    suspend fun searchMovies(expression: String): Flow<Pair<List<Movie>?, String?>>
    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
    suspend fun getMovieDetails(movieId: String): Flow<Pair<MovieDetails?, String?>>
    suspend fun getMoviesCast(movieId: String): Flow<Pair<MovieCast?, String?>>


}