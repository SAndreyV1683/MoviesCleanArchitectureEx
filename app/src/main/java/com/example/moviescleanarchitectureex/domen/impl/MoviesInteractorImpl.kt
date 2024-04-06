package com.example.moviescleanarchitectureex.domen.impl

import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.domen.models.MovieCast
import com.example.moviescleanarchitectureex.domen.models.MovieDetails
import com.example.moviescleanarchitectureex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors
import javax.inject.Inject

val TAG = MoviesInteractorImpl::class.simpleName
class MoviesInteractorImpl @Inject constructor(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()
    override suspend fun searchMovies(expression: String): Flow<Pair<List<Movie>?, String?>> {
        return repository.searchMovie(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }



    }

    override fun addMovieToFavorites(movie: Movie) {
        repository.addMovieToFavorites(movie)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        repository.removeMovieFromFavorites(movie)
    }

    override suspend fun getMovieDetails(movieId: String): Flow<Pair<MovieDetails?, String?>> {

        return repository.getMovieDetails(movieId).map { result->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> Pair(result.data, result.message)
            }
        }

    }

    override suspend fun getMoviesCast(movieId: String): Flow<Pair<MovieCast?, String?>> {
        return repository.getMovieCast(movieId).map {  result ->
            when (result) {
                is Resource.Success -> { Pair(result.data, null) }
                is Resource.Error -> { Pair(result.data, result.message) }
            }
        }

    }
}