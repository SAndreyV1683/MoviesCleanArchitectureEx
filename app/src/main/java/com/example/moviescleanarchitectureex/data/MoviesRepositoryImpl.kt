package com.example.moviescleanarchitectureex.data

import com.example.moviescleanarchitectureex.data.dto.MoviesSearchRequest
import com.example.moviescleanarchitectureex.data.dto.MoviesSearchResponse
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.domen.models.Movie

class MoviesRepositoryImpl(private val networkClient: NetworkClient): MoviesRepository {
    override fun searchMovie(expression: String): List<Movie> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as MoviesSearchResponse).results.map {
                Movie(it.id, it.resultType, it.image, it.title, it.description)
            }
        } else {
            return emptyList()
        }
    }
}