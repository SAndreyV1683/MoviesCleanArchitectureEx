package com.example.moviescleanarchitectureex.domen.impl

import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
       executor.execute {
           consumer.consume(repository.searchMovie(expression))
       }
    }
}