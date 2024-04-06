package com.example.moviescleanarchitectureex.domen.api

import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.domen.models.MovieCast
import com.example.moviescleanarchitectureex.domen.models.MovieDetails
import com.example.moviescleanarchitectureex.util.Resource
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun searchMovie(expression: String): Flow<Resource<List<Movie>>>
    fun addMovieToFavorites(movie: Movie)
    fun removeMovieFromFavorites(movie: Movie)
    suspend fun getMovieDetails(movieId: String): Flow<Resource<MovieDetails>>
    suspend fun getMovieCast(movieId: String): Flow<Resource<MovieCast>>

}