package com.example.moviescleanarchitectureex.domen.impl

import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.util.Resource
import java.util.concurrent.Executors
import javax.inject.Inject

val TAG = MoviesInteractorImpl::class.simpleName
class MoviesInteractorImpl @Inject constructor(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {

        executor.execute {
            when (val resource = repository.searchMovie(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
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

    override fun getMovieDetails(movieId: String, consumer: MoviesInteractor.MovieDetailsConsumer) {
        executor.execute {
            when (val resource = repository.getMovieDetails(movieId)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }
                is Resource.Error -> consumer.consume(resource.data, resource.message)
            }
        }
    }

    override fun getMoviesCast(movieId: String, consumer: MoviesInteractor.MovieCastConsumer) {
        executor.execute {
            when (val resource = repository.getMovieCast(movieId)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(resource.data, resource.message) }
            }
        }
    }
}