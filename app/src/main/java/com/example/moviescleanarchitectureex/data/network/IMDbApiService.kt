package com.example.moviescleanarchitectureex.data.network

import com.example.moviescleanarchitectureex.data.dto.MoviesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IMDbApiService {
    @GET("/en/API/SearchMovie/YOUR_API_KEY/{expression}")
    fun findMovies(@Path("expression") expression: String): Call<MoviesSearchResponse>
}