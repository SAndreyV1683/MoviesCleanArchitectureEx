package com.example.moviescleanarchitectureex.data.converters

import com.example.moviescleanarchitectureex.data.db.MovieEntity
import com.example.moviescleanarchitectureex.data.dto.MovieDto
import com.example.moviescleanarchitectureex.domen.models.Movie
import javax.inject.Inject

class MovieDbConvertor @Inject constructor() {
    fun map(movie: MovieDto): MovieEntity {
        return MovieEntity(
            movie.id,
            movie.resultType,
            movie.image,
            movie.title,
            movie.description
        )
    }

    fun map(movie: MovieEntity): Movie {
        return Movie(
            movie.id,
            movie.resultType,
            movie.image,
            movie.title,
            movie.description,
            false
        )
    }
}