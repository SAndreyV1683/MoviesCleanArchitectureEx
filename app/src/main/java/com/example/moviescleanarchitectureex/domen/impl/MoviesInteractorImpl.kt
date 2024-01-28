package com.example.moviescleanarchitectureex.domen.impl

import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.util.Resource
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
       executor.execute {
           when (val resource = repository.searchMovie(expression)) {
               is Resource.Success -> { consumer.consume(resource.data, null) }
               is Resource.Error -> { consumer.consume(null, resource.message) }
           }
       }
    }
}