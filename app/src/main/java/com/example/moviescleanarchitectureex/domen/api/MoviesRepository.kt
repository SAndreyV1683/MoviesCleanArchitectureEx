package com.example.moviescleanarchitectureex.domen.api

import com.example.moviescleanarchitectureex.domen.models.Movie

interface MoviesRepository {
    fun searchMovie(expression: String): List<Movie>
}