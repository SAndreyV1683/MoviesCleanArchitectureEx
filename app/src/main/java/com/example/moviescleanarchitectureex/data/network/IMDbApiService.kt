package com.example.moviescleanarchitectureex.data.network

import com.example.moviescleanarchitectureex.data.dto.MovieCastResponse
import com.example.moviescleanarchitectureex.data.dto.MovieDetailsResponse
import com.example.moviescleanarchitectureex.data.dto.MoviesSearchResponse
import com.example.moviescleanarchitectureex.data.dto.NamesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
//https://tv-api.com/API/FullCast/k_zcuw1ytf/tt0110413
interface IMDbApiService {
    @GET("/en/API/SearchMovie/k_zcuw1ytf/{expression}")
    suspend fun findMovies(@Path("expression") expression: String): MoviesSearchResponse
    @GET("/en/API/Title/k_zcuw1ytf/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id")movieId: String): MovieDetailsResponse
    @GET("/en/API/FullCast/k_zcuw1ytf/{movie_id}")
    suspend fun getFullCast(@Path("movie_id") movieId: String): MovieCastResponse
    @GET("/en/API/SearchName/k_zcuw1ytf/{expression}")
    suspend fun searchNames(@Path("expression") expression: String): NamesSearchResponse
}