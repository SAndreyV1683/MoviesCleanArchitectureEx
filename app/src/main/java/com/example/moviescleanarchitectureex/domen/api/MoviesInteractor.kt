package com.example.moviescleanarchitectureex.domen.api

import com.example.moviescleanarchitectureex.domen.models.Movie

interface MoviesInteractor {
    fun searchMovies(expression: String, consumer: MoviesConsumer)
    interface MoviesConsumer {
        fun consume(foundMovies: List<Movie>)
    }
}